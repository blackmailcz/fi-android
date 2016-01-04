package uco_448237.movio.pv526.fi.muni.cz.ukol3.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
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
import java.util.List;

import uco_448237.movio.pv526.fi.muni.cz.ukol3.BuildConfig;
import uco_448237.movio.pv526.fi.muni.cz.ukol3.R;
import uco_448237.movio.pv526.fi.muni.cz.ukol3.db.MovieAsyncTaskLoader;
import uco_448237.movio.pv526.fi.muni.cz.ukol3.model.Movie;
import uco_448237.movio.pv526.fi.muni.cz.ukol3.model.MovieSection;
import uco_448237.movio.pv526.fi.muni.cz.ukol3.networking.ConnectionChecker;
import uco_448237.movio.pv526.fi.muni.cz.ukol3.networking.DownloadService;
import uco_448237.movio.pv526.fi.muni.cz.ukol3.singleton.Singleton;
import uco_448237.movio.pv526.fi.muni.cz.ukol3.sync.UpdaterSyncAdapter;

public class MainActivity extends AppCompatActivity implements ConnectionCheckerDialog.ConnectionCheckerDialogListener,
        SelectMovieFragment.OnSwitchChangeCustomListener, LoaderManager.LoaderCallbacks<List<Movie>> {

    private ArrayList<MovieSection> movieSections;
    private DownloadResponseReceiver downloadResponseReceiver;
    private boolean displayFromNetwork = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Init sync adapter
        UpdaterSyncAdapter.initializeSyncAdapter(this);

        // Register receiver
        IntentFilter filter = new IntentFilter(DownloadResponseReceiver.ACTION_RESP);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        downloadResponseReceiver = new DownloadResponseReceiver();
        registerReceiver(downloadResponseReceiver, filter);

        // Create DB section if empty
        if (Singleton.getDbMovieData().isEmpty()) {
            MovieSection dbSection = new MovieSection(getString(R.string.favorite_movies));
            Singleton.getDbMovieData().add(dbSection);
        }

        // If the activity is created for the first time, create data as well
        // If not, load previously created data from parcel
        if (savedInstanceState == null) {
            // Create new section list
            movieSections = Singleton.getDisplayedData();
            // Download data if possible
            checkAndDownload();
        } else {
            // Get data from parcel.
            displayFromNetwork = savedInstanceState.getBoolean("display_from_network");
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
        outState.putBoolean("display_from_network", displayFromNetwork);
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
        Log.w("CHECK", "Check connection");
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
        }
    }

    public void notifyMovieAdapterUpdate() {
        Handler uiCallback = new Handler(Looper.getMainLooper());
        uiCallback.post(new Runnable() {
            @Override
            public void run() {
                try {
                    FragmentManager fm = getFragmentManager();
                    Fragment listFragment = fm.findFragmentById(R.id.listcontainer);
                    if (listFragment != null) {
                        if (((SelectMovieFragment) listFragment).movieGridViewAdapter != null) {
                            ((SelectMovieFragment) listFragment).movieGridViewAdapter.notifyDataSetChanged();
                        }
                    }
                } catch (ClassCastException e) {
                    // Pass
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

    @Override
    public void OnSwitchChanged(boolean fromNetwork) {
        // Set data source
        if (fromNetwork) {
            Singleton.getDisplayedData().clear();
            Log.e("!!", "Network");
            Singleton.getDisplayedData().addAll(Singleton.getNetworkMovieData());
            if (Singleton.getDisplayedData().isEmpty()) {
                checkAndDownload();
            }
            displayFromNetwork = true;
            // Notify change
            notifyMovieAdapterUpdate();
        } else {
            Log.e("!!", "DB");
            updateFavListFromDB();
        }
    }

    private void updateFavListFromDB() {
        // Call loader
        Bundle bundle = new Bundle();
        bundle.putInt("action", MovieAsyncTaskLoader.ACTION_LOADALL);
        bundle.putInt("movie_id", -1);
        Loader loader = getLoaderManager().initLoader(0, bundle, this);
        loader.forceLoad();
        displayFromNetwork = false;
        // Wait for response --> onLoadFinished
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        Log.w("LOADER", "loader created");
        return new MovieAsyncTaskLoader(getApplicationContext(), args);
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
        Log.w("LOADER", "loader " + loader.getId() + "finished");
        switch (loader.getId()) {
            case 0:
                // Mass DB loader
                // Add data when finished.
                Log.e("LOADER", "Loader finished");
                if (data == null) {
                    Log.w("LOADER", "Data loaded is null");
                } else {
                    // Erase old
                    Singleton.getDbMovieData().get(0).getMovies().clear();
                    // Add new
                    Singleton.getDbMovieData().get(0).addAllMovies(data);
                    // Make display
                    Singleton.getDisplayedData().clear();
                    Singleton.getDisplayedData().addAll(Singleton.getDbMovieData());
                    notifyMovieAdapterUpdate();
                }
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
        // ..
    }

    @Override
    protected void onResume() {
        // Refresh DB
        if (!displayFromNetwork) {
            updateFavListFromDB();
        }
        super.onResume();
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
                Singleton.getNetworkMovieData().clear();
                for (MovieSection section : downloadedSections) {
                    Singleton.getNetworkMovieData().add(section);
                }
                // Notify
                showNotification(getString(R.string.info), getString(R.string.download_complete));
                // Update display if network source is selected.
                if (displayFromNetwork) {
                    // Update data
                    OnSwitchChanged(true);
                }
            }
        }
    }

}
