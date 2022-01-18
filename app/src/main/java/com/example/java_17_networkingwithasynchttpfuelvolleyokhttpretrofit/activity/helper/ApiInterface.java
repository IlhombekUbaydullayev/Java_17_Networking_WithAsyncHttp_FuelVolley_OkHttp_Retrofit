package com.example.java_17_networkingwithasynchttpfuelvolleyokhttpretrofit.activity.helper;

import com.example.java_17_networkingwithasynchttpfuelvolleyokhttpretrofit.activity.model.PlayerDatas;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {
    @GET("son_parsing_php")
    Call<PlayerDatas> loadDatas();
}
