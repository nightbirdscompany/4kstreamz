package com.nightbirds.streamz;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import com.google.rvadapter.AdmobNativeAdAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private SearchView search;
    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private List<Movie> movieList;
    private RequestQueue requestQueue;
    ProgressBar srcProg;
    private TextView errorText, noMovie;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        srcProg = findViewById(R.id.srcProg);
        noMovie = findViewById(R.id.noMovie);
        errorText = findViewById(R.id.errorText);
        search = findViewById(R.id.search);  // Use the correct ID for the SearchView
        recyclerView = findViewById(R.id.search_list);  // Use the correct ID for the RecyclerView

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        movieList = new ArrayList<>();
        movieAdapter = new MovieAdapter(this, movieList, SearchActivity.this);
        AdmobNativeAdAdapter admobNativeAdAdapter=AdmobNativeAdAdapter.Builder.Companion.with(
                        "ca-app-pub-3940256099942544/2247696110",//Create a native ad id from admob console
                        movieAdapter,//The adapter you would normally set to your recyClerView
                        "small"//Set it with "small","medium" or "custom"
                )
                .adItemIterval(3)//native ad repeating interval in the recyclerview
                .build();

        recyclerView.setAdapter(admobNativeAdAdapter);

        requestQueue = Volley.newRequestQueue(this);

        fetchMovies();



        search.clearFocus(); // for search

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

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override // for back button
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void filterList(String newText) { //for search
        List<Movie> filteredList = new ArrayList<>();
        for (Movie movie : movieList) {
            if (movie.getTitle().toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(movie);
                noMovie.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        }

        if (filteredList.isEmpty()) {
            noMovie.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            errorText.setVisibility(View.GONE);
        } else {
            movieAdapter.setFilteredList(filteredList);
            noMovie.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }


    private void fetchMovies() {
        String url = "http://103.145.232.246/api/v1/movies.php?sort_by=uploadTime+DESC,+DownHit+DESC&limit=11200";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject movieObj = response.getJSONObject(i);

                                // Parse data from JSON
                                String movieCategory = movieObj.getString("MovieCategory");
                                String title = movieObj.getString("MovieTitle");
                                String year = movieObj.getString("MovieYear");
                                String story = movieObj.getString("MovieStory");
                                String posterUrl = "http://103.145.232.246/Admin/main/images/"+movieObj.getString("MovieID")+"/poster/"+movieObj.getString("poster");
                                String movieUrl = movieObj.getString("MovieWatchLink");

                                // Create a new Movie object and add it to the list
                                Movie movie = new Movie(title, year, story, posterUrl, movieUrl, movieCategory);
                                movieList.add(movie);
                                srcProg.setVisibility(View.GONE);
                                errorText.setVisibility(View.GONE);

                            }

                            // Notify adapter that data has changed
                            movieAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("MainActivity", "JSON Exception: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        errorText.setVisibility(View.VISIBLE);
                        srcProg.setVisibility(View.GONE);


                        error.printStackTrace();
                        Log.e("MainActivity", "Volley Error: " + error.getMessage());
                    }
                });

        requestQueue.add(jsonArrayRequest);
    }

}