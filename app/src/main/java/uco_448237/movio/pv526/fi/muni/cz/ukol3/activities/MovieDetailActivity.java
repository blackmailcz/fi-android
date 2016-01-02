package uco_448237.movio.pv526.fi.muni.cz.ukol3.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import uco_448237.movio.pv526.fi.muni.cz.ukol3.BuildConfig;
import uco_448237.movio.pv526.fi.muni.cz.ukol3.R;
import uco_448237.movio.pv526.fi.muni.cz.ukol3.singleton.Singleton;

public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        if (BuildConfig.logging) {
            Log.w("tag", "STARTING ACTIVITY");
        }
        Bundle bundle = getIntent().getExtras();

        FragmentManager fm = getFragmentManager();
        Fragment detailFragment = fm.findFragmentById(R.id.movie_detail_container);
        // Create empty movie detail
        if (detailFragment == null) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.movie_detail_container, MovieDetailFragment.getInstance(bundle), MovieDetailFragment.TAG);
            ft.commit();
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

}
