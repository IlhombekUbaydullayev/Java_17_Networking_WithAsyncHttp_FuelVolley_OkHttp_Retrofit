package com.example.java_17_networkingwithasynchttpfuelvolleyokhttpretrofit.activity.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.java_17_networkingwithasynchttpfuelvolleyokhttpretrofit.R;
import com.example.java_17_networkingwithasynchttpfuelvolleyokhttpretrofit.activity.adapter.PlayerAdapter;
import com.example.java_17_networkingwithasynchttpfuelvolleyokhttpretrofit.activity.helper.ApiClient;
import com.example.java_17_networkingwithasynchttpfuelvolleyokhttpretrofit.activity.helper.ApiInterface;
import com.example.java_17_networkingwithasynchttpfuelvolleyokhttpretrofit.activity.helper.ServerUrl;
import com.example.java_17_networkingwithasynchttpfuelvolleyokhttpretrofit.activity.model.Player;
import com.example.java_17_networkingwithasynchttpfuelvolleyokhttpretrofit.activity.model.PlayerDatas;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements ServerUrl {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        apiUsingAsyncHttp();
//          apiUsingFuel();       //still in the process of supporting
//        apiUsingVolley();
//        apiUsingOkHttp();
//        apiUsingRetrofit();     //still in the process of supporting
    }



    private void initViews() {
        context = this;
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new GridLayoutManager(context,1));
    }

    private void refreshAdapter(List<Player> players) {
        PlayerAdapter adapter = new PlayerAdapter(context,players);
        recyclerView.setAdapter(adapter);

    }

    private void processWithResponse(final PlayerDatas playerDatas) {

        progressBar.setVisibility(View.GONE);

        String message = playerDatas.getMessage();
        List<Player> players = playerDatas.getData();
        fireToast(message);
        refreshAdapter(players);
    }

    private void fireToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    private void apiUsingAsyncHttp() {
        progressBar.setVisibility(View.VISIBLE);

        RequestParams params = new RequestParams();
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(SERVER_URL,params, new TextHttpResponseHandler(){

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                Log.d("@@@","@@@onSuccess" + statusCode);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                Log.d("@@@","@@@onSuccess" + responseString);

                PlayerDatas playerDatas = new Gson().fromJson(responseString,PlayerDatas.class);
                processWithResponse(playerDatas);
            }
        });
    }



    private void apiUsingVolley() {
        progressBar.setVisibility(View.VISIBLE);

        StringRequest strReq = new StringRequest(SERVER_URL,(response) -> {
            Log.d("@@@","@@@onResponse :" +response);

            PlayerDatas playerDatas = new Gson().fromJson(response,PlayerDatas.class);
            processWithResponse(playerDatas);
        }, (error) -> {
            Log.d("@@@","@@@ onErrorResponse: " + error.getLocalizedMessage());
            progressBar.setVisibility(View.GONE);
        });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(strReq);
    }

    private void apiUsingOkHttp() {
        OkHttpClient httpClient = new OkHttpClient();
        Request request = new Request.Builder().url(SERVER_URL).build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("@@@","@@@ onErrorResponse" + e.getLocalizedMessage());
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String resp = response.body().string();

                runOnUiThread(() -> {
                    Log.d("@@@","@@@ onResponse" + resp);

                    PlayerDatas playerDatas = new Gson().fromJson(resp,PlayerDatas.class);
                    processWithResponse(playerDatas);
                });


            }
        });
    }

    private void apiUsingRetrofit() {
        progressBar.setVisibility(View.VISIBLE);

        ApiInterface apiInterface = ApiClient.getRetrofitInstance().create(ApiInterface.class);
        retrofit2.Call<PlayerDatas> call = apiInterface.loadDatas();
        call.enqueue(new retrofit2.Callback<PlayerDatas>() {

            @Override
            public void onResponse(retrofit2.Call<PlayerDatas> call, retrofit2.Response<PlayerDatas> response) {
                Log.d("@@@","@@@ onResponse:" + response.message());
                processWithResponse(response.body());
            }

            @Override
            public void onFailure(retrofit2.Call<PlayerDatas> call, Throwable t) {
                Log.d("@@@","@@@ onFailure:" + t.getLocalizedMessage());
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}