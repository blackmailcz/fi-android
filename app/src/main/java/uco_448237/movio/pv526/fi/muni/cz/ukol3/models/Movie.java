package uco_448237.movio.pv526.fi.muni.cz.ukol3.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by BlackMail on 12.10.2015.
 */
public class Movie implements Parcelable {

    long releaseDate;
    int coverPath;
    String title;

    public Movie(long releaseDate, int coverPath, String title) {
        this.releaseDate = releaseDate;
        this.coverPath = coverPath;
        this.title = title;
    }

    public Movie(Parcel in) {
        releaseDate = in.readLong();
        coverPath = in.readInt();
        title = in.readString();
    }

    public long getReleaseDate() {
        return releaseDate;
    }

    public int getCoverPath() {
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
        dest.writeLong(releaseDate);
        dest.writeInt(coverPath);
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
