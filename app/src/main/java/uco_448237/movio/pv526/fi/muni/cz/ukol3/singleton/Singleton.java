package uco_448237.movio.pv526.fi.muni.cz.ukol3.singleton;

import android.content.Context;
import android.content.res.Configuration;

/**
 * Created by BlackMail on 19.10.2015.
 */
public class Singleton {
    // Instance data
    private static Singleton instance = null;

    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }

    private Singleton() {
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
