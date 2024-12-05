package com.nightbirds.streamz;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.nightbirds.streamz.Ads.InterstitialAds;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MovieFragment extends Fragment {

    private RecyclerView hollywood, bollywood, hindiDubbing, indianBangla, korean, animation;
    private MovieAdapter hollywoodAdapter, bollywoodAdapter, hindidubbingAdapter, indianBanglaAdapter, koreanAdapter, animationAdapter;
    private List<Movie> hollywoodMovies, bollywoodMovies, hindiDubbingMovies, indianBanglaMovies, koreanMovies, animationMovies;
    private RequestQueue requestQueue;
    private ProgressBar movieProg;
    private TextView errorText;
    private Button hollywoodBut, bollywoodBut, hindidubbingBut, indianBanglaBut, koreanBut, animationBut;

    private boolean isLoading = false;  // For load more control
    private int page = 1;
    private Set<String> loadedMovieIds = new HashSet<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_movie, container, false);

        // Initialize views
        errorText = myView.findViewById(R.id.errorText);
        movieProg = myView.findViewById(R.id.movieProg);

        // RecyclerViews
        hollywood = myView.findViewById(R.id.hollywood);
        bollywood = myView.findViewById(R.id.bollywood);
        indianBangla = myView.findViewById(R.id.indianBangla);
        korean = myView.findViewById(R.id.korean);
        animation = myView.findViewById(R.id.animation);
        hindiDubbing = myView.findViewById(R.id.hindiDubbing);

        // Buttons
        hollywoodBut = myView.findViewById(R.id.hollywoodBut);
        bollywoodBut = myView.findViewById(R.id.bollywoodBut);
        indianBanglaBut = myView.findViewById(R.id.indianBanglaBut);
        koreanBut = myView.findViewById(R.id.koreanBut);
        animationBut = myView.findViewById(R.id.animationBut);
        hindidubbingBut = myView.findViewById(R.id.hindidubbingBut);

        // Initialize Lists for Movies
        hollywoodMovies = new ArrayList<>();
        bollywoodMovies = new ArrayList<>();
        hindiDubbingMovies = new ArrayList<>();
        indianBanglaMovies = new ArrayList<>();
        koreanMovies = new ArrayList<>();
        animationMovies = new ArrayList<>();

        // Initialize Adapters for each category
        hollywoodAdapter = new MovieAdapter(getContext(), hollywoodMovies, getActivity());
        bollywoodAdapter = new MovieAdapter(getContext(), bollywoodMovies, getActivity());
        hindidubbingAdapter = new MovieAdapter(getContext(), hindiDubbingMovies, getActivity());
        indianBanglaAdapter = new MovieAdapter(getContext(), indianBanglaMovies, getActivity());
        koreanAdapter = new MovieAdapter(getContext(), koreanMovies, getActivity());
        animationAdapter = new MovieAdapter(getContext(), animationMovies, getActivity());

        // Set Layout Managers and Adapters
        setupRecyclerView(hollywood, hollywoodAdapter);
        setupRecyclerView(bollywood, bollywoodAdapter);
        setupRecyclerView(hindiDubbing, hindidubbingAdapter);
        setupRecyclerView(indianBangla, indianBanglaAdapter);
        setupRecyclerView(korean, koreanAdapter);
        setupRecyclerView(animation, animationAdapter);

        // Initialize RequestQueue
        requestQueue = Volley.newRequestQueue(getContext());

        // Fetch initial data for each movie category
        fetchMoviesForCategory("Hollywood", hollywoodMovies, hollywoodAdapter, 1);
        fetchMoviesForCategory("Bollywood", bollywoodMovies, bollywoodAdapter, 1);
        fetchMoviesForCategory("Hindi Dubbed", hindiDubbingMovies, hindidubbingAdapter,1);
        fetchMoviesForCategory("Indian Bangla", indianBanglaMovies, indianBanglaAdapter, 1);
        fetchMoviesForCategory("Korean", koreanMovies, koreanAdapter, 1);
        fetchMoviesForCategory("Animation", animationMovies, animationAdapter, 1);

        // Set Button Click Listeners for Switching Visibility
        hollywoodBut.setOnClickListener(v -> showRecyclerView(hollywood));
        bollywoodBut.setOnClickListener(v -> showRecyclerView(bollywood));
        hindidubbingBut.setOnClickListener(v -> showRecyclerView(hindiDubbing));
        indianBanglaBut.setOnClickListener(v -> showRecyclerView(indianBangla));
        koreanBut.setOnClickListener(v -> showRecyclerView(korean));
        animationBut.setOnClickListener(v -> showRecyclerView(animation));

        return myView;
    }

    // Setup RecyclerView with adapter and scroll listener for load more
    private void setupRecyclerView(RecyclerView recyclerView, MovieAdapter adapter) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter); // Using adapter directly without ads

        // Scroll listener to load more when reaching end
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (!isLoading && layoutManager != null && layoutManager.findLastCompletelyVisibleItemPosition() == adapter.getItemCount() - 1) {
                    // End of list reached, load more
                    isLoading = true;
                    loadMoreMovies(recyclerView, adapter);
                }
            }
        });
    }

    // Helper method to show only the selected RecyclerView
    private void showRecyclerView(RecyclerView selectedRecyclerView) {
        hollywood.setVisibility(View.GONE);
        bollywood.setVisibility(View.GONE);
        hindiDubbing.setVisibility(View.GONE);
        indianBangla.setVisibility(View.GONE);
        korean.setVisibility(View.GONE);
        animation.setVisibility(View.GONE);

        // Show the selected one
        selectedRecyclerView.setVisibility(View.VISIBLE);
    }

    // Fetch movies for each category (with pagination support)
    private void fetchMoviesForCategory(String category, List<Movie> movieList, MovieAdapter adapter, int page) {
        String url = "http://103.145.232.246/api/v1/movies.php?sort_by=uploadTime+DESC,+DownHit+DESC&limit=20&page=" + page + "&category=" + category;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject movieObj = response.getJSONObject(i);
                            String movieId = movieObj.getString("MovieID");

                            // Avoid reloading already loaded movies
                            if (loadedMovieIds.contains(movieId)) continue;

                            loadedMovieIds.add(movieId);

                            String title = movieObj.getString("MovieTitle");
                            String year = movieObj.getString("MovieYear");
                            String story = movieObj.getString("MovieStory");
                            String posterUrl = "http://103.145.232.246/Admin/main/images/" + movieId + "/poster/" + movieObj.getString("poster");
                            String movieRating = movieObj.getString("MovieRatings");
                            String movieUrl = movieObj.getString("MovieWatchLink");


                            Movie movie = new Movie(title, year, story, posterUrl, movieRating, movieUrl);
                            movieList.add(movie);
                            movieProg.setVisibility(View.GONE);
                            errorText.setVisibility(View.GONE);
                        }
                        adapter.notifyDataSetChanged();
                        isLoading = false;
                    } catch (JSONException e) {
                        e.printStackTrace();
                        errorText.setVisibility(View.VISIBLE);
                        errorText.setText("Data parsing error. Please try again.");
                    }
                },
                error -> {
                    error.printStackTrace();
                    errorText.setVisibility(View.VISIBLE);
                    errorText.setText("Failed to load movies. Please try again.");
                    movieProg.setVisibility(View.GONE);
                });

        requestQueue.add(jsonArrayRequest);
    }


    // Load more movies on scroll
    private void loadMoreMovies(RecyclerView recyclerView, MovieAdapter adapter) {
        // Increase page number for load more
        page++;
        String category = getCategoryByRecyclerView(recyclerView);
        fetchMoviesForCategory(category, adapter.getMovieList(), adapter, page);
    }

    // Get category name based on the recyclerView
    private String getCategoryByRecyclerView(RecyclerView recyclerView) {
        if (recyclerView == hollywood) return "Hollywood";
        if (recyclerView == bollywood) return "Bollywood";
        if (recyclerView == hindiDubbing) return "Hindi Dubbed";
        if (recyclerView == indianBangla) return "Indian Bangla";
        if (recyclerView == korean) return "Korean";
        if (recyclerView == animation) return "Animation";
        return "";
    }
}
