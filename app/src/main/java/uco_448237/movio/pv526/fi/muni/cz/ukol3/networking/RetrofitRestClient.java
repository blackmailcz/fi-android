package uco_448237.movio.pv526.fi.muni.cz.ukol3.networking;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by BlackMail on 3.1.2016.
 */
public class RetrofitRestClient {

    private static final String BASE_URL = "https://api.themoviedb.org/3";
    private IApiService apiService;

    public RetrofitRestClient() {
        Gson gson = new GsonBuilder().create();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(BASE_URL)
                .setConverter(new GsonConverter(gson))
                .build();
        apiService = restAdapter.create(IApiService.class);
    }

    public IApiService getApiService() {
        return apiService;
    }

}
