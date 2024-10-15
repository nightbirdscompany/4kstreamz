package com.nightbirds.streamz;

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
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.AdRequest;
import com.nightbirds.streamz.Ads.InterstitialAds;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BanglaAllAdapter extends RecyclerView.Adapter<BanglaAllAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<BanglaChannel> banglaChannels;
    private Context context;
    private int clickCounter = 0;

    private InterstitialAd mInterstitialAd;

    public BanglaAllAdapter(Context ctx, List<BanglaChannel> banglaChannels) {
        this.inflater = LayoutInflater.from(ctx);
        this.banglaChannels = banglaChannels;
        this.context = ctx;


        // Preload the first interstitial ad

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.channel_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BanglaChannel channel = banglaChannels.get(position);

        holder.cName.setSelected(true);
        holder.cName.setText(channel.getChannel_name());
        Picasso.get().load(channel.getChannel_logo()).placeholder(R.drawable.iconai).into(holder.cLogo);

        holder.cLay.setOnClickListener(v -> {



                PlayerActivity.videoUrl = channel.getChannel_url();
                PlayerActivity.playerTitle = channel.getChannel_name();
                Intent intent = new Intent(v.getContext(), PlayerActivity.class);
                v.getContext().startActivity(intent);


        });
    }

    @Override
    public int getItemCount() {
        return banglaChannels.size();
    }





    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView cName;
        ImageView cLogo;
        LinearLayout cLay;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cName = itemView.findViewById(R.id.cName);
            cLogo = itemView.findViewById(R.id.cLogo);
            cLay = itemView.findViewById(R.id.cLay);
        }
    }


}
