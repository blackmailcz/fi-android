package uco_448237.movio.pv526.fi.muni.cz.ukol3.networking;

import retrofit.http.GET;
import retrofit.http.Query;
import uco_448237.movio.pv526.fi.muni.cz.ukol3.model.MovieSection;

/**
 * Created by BlackMail on 3.1.2016.
 */
public interface IApiService {

    @GET("/movie/now_playing")
    public MovieSection getNowPlaying(@Query("api_key") String apiKey, @Query("sort_by") String sortBy);

    @GET("/movie/upcoming")
    public MovieSection getUpcoming(@Query("api_key") String apiKey, @Query("sort_by") String sortBy);

}
