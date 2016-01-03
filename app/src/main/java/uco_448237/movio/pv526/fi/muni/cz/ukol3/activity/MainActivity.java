package uco_448237.movio.pv526.fi.muni.cz.ukol3.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;

import uco_448237.movio.pv526.fi.muni.cz.ukol3.BuildConfig;
import uco_448237.movio.pv526.fi.muni.cz.ukol3.R;
import uco_448237.movio.pv526.fi.muni.cz.ukol3.model.MovieSection;
import uco_448237.movio.pv526.fi.muni.cz.ukol3.networking.ConnectionChecker;
import uco_448237.movio.pv526.fi.muni.cz.ukol3.networking.DownloadService;
import uco_448237.movio.pv526.fi.muni.cz.ukol3.singleton.Singleton;

public class MainActivity extends AppCompatActivity implements ConnectionCheckerDialog.ConnectionCheckerDialogListener {

    private ArrayList<MovieSection> movieSections;
    private DownloadResponseReceiver downloadResponseReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Register receiver
        IntentFilter filter = new IntentFilter(DownloadResponseReceiver.ACTION_RESP);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        downloadResponseReceiver = new DownloadResponseReceiver();
        registerReceiver(downloadResponseReceiver, filter);


        // If the activity is created for the first time, create data as well
        // If not, load previously created data from parcel
        if (savedInstanceState == null) {
            // Create new section list
            movieSections = Singleton.getMovieData();
            // Download data if possible
            checkAndDownload();
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
            if (BuildConfig.logging) {
                Log.w(SelectMovieFragment.TAG, "Fragment is created");
            }
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
            if (BuildConfig.logging) {
                Log.e("??", "SETTING VISIBLE");
            }
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

    @Override
    public void onStop() {
        try {
            unregisterReceiver(downloadResponseReceiver);
        } catch (IllegalArgumentException e) {
            // Receiver was already unregistered for some reason
        }
        super.onStop();
    }

    @Override
    public void onConnectionCheckerDialogYes() {
        checkAndDownload();
    }

    @Override
    public void onConnectionCheckerDialogNo() {
        // Pass
    }

    public void checkAndDownload() {
        // Check online state
        boolean connectionSuccessful = false;
        final boolean[] tryToReconnect = {false};
        do {
            if (ConnectionChecker.isOnline(this)) {
                connectionSuccessful = true;
                break;
            } else {
                Log.w("Connection test", "New attempt");
                new ConnectionCheckerDialog().show(getFragmentManager(), "connection_checker_dialog");
            }
        } while (tryToReconnect[0]);
        if (connectionSuccessful) {
            Intent intent = new Intent(Intent.ACTION_SYNC, null, this, DownloadService.class);
            startService(intent);
            /*// Start async task
            DownloadTask downloadTask = new DownloadTask();
            downloadTask.execute();*/
        }
    }

    public void notifyMovieAdapterUpdate() {
        Handler uiCallback = new Handler(Looper.getMainLooper());
        uiCallback.post(new Runnable() {
            @Override
            public void run() {
                FragmentManager fm = getFragmentManager();
                Fragment listFragment = fm.findFragmentById(R.id.listcontainer);
                if (listFragment != null) {
                    if (((SelectMovieFragment) listFragment).movieGridViewAdapter != null) {
                        ((SelectMovieFragment) listFragment).movieGridViewAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    public void showNotification (String header, String text) {
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.warning_icon)
                        .setContentTitle(header)
                        .setContentText(text)
                        .setContentIntent(contentIntent);
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(0, mBuilder.build());

    }

    public class DownloadResponseReceiver extends BroadcastReceiver {

        public static final String ACTION_RESP = "uco_448237.movio.pv526.fi.muni.cz.ukol3.intent.action.MESSAGE_PROCESSED";

        @Override
        public void onReceive(Context context, Intent intent) {
            // Get status
            boolean status = intent.getExtras().getBoolean("is_ok");
            if (!status) {
                Log.e("ERROR","Download failed");
                showNotification(getString(R.string.warning), getString(R.string.download_parsing_failed));
            } else {
                // Get data
                ArrayList<MovieSection> downloadedSections = intent.getParcelableArrayListExtra("downloaded_sections");
                for (MovieSection section : downloadedSections) {
                    Singleton.getMovieData().add(section);
                }
                // Notify
                showNotification(getString(R.string.info), getString(R.string.download_complete));
                // Update view
                notifyMovieAdapterUpdate();
            }
        }
    }


/*
    private class DownloadTask extends AsyncTask<Void, String, Integer> {

        private DownloadProgressDialog downloadDialog;
        private HttpClient httpClient;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Prepare download dialog
            downloadDialog = new DownloadProgressDialog();
            downloadDialog.show(getFragmentManager(), DownloadProgressDialog.TAG);
            // Prepare http client
            httpClient = new HttpClient();
        }

        @Override
        protected Integer doInBackground(Void... params) {
            try {
                processSection("https://api.themoviedb.org/3/movie/now_playing?api_key="+Singleton.API_KEY+"&sort_by=avg_rating.desc", R.string.now_playing_in_cinemas);
                processSection("https://api.themoviedb.org/3/movie/upcoming?api_key="+Singleton.API_KEY+"&sort_by=primary_release_date.asc", R.string.upcoming_movies);
                notifyMovieAdapterUpdate();
                publishProgress("Finished");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        private void processSection(String requestUrl, int sectionStringResource) {
            publishProgress(getString(R.string.downloading_data) + ": " + getString(sectionStringResource));
            try {
                String response = httpClient.runRequest(requestUrl); // Run request
                publishProgress(getString(R.string.processing_data));
                MovieSection movieSection = new GsonBuilder().create().fromJson(response, MovieSection.class);
                movieSection.setSectionName(getString(sectionStringResource));
                publishProgress(getString(R.string.downloading_images) + ": " + getString(sectionStringResource));
                for (Movie m : movieSection.getMovies()) {
                    // Preload images
                    Picasso.with(getApplicationContext()).load(m.getCoverPath()).fetch();
                }
                movieSections.add(movieSection);
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Could not download the section: "+getString(sectionStringResource),Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            downloadDialog.getDialog().setTitle(values[0]);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            // Hide dialog
            downloadDialog.dismiss();
        }
    }
*/
}
