package com.kaooak.android.homebookkeeping.data;

import com.kaooak.android.homebookkeeping.data.JSON.GsonData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

public interface RetrofitCBR {

    @GET("daily_json.js")
    Call<GsonData> getTodayData();

    @GET("archive/{date}/daily_json.js")
    Call<GsonData> getData(@Path("date") String date);

}
