package uco_448237.movio.pv526.fi.muni.cz.ukol3.db;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import uco_448237.movio.pv526.fi.muni.cz.ukol3.model.Movie;

/**
 * Created by BlackMail on 4.1.2016.
 */
public class MovieAsyncTaskLoader extends AsyncTaskLoader<List<Movie>> {

    public static final int ACTION_LOADONE = 1;
    public static final int ACTION_LOADALL = 2;
    public static final int ACTION_INSERT = 3;
    public static final int ACTION_DELETE = 4;

    private Context context;

    private int action = ACTION_LOADALL;
    private Movie movie = null;

    public MovieAsyncTaskLoader(Context context, Bundle bundle) {
        super(context);
        if (bundle.containsKey("action")) {
            action = bundle.getInt("action");
        }
        if (bundle.containsKey("movie")) {
            movie = bundle.getParcelable("movie");
        }
        this.context = context;
    }

    @Override
    public List<Movie> loadInBackground() {
        // Create new manager
        Log.w("LOADER", "loadInBackground");
        MovieContentManager manager = new MovieContentManager(context);
        switch (action) {
            case ACTION_LOADALL:
                return manager.getAllMovies();
            case ACTION_LOADONE:
                List<Movie> movies = new ArrayList<>();
                Movie loadedMovie = manager.getMovieByMovieId(movie.getMovieId());
                if (loadedMovie != null) {
                    movies.add(loadedMovie);
                }
                return movies;
            case ACTION_INSERT:
                List<Movie> movies_ = new ArrayList<>();
                manager.createMovie(movie);
                movies_.add(movie); // Updated ID
                return movies_;
            case ACTION_DELETE:
                manager.deleteMovie(movie);
                return null;
        }
        return null;
    }
}
