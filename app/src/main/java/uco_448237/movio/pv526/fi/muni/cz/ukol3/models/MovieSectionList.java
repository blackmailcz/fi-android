package uco_448237.movio.pv526.fi.muni.cz.ukol3.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by BlackMail on 2.11.2015.
 */
public class MovieSectionList implements Parcelable {

    public ArrayList<MovieSection> sections;

    public MovieSectionList(ArrayList<MovieSection> sections) {
        this.sections = sections;
    }

    public Movie getMovieAt(int position) {
        int count = 0;
        for (MovieSection section : sections) {
            for (Movie movie : section.getMovies()) {
                if (position == count) {
                    return movie;
                }
                count++;
            }
        }
        return null;
    }

    protected MovieSectionList(Parcel in) {
        sections = in.createTypedArrayList(MovieSection.CREATOR);
    }

    public static final Creator<MovieSectionList> CREATOR = new Creator<MovieSectionList>() {
        @Override
        public MovieSectionList createFromParcel(Parcel in) {
            return new MovieSectionList(in);
        }

        @Override
        public MovieSectionList[] newArray(int size) {
            return new MovieSectionList[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(sections);
    }
}
