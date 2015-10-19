package uco_448237.movio.pv526.fi.muni.cz.ukol3;

import uco_448237.movio.pv526.fi.muni.cz.ukol3.models.Movie;

/**
 * Created by BlackMail on 19.10.2015.
 */
public class Singleton {
    // Instance data
    private static Singleton instance = null;

    // Data
    private static Movie selectedMovie = null;

    public static Movie getSelectedMovie() {
        return selectedMovie;
    }

    public static void setSelectedMovie(Movie selectedMovie) {
        Singleton.selectedMovie = selectedMovie;
    }

    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }
}
