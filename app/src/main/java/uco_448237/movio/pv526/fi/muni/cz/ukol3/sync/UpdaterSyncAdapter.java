package uco_448237.movio.pv526.fi.muni.cz.ukol3.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.NotificationManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import uco_448237.movio.pv526.fi.muni.cz.ukol3.R;
import uco_448237.movio.pv526.fi.muni.cz.ukol3.db.MovieContentManager;
import uco_448237.movio.pv526.fi.muni.cz.ukol3.model.Movie;
import uco_448237.movio.pv526.fi.muni.cz.ukol3.networking.RetrofitRestClient;
import uco_448237.movio.pv526.fi.muni.cz.ukol3.singleton.Singleton;

/**
 * Created by BlackMail on 4.1.2016.
 */
public class UpdaterSyncAdapter extends AbstractThreadedSyncAdapter {

    // Interval at which to sync with the server, in seconds.
    public static final int SYNC_INTERVAL = 60 * 60 * 24; //day
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL / 3;

    private Context mContext;

    public UpdaterSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContext = context;
    }

    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder()
                    .syncPeriodic(syncInterval, flexTime)
                    .setSyncAdapter(account, authority)
                    .setExtras(Bundle.EMPTY) //enter non null Bundle, otherwise on some phones it crashes sync
                    .build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account, authority, Bundle.EMPTY, syncInterval);
        }
    }

    /**
     * Helper method to have the sync adapter sync immediately
     *
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context), context.getString(R.string.content_authority), bundle);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }

    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one if the
     * fake account doesn't exist yet.  If we make a new account, we call the onAccountCreated
     * method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if (null == accountManager.getPassword(newAccount)) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        UpdaterSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.w("SYNC", "RUNNING SYNC [new]");

        // DB API
        MovieContentManager manager = new MovieContentManager(mContext);
        // Pack movie IDs to synchronize
        List<Movie> movies = manager.getAllMovies();
        ArrayList<Integer> ids = new ArrayList<>();
        for (Movie movie : movies) {
            ids.add(movie.getMovieId());
        }

        boolean isOk = true;
        ArrayList<Movie> updatedMovies = new ArrayList<>();

        RetrofitRestClient restClient = new RetrofitRestClient();

        for (Integer id : ids) {
            try {
                Log.w("SYNC", "SYNCING MOVIE #"+id);
                Movie newMovie = restClient.getApiService().getMovieById(Singleton.API_KEY, id);
                Movie oldMovie = manager.getMovieByMovieId(id);
                if (!oldMovie.equals(newMovie)) {
                    // Movie will be updated
                    updatedMovies.add(newMovie);
                }
            } catch (Exception e) {
                Log.w("SYNC", "SYNC FAILED - EXCEPTION OCCURRED WHEN FETCHING MOVIE #"+id);
                isOk = false;
            }
        }
        // Sync only if no download failure occurs
        if (isOk) {
            for (Movie movie : updatedMovies) {
                // Update
                manager.updateMovie(movie);
            }
            Log.w("SYNC", "COMPLETE");
        } else {
            Log.w("SYNC", "FAILED");
        }

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mContext)
                        .setSmallIcon(R.drawable.warning_icon)
                        .setContentTitle(mContext.getString(R.string.sync))
                        .setContentText(mContext.getString(R.string.sync_done) + mContext.getString(R.string.sync_updated) + updatedMovies.size() + mContext.getString(R.string.sync_movies));
        NotificationManager nm = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(0, mBuilder.build());

    }
}