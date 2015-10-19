package uco_448237.movio.pv526.fi.muni.cz.ukol3.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import uco_448237.movio.pv526.fi.muni.cz.ukol3.R;
import uco_448237.movio.pv526.fi.muni.cz.ukol3.adapters.MovieGridViewAdapter;
import uco_448237.movio.pv526.fi.muni.cz.ukol3.models.Movie;
import uco_448237.movio.pv526.fi.muni.cz.ukol3.singleton.Singleton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Singleton.getInstance(); // Init singleton

        final List<Movie> myMovieList = new ArrayList<>();
        MovieGridViewAdapter movieGridViewAdapter = new MovieGridViewAdapter(this, myMovieList);
        GridView gridView = (GridView) findViewById(R.id.movies_gridview);
        gridView.setAdapter(movieGridViewAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Select movie
                Singleton.setSelectedMovie(myMovieList.get(position));
                // Start new activity
                Intent intent = new Intent(MainActivity.this, MovieDetailActivity.class);
                startActivity(intent);
            }
        });

        // Add data
        myMovieList.add(new Movie(1, R.drawable.m1, "Jurassic World"));
        myMovieList.add(new Movie(1, R.drawable.m2, "Big Six"));
        myMovieList.add(new Movie(1, R.drawable.m3, "Pixels"));
        myMovieList.add(new Movie(1, R.drawable.m4, "Mad Max"));
        myMovieList.add(new Movie(1, R.drawable.m5, "San Andreas"));
        myMovieList.add(new Movie(1, R.drawable.m6, "Terminator: Genesis"));

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
}
