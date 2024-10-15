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
import com.google.rvadapter.AdmobNativeAdAdapter;
import com.nightbirds.streamz.Ads.InterstitialAds;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MovieFragment extends Fragment {

    ProgressBar movieProg;
    TextView errorText;
    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private List<Movie> movieList;
    private RequestQueue requestQueue;

    private String MovieCategory = "Hollywod";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View myView = inflater.inflate(R.layout.fragment_movie, container, false);

   //====================== find view id start
        errorText = myView.findViewById(R.id.errorText);
        movieProg = myView.findViewById(R.id.movieProg);
        recyclerView = myView.findViewById(R.id.movieView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        movieList = new ArrayList<>();
        movieAdapter = new MovieAdapter(getContext(), movieList, getActivity());



        AdmobNativeAdAdapter admobNativeAdAdapter=AdmobNativeAdAdapter.Builder.Companion.with(
                        "ca-app-pub-7944048926495091/3961592704",//Create a native ad id from admob console
                        movieAdapter,//The adapter you would normally set to your recyClerView
                        "small"//Set it with "small","medium" or "custom"
                )
                .adItemIterval(4)//native ad repeating interval in the recyclerview
                .build();

        recyclerView.setAdapter(admobNativeAdAdapter);


        requestQueue = Volley.newRequestQueue(getContext());

        fetchMovies();

        InterstitialAds.loadAds(getActivity());

        return myView;
    } //============= On Creat ENd

    private void fetchMovies() {
        String url = "http://103.145.232.246/api/v1/movies.php?sort_by=uploadTime+DESC,+DownHit+DESC&limit=11300";

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

                                String movieCategory = movieObj.getString("MovieCategory");
                                if (movieCategory.equals(MovieCategory)) {
                                    // Parse data from JSON
                                    String title = movieObj.getString("MovieTitle");
                                    String year = movieObj.getString("MovieYear");
                                    String story = movieObj.getString("MovieStory");
                                    String posterUrl = "http://103.145.232.246/Admin/main/images/" + movieObj.getString("MovieID") + "/poster/" + movieObj.getString("poster");
                                    String movieUrl = movieObj.getString("MovieWatchLink");

                                    // Create a new Movie object and add it to the list
                                    Movie movie = new Movie(title, year, story, posterUrl, movieUrl, movieCategory);


                                    movieList.add(movie);
                                    movieProg.setVisibility(View.GONE);
                                    errorText.setVisibility(View.GONE);
                                }else {

                                    String title = movieObj.getString("MovieTitle");
                                    String year = movieObj.getString("MovieYear");
                                    String story = movieObj.getString("MovieStory");
                                    String posterUrl = "http://103.145.232.246/Admin/main/images/" + movieObj.getString("MovieID") + "/poster/" + movieObj.getString("poster");
                                    String movieUrl = movieObj.getString("MovieWatchLink");

                                    // Create a new Movie object and add it to the list
                                    Movie movie = new Movie(title, year, story, posterUrl, movieUrl, movieCategory);


                                    movieList.add(movie);
                                    movieProg.setVisibility(View.GONE);
                                    errorText.setVisibility(View.GONE);

                                }
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

                        movieProg.setVisibility(View.GONE);
                        errorText.setVisibility(View.VISIBLE);
                        error.printStackTrace();
                        Log.e("MainActivity", "Volley Error: " + error.getMessage());
                    }
                });

        requestQueue.add(jsonArrayRequest);
    }

}