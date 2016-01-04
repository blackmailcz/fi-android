package uco_448237.movio.pv526.fi.muni.cz.ukol3.sync;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;

import uco_448237.movio.pv526.fi.muni.cz.ukol3.model.Movie;
import uco_448237.movio.pv526.fi.muni.cz.ukol3.networking.RetrofitRestClient;
import uco_448237.movio.pv526.fi.muni.cz.ukol3.singleton.Singleton;

/**
 * Created by BlackMail on 4.1.2016.
 */
public class SyncDownloadService extends IntentService {

    private RetrofitRestClient restClient;


    public SyncDownloadService() {
        super(SyncDownloadService.class.getName());
        restClient = new RetrofitRestClient();
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.w("SYNC", "Sync download service started");

        ArrayList<Integer> ids = intent.getIntegerArrayListExtra("ids");

        if (ids == null) {
            return; // Nothing to update
        }

        boolean isOk = true;
        ArrayList<Movie> updatedMovies = new ArrayList<>();

        for (Integer id : ids) {
            try {
                Log.w("SYNC", "SYNCING MOVIE #"+id);
                Movie movie = restClient.getApiService().getMovieById(Singleton.API_KEY, id);
                updatedMovies.add(movie);
            } catch (Exception e) {
                Log.w("SYNC", "SYNC FAILED - EXCEPTION OCCURED WHEN FETCHING MOVIE #"+id);
                isOk = false;
            }
        }

        // Send results
        Intent outIntent = new Intent();
        outIntent.setAction(SyncUpdateReceiver.ACTION_RESP);
        outIntent.addCategory(Intent.CATEGORY_DEFAULT);
        outIntent.putExtra("is_ok", isOk);
        outIntent.putParcelableArrayListExtra("updated_movies", updatedMovies);
        sendBroadcast(outIntent);
    }
}
