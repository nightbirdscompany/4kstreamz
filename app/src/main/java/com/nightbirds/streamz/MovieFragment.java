package com.nightbirds.streamz;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MovieFragment extends Fragment {

    private RecyclerView hollywood, bollywood, hindiDubbing, tamil, indianBangla, korean, animation;
    private MovieAdapter hollywoodAdapter, bollywoodAdapter, hindidubbingAdapter, tamilAdapter, indianBanglaAdapter, koreanAdapter, animationAdapter;
    private List<Movie> hollywoodMovies, bollywoodMovies, hindiDubbingMovies, tamilMovies, indianBanglaMovies, koreanMovies, animationMovies;
    private Set<String> movieIds; // For tracking unique movies
    private RequestQueue requestQueue;
    private TextView errorText;
    private Button hollywoodBut, bollywoodBut, hindidubbingBut, tamilBut, indianBanglaBut, koreanBut, animationBut;
    private ProgressBar movieProg;
    private boolean isLoading = false;  // For load more control
    private int page = 1;

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
        tamil = myView.findViewById(R.id.tamil);

        // Buttons
        hollywoodBut = myView.findViewById(R.id.hollywoodBut);
        bollywoodBut = myView.findViewById(R.id.bollywoodBut);
        indianBanglaBut = myView.findViewById(R.id.indianBanglaBut);
        koreanBut = myView.findViewById(R.id.koreanBut);
        animationBut = myView.findViewById(R.id.animationBut);
        hindidubbingBut = myView.findViewById(R.id.hindidubbingBut);
        tamilBut = myView.findViewById(R.id.tamilBut);

        // Initialize Lists for Movies
        hollywoodMovies = new ArrayList<>();
        bollywoodMovies = new ArrayList<>();
        hindiDubbingMovies = new ArrayList<>();
        tamilMovies = new ArrayList<>();
        indianBanglaMovies = new ArrayList<>();
        koreanMovies = new ArrayList<>();
        animationMovies = new ArrayList<>();

        // Initialize Set for unique Movie IDs
        movieIds = new HashSet<>();

        // Initialize Adapters for each category
        hollywoodAdapter = new MovieAdapter(getContext(), hollywoodMovies, getActivity());
        bollywoodAdapter = new MovieAdapter(getContext(), bollywoodMovies, getActivity());
        hindidubbingAdapter = new MovieAdapter(getContext(), hindiDubbingMovies, getActivity());
        tamilAdapter = new MovieAdapter(getContext(), tamilMovies, getActivity());
        indianBanglaAdapter = new MovieAdapter(getContext(), indianBanglaMovies, getActivity());
        koreanAdapter = new MovieAdapter(getContext(), koreanMovies, getActivity());
        animationAdapter = new MovieAdapter(getContext(), animationMovies, getActivity());

        // Set Layout Managers and Adapters
        setupRecyclerView(hollywood, hollywoodAdapter);
        setupRecyclerView(bollywood, bollywoodAdapter);
        setupRecyclerView(hindiDubbing, hindidubbingAdapter);
        setupRecyclerView(tamil, tamilAdapter);
        setupRecyclerView(indianBangla, indianBanglaAdapter);
        setupRecyclerView(korean, koreanAdapter);
        setupRecyclerView(animation, animationAdapter);

        // Initialize RequestQueue
        requestQueue = Volley.newRequestQueue(getContext());

        // Fetch initial data for each movie category
        fetchMoviesForCategory("Hollywood", hollywoodMovies, hollywoodAdapter, 1);
        fetchMoviesForCategory("Bollywood", bollywoodMovies, bollywoodAdapter, 1);
        fetchMoviesForCategory("Hindi Dubbed", hindiDubbingMovies, hindidubbingAdapter, 1);
        fetchMoviesForCategory("Tamil", tamilMovies, tamilAdapter, 1);
        fetchMoviesForCategory("Indian Bangla", indianBanglaMovies, indianBanglaAdapter, 1);
        fetchMoviesForCategory("Korean", koreanMovies, koreanAdapter, 1);
        fetchMoviesForCategory("Animation", animationMovies, animationAdapter, 1);

        // Handle button clicks for category selection
        handleCategorySwap();

        return myView;
    }

    private void handleCategorySwap() {
        hollywoodBut.setOnClickListener(v -> showRecyclerView(hollywood));
        bollywoodBut.setOnClickListener(v -> showRecyclerView(bollywood));
        hindidubbingBut.setOnClickListener(v -> showRecyclerView(hindiDubbing));
        tamilBut.setOnClickListener(v -> showRecyclerView(tamil));
        indianBanglaBut.setOnClickListener(v -> showRecyclerView(indianBangla));
        koreanBut.setOnClickListener(v -> showRecyclerView(korean));
        animationBut.setOnClickListener(v -> showRecyclerView(animation));
    }

    private void setupRecyclerView(RecyclerView recyclerView, MovieAdapter adapter) {
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                if (!isLoading && layoutManager != null && layoutManager.findLastCompletelyVisibleItemPosition() == adapter.getItemCount() - 1) {
                    isLoading = true;
                    loadMoreMovies(recyclerView, adapter);
                }
            }
        });
    }

    private void showRecyclerView(RecyclerView selectedRecyclerView) {
        hollywood.setVisibility(View.GONE);
        bollywood.setVisibility(View.GONE);
        hindiDubbing.setVisibility(View.GONE);
        tamil.setVisibility(View.GONE);
        indianBangla.setVisibility(View.GONE);
        korean.setVisibility(View.GONE);
        animation.setVisibility(View.GONE);

        selectedRecyclerView.setVisibility(View.VISIBLE);
    }

    private void fetchMoviesForCategory(String category, List<Movie> movieList, MovieAdapter adapter, int page) {
        errorText.setVisibility(View.GONE);
        String url = "http://103.145.232.246/api/v1/movies.php?sort_by=uploadTime+DESC,+DownHit+DESC&limit=20&page=" + page + "&category=" + category;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        List<Movie> newMovies = new ArrayList<>();

                        for (int i = 0; i < response.length(); i++) {
                            JSONObject movieObj = response.getJSONObject(i);
                            String movieId = movieObj.getString("MovieID");

                            // Check for duplicate MovieID
                            if (!movieIds.contains(movieId)) {
                                String title = movieObj.getString("MovieTitle");
                                String year = movieObj.getString("MovieYear");
                                String story = movieObj.getString("MovieStory");
                                String posterUrl = "http://103.145.232.246/Admin/main/images/" + movieId + "/poster/" + movieObj.getString("poster");
                                String movieRating = movieObj.getString("MovieRatings");
                                String movieUrl = movieObj.getString("MovieWatchLink");
                                String movieCategory = movieObj.getString("MovieCategory");

                                Movie movie = new Movie(title, year, story, posterUrl, movieRating, movieUrl, movieCategory);
                                newMovies.add(movie);

                                // Add MovieID to the Set
                                movieIds.add(movieId);
                            }
                        }

                        if (!newMovies.isEmpty()) {
                            movieList.addAll(newMovies);
                            adapter.notifyDataSetChanged();
                        } else if (page == 1) {
                            errorText.setVisibility(View.VISIBLE);
                            errorText.setText("No movies found for the selected category.");
                        }

                        movieProg.setVisibility(View.GONE);
                        isLoading = false;
                    } catch (JSONException e) {
                        e.printStackTrace();
                        handleError("Data parsing error. Please try again.");
                        movieProg.setVisibility(View.GONE);
                    }
                },
                error -> handleError("Server busy or network issue. Please try again.")
        );

        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                15000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        requestQueue.add(jsonArrayRequest);
    }

    private void handleError(String message) {
        errorText.setVisibility(View.VISIBLE);
        errorText.setText(message);
        isLoading = false;
        movieProg.setVisibility(View.GONE);
    }

    private void loadMoreMovies(RecyclerView recyclerView, MovieAdapter adapter) {
        page++;
        String category = getCategoryByRecyclerView(recyclerView);
        fetchMoviesForCategory(category, adapter.getMovieList(), adapter, page);
    }

    private String getCategoryByRecyclerView(RecyclerView recyclerView) {
        if (recyclerView == hollywood) return "Hollywood";
        if (recyclerView == bollywood) return "Bollywood";
        if (recyclerView == hindiDubbing) return "Hindi Dubbed";
        if (recyclerView == tamil) return "Tamil";
        if (recyclerView == indianBangla) return "Indian Bangla";
        if (recyclerView == korean) return "Korean";
        if (recyclerView == animation) return "Animation";
        return "";
    }
}
