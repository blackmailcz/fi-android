package uco_448237.movio.pv526.fi.muni.cz.ukol3.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by BlackMail on 12.10.2015.
 */
public class Movie implements Parcelable {

    public static final String BASE_URL = "https://image.tmdb.org/t/p/w500";

    private Long recordId = (long) -1; // DB ID
    @SerializedName("id")
    private int movieId;
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

    public Movie(String releaseDate, String coverPath, String title, int movieId) {
        this.releaseDate = releaseDate;
        this.coverPath = coverPath;
        this.title = title;
        this.movieId = movieId;
    }

    public Movie(Parcel in) {
        recordId = in.readLong();
        movieId = in.readInt();
        releaseDate = in.readString();
        coverPath = in.readString();
        title = in.readString();
    }

    @Override
    public String toString() {
        return "Movie: "+getTitle()+" released "+getReleaseDate()+" [mid:"+getMovieId()+"] [dbid:"+getRecordId()+"]";
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int id) {
        this.movieId = id;
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
        if (recordId == null) {
            dest.writeLong(-1);
        } else {
            dest.writeLong(recordId);
        }
        dest.writeInt(movieId);
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
