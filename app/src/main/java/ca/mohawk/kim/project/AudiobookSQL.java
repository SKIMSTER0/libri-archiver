package ca.mohawk.kim.project;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

/**
 * SQL defining audiobooks table schema
 */
public class AudiobookSQL extends android.database.sqlite.SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "db_libriarchive";
    public static final String SQL_CREATE_AUDIOBOOKS =
            "CREATE TABLE " + AudiobookContract.AudiobookEntry.TABLE_NAME + "(" +
                    AudiobookContract.AudiobookEntry._ID + " INTEGER PRIMARY KEY," +
                    AudiobookContract.AudiobookEntry.BOOK_ID + " INTEGER," +
                    AudiobookContract.AudiobookEntry.TITLE + " TEXT," +
                    AudiobookContract.AudiobookEntry.AUTHOR + " TEXT," +
                    AudiobookContract.AudiobookEntry.DESCRIPTION + " TEXT," +
                    AudiobookContract.AudiobookEntry.TOTAL_TIME_SECS + " INTEGER," +
                    AudiobookContract.AudiobookEntry.YEAR_PUBLISHED + " INTEGER," +
                    AudiobookContract.AudiobookEntry.DOWNLOAD_LINK + " TEXT)";

    public AudiobookSQL(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_AUDIOBOOKS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
