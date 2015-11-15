package uco_448237.movio.pv526.fi.muni.cz.ukol3.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by BlackMail on 28.10.2015.
 */
public class MovieSection implements Parcelable {

    private ArrayList<Movie> movies;
    private String sectionName;

    public MovieSection(String sectionName, ArrayList<Movie> movies) {
        this.movies = movies;
        this.sectionName = sectionName;
    }

    protected MovieSection(Parcel in) {
        movies = in.createTypedArrayList(Movie.CREATOR);
        sectionName = in.readString();
    }

    public String getSectionName() {
        return sectionName;
    }

    public ArrayList<Movie> getMovies() {
        return movies;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(movies);
        dest.writeString(sectionName);
    }

    public static final Creator<MovieSection> CREATOR = new Creator<MovieSection>() {
        @Override
        public MovieSection createFromParcel(Parcel in) {
            return new MovieSection(in);
        }

        @Override
        public MovieSection[] newArray(int size) {
            return new MovieSection[size];
        }
    };
}
