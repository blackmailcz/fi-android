package uco_448237.movio.pv526.fi.muni.cz.ukol3.singleton;

import android.content.Context;
import android.content.res.Configuration;

import java.util.ArrayList;
import java.util.List;

import uco_448237.movio.pv526.fi.muni.cz.ukol3.model.MovieSection;

/**
 * Created by BlackMail on 19.10.2015.
 */
public class Singleton {

    // API KEY
    public static final String API_KEY = "7a1fbc5926b4a9aec175ac6bf651d4b6";

    // Instance data
    private static Singleton instance = null;
    private static ArrayList<MovieSection> movieData = new ArrayList<>();

    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }

    private Singleton() {
    }

    public static ArrayList<MovieSection> getMovieData() {
        return Singleton.movieData;
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
