package uco_448237.movio.pv526.fi.muni.cz.ukol3.singleton;

import android.content.Context;
import android.content.res.Configuration;

import java.util.ArrayList;

import uco_448237.movio.pv526.fi.muni.cz.ukol3.R;
import uco_448237.movio.pv526.fi.muni.cz.ukol3.model.MovieSection;

/**
 * Created by BlackMail on 19.10.2015.
 */
public class Singleton {

    // API KEY
    public static final String API_KEY = "7a1fbc5926b4a9aec175ac6bf651d4b6";

    // Instance data
    private static Singleton instance = null;
    private static ArrayList<MovieSection> displayedData = new ArrayList<>();
    private static ArrayList<MovieSection> networkMovieData = new ArrayList<>();
    private static ArrayList<MovieSection> dbMovieData = new ArrayList<>();

    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }

    private Singleton() {
    }

    public static ArrayList<MovieSection> getDisplayedData() {
        return displayedData;
    }

    public static ArrayList<MovieSection> getNetworkMovieData() {
        return Singleton.networkMovieData;
    }

    public static ArrayList<MovieSection> getDbMovieData() {
        return dbMovieData;
    }

    public static void setDisplayedAsDb() {
        Singleton.getDisplayedData().clear();
        Singleton.getDisplayedData().addAll(Singleton.getDbMovieData());
    }

    public static void setDisplayedAsNetwork() {
        Singleton.getDisplayedData().clear();
        Singleton.getDisplayedData().addAll(Singleton.getNetworkMovieData());
    }

    public boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }


    /*// Test method
    public boolean isTablet(Context context) {
        return !_isTablet(context);
    }*/

}
