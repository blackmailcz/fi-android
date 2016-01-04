package uco_448237.movio.pv526.fi.muni.cz.ukol3.db;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import uco_448237.movio.pv526.fi.muni.cz.ukol3.model.Movie;

/**
 * Created by BlackMail on 3.1.2016.
 */
public class MovieContentManager {

    public static final int COL_RECORD_ID = 0;
    public static final int COL_MOVIE_ID = 1;
    public static final int COL_NAME = 2;
    public static final int COL_RELEASE_DATE = 3;
    public static final int COL_COVER_PATH = 4;

    private static final String WHERE_MOVIE_ID = MovieProviderWrapper.MovieEntityContants.MOVIE_ID + " = ?";

    private Context mContext;

    public MovieContentManager(Context context) {
        mContext = context.getApplicationContext();
    }

    public ContentValues prepareMovieValues(Movie entity) {
        if (entity == null) {
            return null;
        }
        ContentValues values = new ContentValues();
        values.put(MovieProviderWrapper.MovieEntityContants.MOVIE_ID, entity.getMovieId());
        values.put(MovieProviderWrapper.MovieEntityContants.MOVIE_NAME, entity.getTitle());
        values.put(MovieProviderWrapper.MovieEntityContants.MOVIE_RELEASE_DATE, entity.getReleaseDate());
        values.put(MovieProviderWrapper.MovieEntityContants.MOVIE_COVER_PATH, entity.getCoverPath());
        return values;
    }

    public void createMovie(Movie movie) {
        // Assuming all is OK ...
        movie.setRecordId(ContentUris.parseId(mContext.getContentResolver().insert(MovieProviderWrapper.MovieEntityContants.CONTENT_URI, prepareMovieValues(movie))));
    }

    public void deleteMovie(Movie movie) {
        if (movie == null) {
            throw new NullPointerException("movie == null");
        }
        if (movie.getRecordId() == null) {
            throw new IllegalStateException("movie id cannot be null");
        }
        mContext.getContentResolver().delete(MovieProviderWrapper.MovieEntityContants.CONTENT_URI, WHERE_MOVIE_ID, new String[]{String.valueOf(movie.getMovieId())});
    }

    public void updateMovie(Movie movie) {
        if (movie.getRecordId() == null) {
            throw new IllegalArgumentException("ID cant NULL");
        }
        mContext.getContentResolver().update(MovieProviderWrapper.MovieEntityContants.CONTENT_URI, prepareMovieValues(movie), WHERE_MOVIE_ID, new String[]{String.valueOf(movie.getMovieId())});
    }

    private Movie getMovie(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        Movie movie = new Movie(cursor.getString(COL_RELEASE_DATE),
                cursor.getString(COL_COVER_PATH),
                cursor.getString(COL_NAME));
        movie.setMovieId(cursor.getInt(COL_MOVIE_ID));
        movie.setRecordId(cursor.getLong(COL_RECORD_ID));
        return movie;
    }

    public Movie getMovieByMovieId(int id) {
        Cursor cursor = mContext.getContentResolver().query(MovieProviderWrapper.MovieEntityContants.CONTENT_URI, MovieProviderWrapper.MovieEntityContants.COLUMNS,
                WHERE_MOVIE_ID, new String[]{String.valueOf(id)}, null);
        if (cursor != null && cursor.moveToFirst()) {
            return getMovie(cursor);
        }
        return null;
    }

    public List<Movie> getAllMovies() {
        Cursor cursor = mContext.getContentResolver().query(MovieProviderWrapper.MovieEntityContants.CONTENT_URI, MovieProviderWrapper.MovieEntityContants.COLUMNS, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            List<Movie> movies = new ArrayList<>(cursor.getCount());
            try {
                while (!cursor.isAfterLast()) {
                    movies.add(getMovie(cursor));
                    cursor.moveToNext();
                }
            } finally {
                cursor.close();
            }
            return movies;
        }
        return Collections.emptyList();
    }


}
