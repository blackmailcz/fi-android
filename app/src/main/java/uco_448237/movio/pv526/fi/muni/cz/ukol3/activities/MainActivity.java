package uco_448237.movio.pv526.fi.muni.cz.ukol3.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;

import uco_448237.movio.pv526.fi.muni.cz.ukol3.R;
import uco_448237.movio.pv526.fi.muni.cz.ukol3.models.Movie;
import uco_448237.movio.pv526.fi.muni.cz.ukol3.models.MovieSection;
import uco_448237.movio.pv526.fi.muni.cz.ukol3.singleton.Singleton;

public class MainActivity extends AppCompatActivity {

    private ArrayList<MovieSection> movieSections;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // If the activity is created for the first time, create data as well
        // If not, load previously created data from parcel
        if (savedInstanceState == null) {
            // Create new section list
            movieSections = new ArrayList<>();
            // Create new lists
            ArrayList<Movie> movieList1 = new ArrayList<>();
            // Add data (Will be sorted out in another class, but this is just an example)
            movieList1.add(new Movie(1, R.drawable.m1, "Jurassic World"));
            movieList1.add(new Movie(1, R.drawable.m2, "Big Six"));
            movieList1.add(new Movie(1, R.drawable.m3, "Pixels"));
            movieSections.add(new MovieSection("Some movies",movieList1));
            ArrayList<Movie> movieList2 = new ArrayList<>();
            movieList2.add(new Movie(1, R.drawable.m4, "Mad Max"));
            movieList2.add(new Movie(1, R.drawable.m5, "San Andreas"));
            movieList2.add(new Movie(1, R.drawable.m6, "Terminator: Genesis"));
            movieSections.add(new MovieSection("Other movies",movieList2));
        } else {
            // Get data from parcel.
            movieSections = savedInstanceState.getParcelableArrayList("movie_sections");
        }

        // Determine which fragment is currently displayed.
        FragmentManager fm = getFragmentManager();
        Fragment leftFragment = fm.findFragmentById(R.id.listcontainer);
        Fragment rightFragment = fm.findFragmentById(R.id.tabletdetailscontainer);

        // Create fragment if it does not exist
        if (leftFragment == null) {
            // Create bundle with movie list data
            Bundle args = new Bundle();
            // Parcel the list we created or restored
            args.putParcelableArrayList("movie_sections",movieSections);
            // Pass it to the fragment
            SelectMovieFragment selectMovieFragment = SelectMovieFragment.getInstance(args);
            // Bind fragment to view
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.listcontainer, selectMovieFragment, SelectMovieFragment.TAG);
            ft.commit();
            // Log to make sure the fragment is created only once
            Log.w(SelectMovieFragment.TAG, "Fragment is created");
        }
        // If we are using tablet...
        if (Singleton.getInstance().isTablet(this)) {
            // Create empty movie detail
            if (rightFragment == null) {
                FragmentTransaction ft = fm.beginTransaction();
                ft.add(R.id.tabletdetailscontainer, new MovieDetailFragment(), MovieDetailFragment.TAG);
                ft.commit();
            }
            // Also, enable the view
            FrameLayout rightFragmentContainer = (FrameLayout) findViewById(R.id.tabletdetailscontainer);
            rightFragmentContainer.setVisibility(View.VISIBLE);
            Log.e("??","SETTING VISIBLE");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState (Bundle outState) {
        // Save the data to a bundle when the activity dies
        outState.putParcelableArrayList("movie_sections",movieSections);
        super.onSaveInstanceState(outState);
    }

}
