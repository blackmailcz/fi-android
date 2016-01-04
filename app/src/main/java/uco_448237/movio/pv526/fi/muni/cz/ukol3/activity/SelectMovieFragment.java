package uco_448237.movio.pv526.fi.muni.cz.ukol3.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.Switch;

import java.util.ArrayList;

import uco_448237.movio.pv526.fi.muni.cz.ukol3.BuildConfig;
import uco_448237.movio.pv526.fi.muni.cz.ukol3.R;
import uco_448237.movio.pv526.fi.muni.cz.ukol3.adapter.MovieGridViewAdapter;
import uco_448237.movio.pv526.fi.muni.cz.ukol3.model.Movie;
import uco_448237.movio.pv526.fi.muni.cz.ukol3.model.MovieSection;
import uco_448237.movio.pv526.fi.muni.cz.ukol3.singleton.Singleton;

/**
 * Created by BlackMail on 26.10.2015.
 */
public class SelectMovieFragment extends Fragment implements Switch.OnCheckedChangeListener {

    // Tag
    public static final String TAG = SelectMovieFragment.class.getSimpleName();

    // Data
    private ArrayList<MovieSection> movieSections;

    // Adapter (to be refreshed from outside)
    public MovieGridViewAdapter movieGridViewAdapter;

    // Switch
    Switch sourceSwitch;

    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get movie list
        if (getArguments() != null) {
            movieSections = getArguments().getParcelableArrayList("movie_sections");
        }
        if (BuildConfig.logging) {
            Log.w(SelectMovieFragment.TAG, "Fragment onCreate is called");
        }
    }

    public static SelectMovieFragment getInstance (Bundle data) {
        SelectMovieFragment fragment = new SelectMovieFragment();
        fragment.setArguments(data);
        return fragment;
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        View rootView = inflater.inflate(R.layout.select_movie, container, false);
        // Switch
        sourceSwitch = (Switch) rootView.findViewById(R.id.discover_switch);
        sourceSwitch.setOnCheckedChangeListener(this);
        // GridView
        movieGridViewAdapter = new MovieGridViewAdapter(getActivity(), movieSections);
        GridView gridView = (GridView) rootView.findViewById(R.id.movies_gridview);
        gridView.setAdapter(movieGridViewAdapter);
        gridView.setEmptyView(rootView.findViewById(R.id.movies_gridview_empty));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentManager fm = getFragmentManager();
                Bundle args = new Bundle();
                // We have to pass the data using parcelable, so we parcel a list item
                if (movieSections != null) {
                    if (BuildConfig.logging) {
                        Log.w("tag", "## Packing parcel...");
                    }
                    args.putParcelable("selected_movie", (Movie) movieGridViewAdapter.getItem(position));
                }
                MovieDetailFragment movieDetailFragment = MovieDetailFragment.getInstance(args);
                // Bind the layout to the new fragment
                FragmentTransaction ft = fm.beginTransaction();
                if (Singleton.getInstance().isTablet(getActivity())) {
                    // Tablet: Replace the Tabletdetailscontainer with new fragment of the same type
                    ft.replace(R.id.tabletdetailscontainer, movieDetailFragment);
                } else {
                    // Phone: Start new activity
                    Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
                    intent.putExtras(args);
                    startActivity(intent);
                }
                ft.commit();
            }
        });
        return rootView;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        // Change text
        if (sourceSwitch.isChecked()) {
            sourceSwitch.setText(R.string.favorites);
        } else {
            sourceSwitch.setText(R.string.discover);
        }
        // Try to call listener in main activity, if it is implemented. If not, pass.
        try {
            ((OnSwitchChangeCustomListener) getActivity()).OnSwitchChanged(!sourceSwitch.isChecked());
        } catch (ClassCastException e) {
            // pass
        }
    }

    public interface OnSwitchChangeCustomListener {
        void OnSwitchChanged(boolean fromNetwork);
    }
}
