package ca.mohawk.kim.project;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;

/**
 * List of audiobooks retrieved from the librivox api
 */
public class BrowseWeb extends Fragment {
    public static final String TAG = "==BrowseWeb==";

    public ListView browseWebList;
    public String searchQuery;
    public SearchView searchText;
    public String filterLanguage;
    public int filterTotalTimeSecs;
    public int filterYear;
    public ImageView favoritedIcon;

    /**
     * required empty constructor
     */
    public BrowseWeb() {
    }

    /**
     * Initializes list view for audiobook web browsing, and search bar for search functionality
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_browse_web, container, false);

        // initiatize widgets
        browseWebList = view.findViewById(R.id.browseWebList);
        searchText = view.findViewById(R.id.searchText);
        ListView browseWebList = (ListView) view.findViewById(R.id.browseWebList);

        // retrieve shared preferences for filter values
        SharedPreferences sharedPreferences = MainActivity.getCurrentActivity().getSharedPreferences("libriarchive", Context.MODE_PRIVATE);
        filterLanguage = sharedPreferences.getString("language", "english");
        filterTotalTimeSecs = sharedPreferences.getInt("total_time_secs", 0);
        filterYear = sharedPreferences.getInt("copyright_year", 1800);

        // search bar handler, retrieve web API every time search submitted
        searchText.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String search) {

                searchQuery = String.format(getResources().getString(R.string.searchWebQueryTitle), search);
                retrieveLibrivox(searchQuery);

                Log.d(TAG, "Submitted Search Query: " + searchQuery);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String search) {
                return false;
            }
        });

        return view;
    }

    /**
     * Download json data of librivox audiobooks from web api working asynchronously in background
     * @param URI
     */
    public void retrieveLibrivox(String URI){
        RetrieveLibrivox retrieveLibrivox = new RetrieveLibrivox(this.getContext());
        retrieveLibrivox.execute(URI);
    }

    /**
     * Exports data
     */
    public void exportData(){

    }
}