package uco_448237.movio.pv526.fi.muni.cz.ukol3.networking;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import java.util.ArrayList;

import uco_448237.movio.pv526.fi.muni.cz.ukol3.R;
import uco_448237.movio.pv526.fi.muni.cz.ukol3.activity.MainActivity;
import uco_448237.movio.pv526.fi.muni.cz.ukol3.model.MovieSection;
import uco_448237.movio.pv526.fi.muni.cz.ukol3.singleton.Singleton;

/**
 * Created by BlackMail on 3.1.2016.
 */
public class DownloadService extends IntentService {

    private static final String TAG = "download_service";
    private RetrofitRestClient restClient;


    public DownloadService() {
        super(DownloadService.class.getName());
        restClient = new RetrofitRestClient();
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d(TAG, "Service started");

        final ResultReceiver receiver = intent.getParcelableExtra("receiver");

        Bundle bundle = new Bundle();

        boolean isOk = true;
        ArrayList<MovieSection> downloadedSections = new ArrayList<>();

        try {
            MovieSection nowPlaying = restClient.getApiService().getNowPlaying(Singleton.API_KEY, "avg_rating.desc");
            nowPlaying.setSectionName(getString(R.string.now_playing_in_cinemas));
            downloadedSections.add(nowPlaying);

            MovieSection upcoming = restClient.getApiService().getUpcoming(Singleton.API_KEY, "primary_release_date.asc");
            upcoming.setSectionName(getString(R.string.upcoming_movies));
            downloadedSections.add(upcoming);
        } catch (Exception e) {
            isOk = false;
        }

        // Send results
        Intent outIntent = new Intent();
        outIntent.setAction(MainActivity.DownloadResponseReceiver.ACTION_RESP);
        outIntent.addCategory(Intent.CATEGORY_DEFAULT);
        outIntent.putExtra("is_ok", isOk);
        outIntent.putParcelableArrayListExtra("downloaded_sections", downloadedSections);
        sendBroadcast(outIntent);

    }


}
