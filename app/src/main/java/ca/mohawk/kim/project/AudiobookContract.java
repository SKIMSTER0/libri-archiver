package ca.mohawk.kim.project;

import android.provider.BaseColumns;
/**
 * SQLOpenHelper contract describing audiobook table
 */
public class AudiobookContract {

    /**
     * Columns of the audiobooks table
     */
    public static class AudiobookEntry implements BaseColumns {
        public static final String TABLE_NAME = "audiobooks";
        public static final String BOOK_ID = "book_id";
        public static final String TITLE = "title";
        public static final String DESCRIPTION = "description";
        public static final String AUTHOR = "author";
        public static final String TOTAL_TIME_SECS = "total_time_secs";
        public static final String DOWNLOAD_LINK = "download_link";
        public static final String YEAR_PUBLISHED = "year_published";
    }
}
