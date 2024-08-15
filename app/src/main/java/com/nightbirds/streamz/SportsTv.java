package com.nightbirds.streamz;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SportsTv extends AppCompatActivity {

    RecyclerView recyclerView;

    List<BanglaChannel> banglaChannels;

    private static String sportsJson = "https://nightbirdscompany.github.io/4kstreamzdata/app/json/sports.json";
    BanglaAllAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sports_tv);

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.sportsallrecycler);

        banglaChannels = new ArrayList<>();
        extractBanglaChannels();// recycler view end


    }//===================== on Create end

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void extractBanglaChannels() {

        RequestQueue queue = Volley.newRequestQueue(SportsTv.this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, sportsJson, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject banglaObject = jsonArray.getJSONObject(i);

                        BanglaChannel banglaChannel = new BanglaChannel();
                        banglaChannel.setChannel_name(banglaObject.getString("channel_name"));
                        banglaChannel.setChannel_logo(banglaObject.getString("channel_logo"));
                        banglaChannel.setChannel_url(banglaObject.getString("channel_url"));

                        banglaChannels.add(banglaChannel);

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }

                recyclerView.setLayoutManager(new GridLayoutManager(SportsTv.this, 3));
                adapter = new BanglaAllAdapter(SportsTv.this, banglaChannels);
                recyclerView.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("tag", "onErrorResponse: " + volleyError.getMessage());
            }
        });

        queue.add(jsonArrayRequest);
    }

}