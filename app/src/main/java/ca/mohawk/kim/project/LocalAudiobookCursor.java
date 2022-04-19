package ca.mohawk.kim.project;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Adapter class to handle local audiobook list
 */
public class LocalAudiobookCursor extends CursorAdapter {
    TextView titleText;
    TextView authorText;
    TextView totalTimeText;
    ImageView favoritedIcon;

    public LocalAudiobookCursor(Context context, Cursor c) {
        super(context, c);
    }

    /**
     * inflate new view and return
     * @param context
     * @param cursor
     * @param viewGroup
     * @return
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View listView = LayoutInflater.from(context).inflate(R.layout.audiobook_item, viewGroup ,false);
        //productList = productsListView.findViewById(R.id.productsList);

        //int pos = cursor.getPosition();
        return listView;
    }

    /**
     * bind all data to given view
     * @param view
     * @param context
     * @param cursor
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        titleText = view.findViewById(R.id.title);
        authorText = view.findViewById(R.id.author);
        totalTimeText = view.findViewById(R.id.totalTimeSecs);
        favoritedIcon = view.findViewById(R.id.favoritedIcon);

        String title = cursor.getString(cursor.getColumnIndexOrThrow(AudiobookContract.AudiobookEntry.TITLE));
        String author = cursor.getString(cursor.getColumnIndexOrThrow(AudiobookContract.AudiobookEntry.AUTHOR));
        String totalTimeSecs = cursor.getString(cursor.getColumnIndexOrThrow(AudiobookContract.AudiobookEntry.TOTAL_TIME_SECS));

        titleText.setText(title);
        authorText.setText(title);

        String minutes = String.format("%.2f", Integer.parseInt(totalTimeSecs) / 60d);
        totalTimeText.setText(minutes + " minutes");
        favoritedIcon.setVisibility(View.GONE);
    }

}