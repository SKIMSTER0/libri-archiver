package ca.mohawk.kim.project;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Downloads json from librivox JSON asynchronously in the background, populates web audiobook list
 */
public class RetrieveLibrivox extends AsyncTask<String, Void, String> {
    public static final String TAG = "==RetrieveLibrivox==";

    private Context context;
    private Toast errorAPI;
    private String language;
    private int totalTimeSecsMax;
    private int copyrightYear;

    public RetrieveLibrivox(Context context){
        this.context = context;

        // retrieve settings options
        SharedPreferences sharedPreference = context.getSharedPreferences("libriarchive", Context.MODE_PRIVATE);
        language = sharedPreference.getString("language", "English");
        totalTimeSecsMax = sharedPreference.getInt("totalTimeSecsMax", 1000000);
        copyrightYear = sharedPreference.getInt("copyrightYear", 2000);

        Log.d(TAG, "settings language: " + language);
        Log.d(TAG, "total time seconds: " + totalTimeSecsMax);
        Log.d(TAG, "copyright year: " + copyrightYear);
    }

    /**
     * Downloads json data from librivox api
     * @param params
     * @return
     */
    @Override
    protected String doInBackground(String... params) {
        Log.d(TAG, "Starting Background Task");

        StringBuilder results = new StringBuilder();
        try {
            URL url = new URL(params[0]);
            String line = null;

            // open connection (get is default)
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // read response
            int statusCode = conn.getResponseCode();
            if (statusCode == 200) {
                InputStream inputStream = new BufferedInputStream(
                        conn.getInputStream());
                BufferedReader bufferedReader =
                        new BufferedReader(new InputStreamReader(inputStream,
                                "UTF-8"));
                while ((line = bufferedReader.readLine()) != null) {
                    results.append(line);
                }
            }
            Log.d(TAG, "Data received = " + results.length());
            Log.d(TAG, "Response Code: " + statusCode);
        } catch (IOException ex){
            Log.d(TAG, "Caught Exception: " + ex);
        }

        return results.toString();
    }

    /**
    * After download has completed associate, parse results
    * @param result - do nothing if results == null
    */
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        Activity main = (Activity) context;
        ListView browseWebList = main.findViewById(R.id.browseWebList);
        AudiobookContainer container = null;
        AudiobookContainer filteredContainer = new AudiobookContainer();
        filteredContainer.books = new ArrayList<Audiobook>();

        GsonBuilder builder = new GsonBuilder();
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        if (result == null) {
            Log.d(TAG, "No results, API may be down");
            errorAPI = Toast.makeText(MainActivity.getCurrentActivity(), "API not available, check your internet connection", Toast.LENGTH_SHORT);
            errorAPI.show();
            return;
        }
        else if (result.startsWith("<")) {
            Log.d(TAG, "invalid json, API may be broken");
            errorAPI = Toast.makeText(MainActivity.getCurrentActivity(), "API returns error page, maybe be broken", Toast.LENGTH_SHORT);
            errorAPI.show();
            return;
        }
        else {
            container = gson.fromJson(result, AudiobookContainer.class);
        }

        // if list populated connect adapter, clear otherwise
        if (container != null) {
            WebAdapter adapter = new WebAdapter(main, R.layout.audiobook_item);

            //filter book to settings
            for (Audiobook book : container.books){
                if (filterAdapter(book)){
                    filteredContainer.books.add(book);
                }
            }

            adapter.addAll(filteredContainer.books);

            browseWebList.setAdapter(adapter);
        } else {
            browseWebList.setAdapter(null);
            Toast noResults = Toast.makeText(MainActivity.getCurrentActivity(), "No results found!", Toast.LENGTH_SHORT);
            noResults.show();
        }
    }

    /**
     * Filter audiobook fields to app setting options
     * @param book
     * @return
     */
    private boolean filterAdapter(Audiobook book){
        boolean validBook = book.language.equals(language) &&
            book.totaltimesecs < totalTimeSecsMax &&
            Integer.parseInt(book.copyrightYear) < copyrightYear;

        return validBook;
    }

}
