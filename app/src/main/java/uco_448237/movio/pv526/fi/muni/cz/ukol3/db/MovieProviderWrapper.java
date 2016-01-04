package uco_448237.movio.pv526.fi.muni.cz.ukol3.db;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by BlackMail on 3.1.2016.
 */
public class MovieProviderWrapper {

    // API NAME
    public static final String CONTENT_AUTHORITY = "cz.blackmail.example.app";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    // API ENTITY
    public static final String PATH_WORK_TIME = "movie";

    public static final class MovieEntityContants implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_WORK_TIME).build();

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_WORK_TIME;
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_WORK_TIME;

        public static final String TABLE_NAME = "movies";

        public static final String MOVIE_ID = "id";
        public static final String MOVIE_RELEASE_DATE = "release_date";
        public static final String MOVIE_NAME = "name";
        public static final String MOVIE_COVER_PATH = "cover_path";

        public static final String[] COLUMNS = { _ID, MOVIE_ID, MOVIE_NAME, MOVIE_RELEASE_DATE, MOVIE_COVER_PATH };

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" ("+_ID+" INTEGER PRIMARY KEY, " +MOVIE_ID+" INTEGER, "+
                MOVIE_NAME+" TEXT, "+MOVIE_RELEASE_DATE+" TEXT, "+MOVIE_COVER_PATH+" TEXT)";

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

}
