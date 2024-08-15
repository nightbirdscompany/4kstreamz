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
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MovieFragment extends Fragment {

    RecyclerView recyclerView;
    List<Movie> movies;

    private static String movieJson = "https://developerhasan4.github.io/tsports/video/check.json";
    MovieAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View myView = inflater.inflate(R.layout.fragment_movie, container, false);

   //====================== find view id start
        recyclerView = myView.findViewById(R.id.movieView);

        //====================== find view id end

        movies = new ArrayList<>();
        extractMovies();

        return myView;
    } //============= On Creat ENd

    private void extractMovies() {

        RequestQueue queue = Volley.newRequestQueue(getActivity());
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

                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                adapter = new MovieAdapter(getActivity(), movies);
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