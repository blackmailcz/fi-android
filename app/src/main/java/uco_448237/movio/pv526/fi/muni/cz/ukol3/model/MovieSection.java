package uco_448237.movio.pv526.fi.muni.cz.ukol3.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Created by BlackMail on 28.10.2015.
 */
public class MovieSection implements Parcelable {

    @SerializedName("results")
    private ArrayList<Movie> movies = new ArrayList<>();
    private String sectionName;

    public MovieSection(String sectionName, ArrayList<Movie> movies) {
        this.movies = movies;
        this.sectionName = sectionName;
    }

    public MovieSection(String sectionName) {
        this.sectionName = sectionName;
    }

    protected MovieSection(Parcel in) {
        movies = in.createTypedArrayList(Movie.CREATOR);
        sectionName = in.readString();
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) { this.sectionName = sectionName; }

    public ArrayList<Movie> getMovies() {
        return movies;
    }

    public void addMovie(Movie movie) {
        this.movies.add(movie);
    }

    public void addAllMovies(Collection<Movie> movies) {
        this.movies.addAll(movies);
    }

    public void removeById(int id) {
        Iterator<Movie> iterator = movies.iterator();
        while (iterator.hasNext()) {
            Movie movie = iterator.next();
            if (movie.getMovieId() == id) {
                movies.remove(movie);
            }
        }
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
