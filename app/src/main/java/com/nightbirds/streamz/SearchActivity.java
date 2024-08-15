package com.nightbirds.streamz;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
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

public class SearchActivity extends AppCompatActivity {

    SearchView search;

    RecyclerView recyclerView;
    List<Movie> movies;

    private static String movieJson = "https://nightbirdscompany.github.io/4kstreamzdata/app/json/movie.json";
    MovieAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        search = findViewById(R.id.search);
        recyclerView = findViewById(R.id.search_list);


        search.clearFocus();// for search

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        }); //====================== search end

        Toolbar toolbar = findViewById(R.id.toolbar);// for back button

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);// for back button end


        movies = new ArrayList<>(); //for recycler
        extractMovies();


    }//===================== on Create end


    // for searchbar

    @Override// for back button
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }// for back button end

    private void filterList(String newText) {//for search

        List<Movie> filteredList = new ArrayList<>();
        for (Movie movie : movies){
            if (movie.getMovie_title().toLowerCase().contains(newText.toLowerCase())){
                filteredList.add(movie);
            }
        }

        if (filteredList.isEmpty()){
            Toast.makeText(this, "No Movie Found", Toast.LENGTH_SHORT).show();
        }else {
              adapter.setFilteredList(filteredList);
        }
    }//search end

    private void extractMovies() {// for recycler

        RequestQueue queue = Volley.newRequestQueue(SearchActivity.this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, movieJson, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject movieObject = jsonArray.getJSONObject(i);

                        Movie movie = new Movie();
                        movie.setMovie_title(movieObject.getString("movie_title").toString());
                        movie.setMovie_poster(movieObject.getString("movie_poster").toString());
                        movie.setMovie_actor(movieObject.getString("movie_actor").toString());
                        movie.setMovie_url(movieObject.getString("movie_url").toString());

                        movies.add(movie);


                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }

                recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
                adapter = new MovieAdapter(SearchActivity.this, movies);
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