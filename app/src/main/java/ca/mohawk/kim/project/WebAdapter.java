package ca.mohawk.kim.project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Adapter class to handle web audiobook list
 */
public class WebAdapter extends ArrayAdapter<Audiobook> {
    public static final String TAG = "==WebAdapter==";

    private TextView titleText;
    private TextView authorText;
    private TextView totalTimeText;
    private ImageView favoritedIcon;
    private Drawable favoritedIconOn;
    private Drawable favoritedIconOff;

    public WebAdapter(@NonNull Context context, int resource) {
        super(context, resource);

        favoritedIconOn = context.getDrawable(android.R.drawable.star_big_on);
        favoritedIconOff = context.getDrawable(android.R.drawable.star_big_off);
    }

    /**
     * Sets custom list view for audiobook item
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.audiobook_item, parent ,false);

        titleText = view.findViewById(R.id.title);
        authorText = view.findViewById(R.id.author);
        totalTimeText = view.findViewById(R.id.totalTimeSecs);
        favoritedIcon = view.findViewById(R.id.favoritedIcon);

        Audiobook book = super.getItem(position);
        boolean isFavorited = isAlreadyFavorited(book);

        //query database to see if already favorited
        if (isFavorited){
            favoritedIcon.setImageDrawable(favoritedIconOn);
            Log.d(TAG, "Item inserted into local database");
        } else {
            favoritedIcon.setImageDrawable(favoritedIconOff);
            Log.d(TAG, "Item deleted from local database");
        }

        // set onclick handler for favorited icon
        favoritedIcon.setOnClickListener((clickView) -> favoritedOnClick(clickView, book, isFavorited));

        // set list widget texts
        titleText.setText(book.title);
        authorText.setText(book.getAuthors());
        totalTimeText.setText(book.getTimeInMinutes());
        return view;
    }

    /**
     * Check if audiobook item has already been favorited (appears in local database), set favorited icon
     * @param book
     * @return
     */
    public boolean isAlreadyFavorited(Audiobook book){
        AudiobookSQL dbHelper = new AudiobookSQL(this.getContext(), AudiobookContract.AudiobookEntry.TABLE_NAME, null, 1);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor selectFavorited = db.rawQuery("SELECT * FROM " + AudiobookContract.AudiobookEntry.TABLE_NAME + " WHERE book_id = " + book.id, null);
        selectFavorited.moveToFirst();
        boolean isLast = selectFavorited.isLast();
        selectFavorited.close();

        return isLast;
    }

    /**
     * Onclick handler for clicking on favorited icon
     * @param view
     * @param book
     * @param isFavorited
     */
    public void favoritedOnClick(View view, Audiobook book, Boolean isFavorited){
        //add to database
        AudiobookSQL dbHelper = new AudiobookSQL(this.getContext(), AudiobookContract.AudiobookEntry.TABLE_NAME, null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //switch favorited icon, add to database if not favorited, delete if already favorited
        if (isAlreadyFavorited(book)){
            ((ImageView)view).setImageDrawable(favoritedIconOff);

            db.execSQL("DELETE FROM " + AudiobookContract.AudiobookEntry.TABLE_NAME + " WHERE book_id = " + book.id);
        } else {
            ((ImageView)view).setImageDrawable(favoritedIconOn);

            ContentValues values = new ContentValues();
            values.put(AudiobookContract.AudiobookEntry.BOOK_ID, book.id);
            values.put(AudiobookContract.AudiobookEntry.TITLE, book.title);
            values.put(AudiobookContract.AudiobookEntry.DESCRIPTION, book.description);
            values.put(AudiobookContract.AudiobookEntry.DOWNLOAD_LINK, book.urlZipFile);
            values.put(AudiobookContract.AudiobookEntry.TOTAL_TIME_SECS, book.totaltimesecs);
            values.put(AudiobookContract.AudiobookEntry.YEAR_PUBLISHED, book.copyrightYear);

            db.insert(AudiobookContract.AudiobookEntry.TABLE_NAME, null, values);
        }

        db.close();

        Log.d(TAG, "Clicked Favorited");
    }
}
