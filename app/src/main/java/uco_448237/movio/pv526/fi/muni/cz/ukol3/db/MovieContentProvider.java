package uco_448237.movio.pv526.fi.muni.cz.ukol3.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Arrays;

/**
 * Created by BlackMail on 3.1.2016.
 */
public class MovieContentProvider extends ContentProvider {

    private static final String TAG = MovieContentProvider.class.getSimpleName();

    private static final int WORK_TIME = 100;
    private static final int WORK_TIME_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private DBHelper mOpenHelper;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieProviderWrapper.CONTENT_AUTHORITY;

        matcher.addURI(authority, MovieProviderWrapper.PATH_WORK_TIME, WORK_TIME);
        matcher.addURI(authority, MovieProviderWrapper.PATH_WORK_TIME + "/#", WORK_TIME_ID);

        return matcher;
    }


    @Override
    public boolean onCreate() {
        mOpenHelper = new DBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case WORK_TIME_ID: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieProviderWrapper.MovieEntityContants.TABLE_NAME,
                        projection,
                        MovieProviderWrapper.MovieEntityContants._ID + " = '" + ContentUris.parseId(uri) + "'",
                        null,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case WORK_TIME: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieProviderWrapper.MovieEntityContants.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case WORK_TIME:
                return MovieProviderWrapper.MovieEntityContants.CONTENT_TYPE;
            case WORK_TIME_ID:
                return MovieProviderWrapper.MovieEntityContants.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case WORK_TIME: {
                long _id = db.insert(MovieProviderWrapper.MovieEntityContants.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = MovieProviderWrapper.MovieEntityContants.buildMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        switch (match) {
            case WORK_TIME:
                rowsDeleted = db.delete(MovieProviderWrapper.MovieEntityContants.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (selection == null || rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case WORK_TIME:
                rowsUpdated = db.update(MovieProviderWrapper.MovieEntityContants.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
