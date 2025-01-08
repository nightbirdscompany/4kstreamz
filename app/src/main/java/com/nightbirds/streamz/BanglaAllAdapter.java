package com.nightbirds.streamz;

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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BanglaAllAdapter extends RecyclerView.Adapter<BanglaAllAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<BanglaChannel> banglaChannels;
    private Context context;

    public BanglaAllAdapter(Context ctx, List<BanglaChannel> banglaChannels) {
        this.inflater = LayoutInflater.from(ctx);
        this.banglaChannels = banglaChannels;
        this.context = ctx;

        // Sort channels alphabetically by name when the adapter is initialized
        sortChannelsAlphabetically();
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
        // Focus change listener
        holder.itemView.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                holder.itemView.setBackgroundResource(R.drawable.tv_control);
            } else {
                holder.itemView.setBackgroundResource(android.R.color.transparent);
            }
        });

        holder.cName.setSelected(true);
        holder.cName.setText(channel.getChannel_name());
        Picasso.get().load(channel.getChannel_logo()).placeholder(R.drawable.iconai).into(holder.cLogo);

        holder.itemView.setOnClickListener(v -> {
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

    /**
     * Sort the banglaChannels list alphabetically by channel name.
     */
    private void sortChannelsAlphabetically() {
        Collections.sort(banglaChannels, new Comparator<BanglaChannel>() {
            @Override
            public int compare(BanglaChannel channel1, BanglaChannel channel2) {
                return channel1.getChannel_name().compareToIgnoreCase(channel2.getChannel_name());
            }
        });
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
