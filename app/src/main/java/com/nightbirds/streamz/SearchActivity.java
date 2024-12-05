package com.nightbirds.streamz;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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

    // Debounce variables
    private final Handler searchHandler = new Handler();
    private Runnable searchRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(android.R.color.black));
        }

        srcProg = findViewById(R.id.srcProg);
        noMovie = findViewById(R.id.noMovie);
        errorText = findViewById(R.id.errorText);
        search = findViewById(R.id.search);
        recyclerView = findViewById(R.id.search_list);

        ImageView searchIcon = search.findViewById(androidx.appcompat.R.id.search_mag_icon);
        searchIcon.setColorFilter(Color.BLACK); // Set your desired color here

        TextView searchText = search.findViewById(androidx.appcompat.R.id.search_src_text);
        searchText.setTextColor(Color.BLACK);         // Change query text color
        searchText.setHintTextColor(Color.BLACK);     // Change hint text color

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        movieList = new ArrayList<>();
        movieAdapter = new MovieAdapter(this, movieList, SearchActivity.this);

        recyclerView.setAdapter(movieAdapter); // Directly set the MovieAdapter without ads
        requestQueue = Volley.newRequestQueue(this);

        // Set up the search listener
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Remove previous callbacks
                searchHandler.removeCallbacks(searchRunnable);

                // Set a new search query
                searchRunnable = () -> performSearch(newText);
                // Delaying the search query execution for 300 milliseconds
                searchHandler.postDelayed(searchRunnable, 300);
                return true;
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.getNavigationIcon().setTint(getResources().getColor(R.color.white));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void performSearch(String query) {
        // Check if query is empty
        if (query.isEmpty()) {
            noMovie.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            return;
        }

        // Show progress indicator
        srcProg.setVisibility(View.VISIBLE);
        noMovie.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);

        // Fetch movies based on search query
        String url = "http://103.145.232.246/api/v1/movies.php?search=" + query;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    movieList.clear(); // Clear previous results
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject movieObj = response.getJSONObject(i);
                            String title = movieObj.getString("MovieTitle");
                            String year = movieObj.getString("MovieYear");
                            String story = movieObj.getString("MovieStory");
                            String posterUrl = "http://103.145.232.246/Admin/main/images/" + movieObj.getString("MovieID") + "/poster/" + movieObj.getString("poster");
                            String rating = movieObj.getString("MovieRatings");
                            String movieUrl = movieObj.getString("MovieWatchLink");
                            String movieCategory = movieObj.getString("MovieCategory");

                            // Create a new Movie object and add it to the list
                            Movie movie = new Movie(title, year, story, posterUrl, rating, movieUrl);
                            movieList.add(movie);
                        }

                        if (movieList.isEmpty()) {
                            noMovie.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                            errorText.setVisibility(View.GONE);
                        } else {
                            noMovie.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            errorText.setVisibility(View.GONE);
                            movieAdapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("SearchActivity", "JSON Exception: " + e.getMessage());
                    } finally {
                        srcProg.setVisibility(View.GONE);
                    }
                },
                error -> {
                    errorText.setVisibility(View.VISIBLE);
                    srcProg.setVisibility(View.GONE);
                    Log.e("SearchActivity", "Volley Error: " + error.getMessage());
                }
        );

        requestQueue.add(jsonArrayRequest);
    }
}
