package com.example.testnimap;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.testnimap.Adapters.TestNimapAdapter;
import com.example.testnimap.models.TestNimap;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    private String url;
    private TestNimap mTestNimap;
    private RecyclerView mRecyclerView;
    private SharedPreferences mSharedPreferences;
    private static final String MyPREFERENCES = "MyPrefs";
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressBar = findViewById(R.id.progressBar);
        mSharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.hasFixedSize();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        if(!isConnected() && isCached()) {
            String cachedResponse = mSharedPreferences.getString("DATA","");
            mTestNimap = new Gson().fromJson(cachedResponse,TestNimap.class);
            bindAdapter();
            mProgressBar.setVisibility(View.GONE);
        } else {
            url = "http://test.chatongo.in/testdata.json";
            getData();
        }
    }

    private boolean isCached() {
        String isCached = mSharedPreferences.getString("DATA","");
        return !isCached.equals("");
    }

    private void getData() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mTestNimap = new Gson().fromJson(response, TestNimap.class);
                Log.d("Response", "" + mTestNimap.getData().getRecords().get(0).getTitle());
                cachedData(response);
                bindAdapter();
                mProgressBar.setVisibility(View.GONE);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i("Error", "Error :" + error.toString());
            }
        });
        requestQueue.add(mStringRequest);
    }

    private void cachedData(String response) {
        mSharedPreferences.edit().putString("DATA",response).apply();
    }

    private void bindAdapter() {
        TestNimapAdapter mTestNimapAdapter = new TestNimapAdapter(mTestNimap.getData().getRecords());
        mRecyclerView.setAdapter(mTestNimapAdapter);
        mRecyclerView.setVisibility(View.VISIBLE);
    }


    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            return (mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting());
        }
        else
            return false;
    }
}
