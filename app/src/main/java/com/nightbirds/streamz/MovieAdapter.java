package com.nightbirds.streamz;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    LayoutInflater inflater;
    List<Movie> movies;

    public MovieAdapter(Context ctx, List<Movie>movies) {

        this.inflater = LayoutInflater.from(ctx);
        this.movies = movies;
    }

    public void setFilteredList(List<Movie> filteredList){
        this.movies = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.search_item, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.movieName.setSelected(true);
        holder.movieActor.setSelected(true);
        holder.movieName.setText(movies.get(position).getMovie_title());
        holder.movieActor.setText(movies.get(position).getMovie_actor());
        Picasso.get().load(movies.get(position).getMovie_poster()).placeholder(R.drawable.iconai).into(holder.moviePoster);
        holder.searchItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PlayerActivity.videoUrl = (movies.get(position).getMovie_url());

                PlayerActivity.playerTitle = (movies.get(position).getMovie_title());

                Intent intent = new Intent(v.getContext(), PlayerActivity.class);
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {

        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView movieName, movieActor;
        ImageView moviePoster;
        LinearLayout searchItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            movieName = itemView.findViewById(R.id.movieName);
            moviePoster = itemView.findViewById(R.id.moviePoster);
            movieActor = itemView.findViewById(R.id.movieActor);
            searchItem = itemView.findViewById(R.id.searchItem);
        }
    }
}
