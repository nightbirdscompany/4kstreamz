package com.nightbirds.streamz;

import android.annotation.SuppressLint;
import android.app.Activity;
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

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.LoadAdError;
import com.squareup.picasso.Picasso;

import java.util.List;

public class EventAdepter extends RecyclerView.Adapter<EventAdepter.ViewHolder> {

    private LayoutInflater inflater;
    private List<Event> events;
    private Context context;
    private int clickCounter = 0;
    private InterstitialAd mInterstitialAd;

    public EventAdepter(Context ctx, List<Event> events) {
        this.inflater = LayoutInflater.from(ctx);
        this.events = events;
        this.context = ctx;

        // Preload the first interstitial ad
        loadInterstitialAd();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.event_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Event event = events.get(position);

        holder.eventTitle.setSelected(true);
        holder.eventTitle.setText(event.getEvent_title());
        Picasso.get().load(event.getEvent_poster()).placeholder(R.drawable.event).into(holder.eventImg);

        holder.eventLay.setOnClickListener(v -> {
            clickCounter++;
            if (clickCounter == 3) {
                // Show the interstitial ad if it's ready
                if (mInterstitialAd != null) {
                    mInterstitialAd.show((Activity) context);
                    mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            super.onAdDismissedFullScreenContent();
                            loadInterstitialAd(); // Load the next ad
                            startPlayerActivity(v, event);
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                            super.onAdFailedToShowFullScreenContent(adError);
                            startPlayerActivity(v, event);
                        }
                    });
                } else {
                    // If the ad is not ready, proceed directly to the activity
                    startPlayerActivity(v, event);
                }
                clickCounter = 0; // Reset the counter after showing the ad
            } else {
                // If the click counter is not 5, proceed directly to the activity
                startPlayerActivity(v, event);
            }
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    private void startPlayerActivity(View v, Event event) {
        PlayerActivity.videoUrl = event.getEvent_url();
        PlayerActivity.playerTitle = event.getEvent_title();
        Intent intent = new Intent(v.getContext(), PlayerActivity.class);
        v.getContext().startActivity(intent);
    }

    private void loadInterstitialAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(context, "ca-app-pub-7944048926495091/7633723336", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        mInterstitialAd = interstitialAd;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        mInterstitialAd = null;
                    }
                });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView eventTitle;
        ImageView eventImg;
        LinearLayout eventLay;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventImg = itemView.findViewById(R.id.eventImg);
            eventTitle = itemView.findViewById(R.id.eventTitle);
            eventLay = itemView.findViewById(R.id.eventLay);
        }
    }
}
