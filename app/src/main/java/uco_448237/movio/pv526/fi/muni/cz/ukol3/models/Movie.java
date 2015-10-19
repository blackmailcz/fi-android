package uco_448237.movio.pv526.fi.muni.cz.ukol3.models;

/**
 * Created by BlackMail on 12.10.2015.
 */
public class Movie {

    long releaseDate;
    int coverPath;
    String title;

    public Movie(long releaseDate, int coverPath, String title) {
        this.releaseDate = releaseDate;
        this.coverPath = coverPath;
        this.title = title;
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
}
