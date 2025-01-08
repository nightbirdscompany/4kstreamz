package com.nightbirds.streamz;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.nightbirds.streamz.Download.Data;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MovieViewHolder> {

    private Context context;
    private List<Movie> movies;
    private Activity activity;

    public SearchAdapter(Context context, List<Movie> movies, Activity activity) {
        this.context = context;
        this.movies = movies;
        this.activity = activity;
    }

    public void setFilteredList(List<Movie> filteredList) {
        this.movies = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_item, parent, false);
        return new MovieViewHolder(view);
    }

    public List<Movie> getMovieList() {
        return movies;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.movieTitle.setText(movie.getTitle());
        holder.movieYear.setText(movie.getYear());
        holder.movieGen.setText(movie.getMovieCategory());
        holder.movieStory.setText(movie.getStory());
        holder.movieStory.setSelected(true);
        holder.movieTitle.setSelected(true);
        holder.Movierating.setText(movie.getMovieRating());

        // Load the poster using Picasso
        Picasso.get()
                .load(movie.getPosterUrl())
                .placeholder(R.drawable.event) // A placeholder image
                .into(holder.poster);

        holder.itemView.setOnClickListener(v -> {
            MovieViewHolder.dialogTitle = movie.getTitle();
            MovieViewHolder.dialogDes = movie.getStory();
            showOptionsDialog(movie);
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView movieTitle, movieYear, movieStory, Movierating, movieGen;
        ImageView poster;

        public static String dialogTitle, dialogDes;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            movieTitle = itemView.findViewById(R.id.movieTitle);
            movieYear = itemView.findViewById(R.id.movieYear);
            movieStory = itemView.findViewById(R.id.movieStory);
            poster = itemView.findViewById(R.id.moviePoster);
            Movierating = itemView.findViewById(R.id.Movierating);
            movieGen = itemView.findViewById(R.id.movieGen);
        }
    }

    private void showOptionsDialog(Movie movie) {
        TextView customTitle = new TextView(context);
        customTitle.setText(MovieViewHolder.dialogTitle);
        customTitle.setTextSize(20); // Set title size
        customTitle.setPadding(20, 20, 20, 5); // Set padding
        customTitle.setTextColor(context.getResources().getColor(R.color.white));

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCustomTitle(customTitle)
                .setMessage(MovieViewHolder.dialogDes)
                .setPositiveButton("Play", (dialog, id) -> {
                    // Open PlayerActivity
                    PlayerActivity.videoUrl = movie.getMovieUrl();
                    PlayerActivity.playerTitle = movie.getTitle();
                    Intent intent = new Intent(context, PlayerActivity.class);
                    context.startActivity(intent);
                })
                .setNegativeButton("Download", (dialog, id) -> {
                    // Start downloading the file
                    /*
                    if (InterstitialAds.mInterstitialAd != null) {
                        InterstitialAds.mInterstitialAd.show(activity);
                        InterstitialAds.mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                super.onAdDismissedFullScreenContent();

                                // Reset ad and reload a new one
                                InterstitialAds.mInterstitialAd = null;
                                InterstitialAds.loadAds(activity);

                                // Proceed with the download process after ad is dismissed
                                Data.sampleUrls = new String[]{movie.getMovieUrl()};
                                Intent intent = new Intent(context, DownloadActivity.class);
                                context.startActivity(intent);
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                super.onAdFailedToShowFullScreenContent(adError);

                                // Log ad error
                                Log.e("AdError", "Interstitial ad failed to show: " + adError.getMessage());

                                // Reset ad and reload a new one
                                InterstitialAds.mInterstitialAd = null;
                                InterstitialAds.loadAds(activity);

                                // Proceed with the download process even if ad fails to show
                                Data.sampleUrls = new String[]{movie.getMovieUrl()};
                                Intent intent = new Intent(context, DownloadActivity.class);
                                context.startActivity(intent);
                            }
                        });
                    } else {
                    */
                    // Ad is not loaded, proceed with the download directly
                    Data.sampleUrls = new String[]{movie.getMovieUrl()};
                    Intent intent = new Intent(context, DownloadActivity.class);
                    context.startActivity(intent);

                    // Reload the interstitial ad for future use
                    // InterstitialAds.loadAds(activity);
                    // }
                })
                .setNeutralButton("Cancel", null);

        AlertDialog dialog = builder.create();

        dialog.setOnShowListener(dialogInterface -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context.getApplicationContext(), R.color.white));
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(context.getApplicationContext(), R.color.white));
            dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(ContextCompat.getColor(context.getApplicationContext(), R.color.white));
            dialog.getWindow().setBackgroundDrawableResource(R.color.black);

            // Change the message color
            TextView messageView = dialog.findViewById(android.R.id.message);
            if (messageView != null) {
                messageView.setTextColor(context.getResources().getColor(R.color.white)); // Replace with your message color resource
            }
        });

        dialog.show();
    }
}
