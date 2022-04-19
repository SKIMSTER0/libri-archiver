package ca.mohawk.kim.project;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * List of local audiobooks favorited from the web list
 */
public class BrowseLocal extends Fragment implements AdapterView.OnItemClickListener {
    public static final String TAG = "==BrowseLocal==";

    ListView browseLocalList;

    public BrowseLocal() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View localView = inflater.inflate(R.layout.fragment_browse_local, container, false);

        browseLocalList = localView.findViewById(R.id.browseLocalList);
        readAll(browseLocalList);

        return localView;
    }

    /**
     * reads all products from audiobooks table
     */
    private void readAll(ListView browseLocalList) {
        //SimpleCursorAdapter adapter1 = new SimpleCursorAdapter()
        //read from database
        AudiobookSQL dbHelper = new AudiobookSQL(this.getContext(), AudiobookContract.AudiobookEntry.TABLE_NAME, null, 1);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        //which columns to extract from query
        String[] projection = {
                AudiobookContract.AudiobookEntry.TITLE
        };

        Cursor cursor = db.query(
                AudiobookContract.AudiobookEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        LocalAudiobookCursor adapter = new LocalAudiobookCursor(this.getContext(), cursor);
        browseLocalList.setAdapter(adapter);

        Log.d(TAG, "Read all audiobooks");
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
    }
}