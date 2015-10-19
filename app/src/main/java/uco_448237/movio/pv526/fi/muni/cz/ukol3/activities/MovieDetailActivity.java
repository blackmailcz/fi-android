package uco_448237.movio.pv526.fi.muni.cz.ukol3.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import uco_448237.movio.pv526.fi.muni.cz.ukol3.R;
import uco_448237.movio.pv526.fi.muni.cz.ukol3.singleton.Singleton;

/**
 * Created by BlackMail on 19.10.2015.
 */
public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moviedetail);

        // Set data do display
        if (Singleton.getSelectedMovie() != null) {
            TextView releaseDateTV = (TextView) findViewById(R.id.details_release_date);
            releaseDateTV.setText(Long.toString(Singleton.getSelectedMovie().getReleaseDate()));
            TextView nameTV = (TextView) findViewById(R.id.details_name);
            nameTV.setText(Singleton.getSelectedMovie().getTitle());
            ImageView coverIV = (ImageView) findViewById(R.id.details_cover);
            coverIV.setImageResource(Singleton.getSelectedMovie().getCoverPath());
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
