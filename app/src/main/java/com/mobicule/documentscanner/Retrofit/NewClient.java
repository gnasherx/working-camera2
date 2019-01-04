package com.mobicule.documentscanner.Retrofit;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface NewClient {

    @POST("/image/upload")
    Call<Response> sendData(@Body RequestBody params);

}