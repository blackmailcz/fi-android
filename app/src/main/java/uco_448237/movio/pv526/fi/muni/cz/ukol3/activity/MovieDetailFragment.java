package uco_448237.movio.pv526.fi.muni.cz.ukol3.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.LoaderManager;
import android.content.Loader;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import uco_448237.movio.pv526.fi.muni.cz.ukol3.BuildConfig;
import uco_448237.movio.pv526.fi.muni.cz.ukol3.R;
import uco_448237.movio.pv526.fi.muni.cz.ukol3.db.MovieAsyncTaskLoader;
import uco_448237.movio.pv526.fi.muni.cz.ukol3.model.Movie;

/**
 * Created by BlackMail on 26.10.2015.
 */
public class MovieDetailFragment extends Fragment implements Button.OnClickListener, LoaderManager.LoaderCallbacks<List<Movie>> {

    // Data
    private Movie selectedMovie;

    // Button
    private FloatingActionButton addOrRemoveButton;

    // BG
    private RelativeLayout background;

    // Tag
    public static final String TAG = MovieDetailFragment.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get selected movie
        if (getArguments() != null) {
            selectedMovie = getArguments().getParcelable("selected_movie");
        }
        if (BuildConfig.logging) {
            Log.w(MovieDetailFragment.TAG, "Fragment onCreate is called");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        View rootView;
        if (selectedMovie == null) {
            // No movie selected - display some default screen [SAFE CHECK]
            rootView = inflater.inflate(R.layout.no_movie_selected, container, false);
        } else {
            rootView = inflater.inflate(R.layout.movie_detail, container, false);
            // BG
            background = (RelativeLayout) rootView.findViewById(R.id.movie_detail_bg);
            // Button
            addOrRemoveButton = (FloatingActionButton) rootView.findViewById(R.id.button_add);
            addOrRemoveButton.setOnClickListener(this);
            // Check status upon creating
            checkFavoriteButtonImage();
            TextView releaseDateTV = (TextView) rootView.findViewById(R.id.details_release_date);
            releaseDateTV.setText(selectedMovie.getReleaseDate());
            TextView nameTV = (TextView) rootView.findViewById(R.id.details_name);
            nameTV.setText(selectedMovie.getTitle());
            ImageView coverIV = (ImageView) rootView.findViewById(R.id.details_cover);
            Picasso.with(getActivity()).load(Movie.BASE_URL + selectedMovie.getCoverPath()).placeholder(R.drawable.image_not_available).into(coverIV);
        }
        return rootView;
    }

    public static MovieDetailFragment getInstance (Bundle data) {
        MovieDetailFragment fragment = new MovieDetailFragment();
        fragment.setArguments(data);
        return fragment;
    }

    public void setFavoriteMovieState(boolean isPositive) {
        int buttonIcon;
        int bgColor;
        if (isPositive) {
            buttonIcon = R.drawable.plus_icon;
            bgColor = Color.WHITE;
        } else {
            buttonIcon = R.drawable.cross_icon;
            bgColor = Color.YELLOW;
        }
        if (addOrRemoveButton != null) {
            addOrRemoveButton.setImageResource(buttonIcon);
            background.setBackgroundColor(bgColor);
        }

    }

    @Override
    public void onClick(View v) {
        Log.w("FAV", "Button pressed - listener reaction");
        // Determine DB status.
        Bundle bundle = new Bundle();
        bundle.putInt("action",MovieAsyncTaskLoader.ACTION_LOADONE);
        bundle.putParcelable("movie", selectedMovie);
        Loader loader = getLoaderManager().initLoader(1, bundle, this);
        loader.forceLoad();
    }

    private void checkFavoriteButtonImage() {
        Log.w("FAV", "Button pressed - listener reaction");
        // Determine DB status.
        Bundle bundle = new Bundle();
        bundle.putInt("action",MovieAsyncTaskLoader.ACTION_LOADONE);
        bundle.putParcelable("movie", selectedMovie);
        Loader loader = getLoaderManager().initLoader(4, bundle, this);
        loader.forceLoad();
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        Log.w("LOADER", "loader created");
        return new MovieAsyncTaskLoader(getActivity(), args);
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
        Log.w("LOADER", "loader "+loader.getId()+"finished");
        switch (loader.getId()) {
            case 1:
                // Check + insert or delete movie
                boolean movieExists;
                Movie movie;
                if (data == null || data.isEmpty()) {
                    // Movie does not exist --> insert
                    addMovieToFavorites(selectedMovie);
                    movieExists = false;
                } else {
                    // Movie exists -> delete
                    deleteMovieFromFavorites(selectedMovie);
                    movieExists = true;
                }
                // Notify icon change
                notifyChangeFavoriteButtonImage(movieExists);
                break;
            case 4:
                // Check for status only
                // Check + insert or delete movie
                if (data == null || data.isEmpty()) {
                    // Movie does not exist --> insert
                    movieExists = false;
                } else {
                    // Movie exists -> delete
                    movieExists = true;
                }
                // Notify icon change
                notifyChangeFavoriteButtonImage(!movieExists);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {

    }

    public void addMovieToFavorites(Movie movie) {
        Bundle bundle = new Bundle();
        bundle.putInt("action",MovieAsyncTaskLoader.ACTION_INSERT);
        bundle.putParcelable("movie", movie);
        Loader loader = getLoaderManager().initLoader(2, bundle, this);
        loader.forceLoad();
    }
    public void deleteMovieFromFavorites(Movie movie) {
        Bundle bundle = new Bundle();
        bundle.putInt("action",MovieAsyncTaskLoader.ACTION_DELETE);
        bundle.putParcelable("movie", movie);
        Loader loader = getLoaderManager().initLoader(3, bundle, this);
        loader.forceLoad();
    }

    public void notifyChangeFavoriteButtonImage(final boolean isPositive) {
        Handler uiCallback = new Handler(Looper.getMainLooper());
        uiCallback.post(new Runnable() {
            @Override
            public void run() {
                setFavoriteMovieState(isPositive);
            }
        });
    }

    public void notifyMovieAdapterUpdate() {
        Handler uiCallback = new Handler(Looper.getMainLooper());
        uiCallback.post(new Runnable() {
            @Override
            public void run() {
                try {
                    FragmentManager fm = getFragmentManager();
                    Fragment listFragment = fm.findFragmentByTag(SelectMovieFragment.TAG);
                    if (listFragment != null) {
                        if (((SelectMovieFragment) listFragment).movieGridViewAdapter != null) {
                            ((SelectMovieFragment) listFragment).movieGridViewAdapter.notifyDataSetChanged();
                        }
                    }
                } catch (ClassCastException e) {
                    // Pass
                }
            }
        });
    }
}
