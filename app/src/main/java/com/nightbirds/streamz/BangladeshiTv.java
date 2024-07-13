package com.nightbirds.streamz;

import android.os.Bundle;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BangladeshiTv extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BanglaAllAdapter adapter;
    private List<String> imageUrlList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bangladeshitv);

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //====== toolbar end

        recyclerView = findViewById(R.id.banglaallrecycler);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2)); // Adjust span count as per your needs

        imageUrlList = new ArrayList<>();
        adapter = new BanglaAllAdapter(this, imageUrlList);
        recyclerView.setAdapter(adapter);

        fetchImages(); // Method to fetch image URLs from server or local storage
    }

    private void fetchImages() {
        String url = "https://i.imgur.com/8yIIokW.jpg"; // Replace with your actual API endpoint
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject object = response.getJSONObject(i);
                            String imageUrl = object.getString("movie_poster"); // Replace with your JSON key
                            imageUrlList.add(imageUrl);
                        }
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    error.printStackTrace();
                });

        queue.add(request);


    }//===================== on Create end

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}