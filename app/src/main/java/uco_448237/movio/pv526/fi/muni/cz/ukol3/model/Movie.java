package uco_448237.movio.pv526.fi.muni.cz.ukol3.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import uco_448237.movio.pv526.fi.muni.cz.ukol3.R;

/**
 * Created by BlackMail on 12.10.2015.
 */
public class Movie implements Parcelable {

    public static final String BASE_URL = "https://image.tmdb.org/t/p/w500";

    @SerializedName("release_date")
    private String releaseDate;
    @SerializedName("poster_path")
    private String coverPath;
    @SerializedName("title")
    private String title;

    public Movie(String releaseDate, String coverPath, String title) {
        this.releaseDate = releaseDate;
        this.coverPath = coverPath;
        this.title = title;
    }

    public Movie(Parcel in) {
        releaseDate = in.readString();
        coverPath = in.readString();
        title = in.readString();
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(releaseDate);
        dest.writeString(coverPath);
        dest.writeString(title);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {

        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }

    };
}
