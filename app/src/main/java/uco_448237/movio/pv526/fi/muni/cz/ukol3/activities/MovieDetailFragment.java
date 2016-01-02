package uco_448237.movio.pv526.fi.muni.cz.ukol3.activities;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import uco_448237.movio.pv526.fi.muni.cz.ukol3.BuildConfig;
import uco_448237.movio.pv526.fi.muni.cz.ukol3.R;
import uco_448237.movio.pv526.fi.muni.cz.ukol3.models.Movie;
import uco_448237.movio.pv526.fi.muni.cz.ukol3.singleton.Singleton;

/**
 * Created by BlackMail on 26.10.2015.
 */
public class MovieDetailFragment extends Fragment {

    // Data
    private Movie selectedMovie;

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
            // No movie selected - display some default screen
            rootView = inflater.inflate(R.layout.no_movie_selected, container, false);
        } else {
            rootView = inflater.inflate(R.layout.movie_detail, container, false);
            TextView releaseDateTV = (TextView) rootView.findViewById(R.id.details_release_date);
            releaseDateTV.setText(Long.toString(selectedMovie.getReleaseDate()));
            TextView nameTV = (TextView) rootView.findViewById(R.id.details_name);
            nameTV.setText(selectedMovie.getTitle());
            ImageView coverIV = (ImageView) rootView.findViewById(R.id.details_cover);
            coverIV.setImageResource(selectedMovie.getCoverPath());
        }
        return rootView;
    }

    public static MovieDetailFragment getInstance (Bundle data) {
        MovieDetailFragment fragment = new MovieDetailFragment();
        fragment.setArguments(data);
        return fragment;
    }
}
