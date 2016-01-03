package uco_448237.movio.pv526.fi.muni.cz.ukol3.networking;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by BlackMail on 2.1.2016.
 */
public class HttpClient {

    private final OkHttpClient client = new OkHttpClient();

    public String runRequest(String url) throws IOException {
        if (url == null) {
            throw new NullPointerException("Request URL cannot be null");
        }
        // Create new request
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = this.client.newCall(request);
        Response response = call.execute();
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code: "+response);
        }
        return response.body().string();
    }

}
