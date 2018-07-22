package com.kaooak.android.homebookkeeping.data;

import com.kaooak.android.homebookkeeping.data.JSON.GsonData;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitCB {
    @GET("https://www.cbr-xml-daily.ru/daily_json.js")
    Call<GsonData> getData();
}
