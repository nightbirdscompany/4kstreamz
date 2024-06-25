package com.nightbirds.streamz;

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
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class MovieFragment extends Fragment {

    RecyclerView movieView;
    HashMap<String, String> hashMap;
    ArrayList<HashMap<String, String>> arrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View myView = inflater.inflate(R.layout.fragment_movie, container, false);

   //====================== find view id start
        movieView = myView.findViewById(R.id.movieView);

        //====================== find view id end

        String serverUrl = "http://livetv.free.nf/movie.json";

        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, serverUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {

                    for (int x=0; x< response.length(); x++){

                        JSONObject jsonObject = response.getJSONObject(x);

                        String title = jsonObject.getString("title");
                        String movie_poster = jsonObject.getString("movie_poster");
                        String movie_link = jsonObject.getString("movie_link");

                        arrayList = new ArrayList<>();

                        hashMap = new HashMap<>();
                        hashMap.put("movie_link", movie_link);
                        hashMap.put("title", title);
                        hashMap.put("movie_poster", movie_poster);
                        arrayList.add(hashMap);
                    }

                    MyAdapter adapter = new MyAdapter();
                    movieView.setAdapter(adapter);
                    movieView.setLayoutManager(new LinearLayoutManager(getContext()));

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                Log.d("serverRes", response.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(arrayRequest);


        //====================== array & hashmap start



        //====================== array & hashmap end



        return myView;
    } //============= On Creat ENd

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

        private class MyViewHolder extends RecyclerView.ViewHolder{

            TextView movieName;

            ImageView moviePoster;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);

                movieName = itemView.findViewById(R.id.movieName);
                moviePoster = itemView.findViewById(R.id.moviePoster);
            }
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            LayoutInflater inflater = getLayoutInflater();
            View myView = inflater.inflate(R.layout.movie_items, parent, false);
            return new MyViewHolder(myView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

            hashMap = arrayList.get(position);
            String movie_link = hashMap.get("movie_link");
            String title = hashMap.get("title");
            String movie_poster = hashMap.get("movie_poster");
            holder.movieName.setText(title);

            Picasso.get()
                    .load(movie_poster)
                    .placeholder(R.drawable.iconai)
                    .into(holder.moviePoster);

        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }


    }
}