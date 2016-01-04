package uco_448237.movio.pv526.fi.muni.cz.ukol3.sync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;

import uco_448237.movio.pv526.fi.muni.cz.ukol3.db.MovieContentManager;
import uco_448237.movio.pv526.fi.muni.cz.ukol3.model.Movie;

/**
 * Created by BlackMail on 4.1.2016.
 */
public class SyncUpdateReceiver extends BroadcastReceiver {

    public static final String ACTION_RESP = "uco_448237.movio.pv526.fi.muni.cz.ukol3.intent.action.MESSAGE_PROCESSED";


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.w("SYNC","RECEIVER GOT DATA");
        boolean status = intent.getExtras().getBoolean("is_ok", false);
        if (status) {
            // Get data
            ArrayList<Movie> updatedMovies = intent.getParcelableArrayListExtra("updated_movies");
            if (updatedMovies != null) {
                MovieContentManager manager = new MovieContentManager(context);
                for (Movie movie : updatedMovies) {
                    // Update
                    manager.updateMovie(movie);
                }
            }
        }
        // End
        context.unregisterReceiver(this);
        Log.w("SYNC","RECEIVER UNREGISTERED");
    }

}
