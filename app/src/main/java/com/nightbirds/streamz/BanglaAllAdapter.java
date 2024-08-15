package com.nightbirds.streamz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class BanglaAllAdapter extends RecyclerView.Adapter<BanglaAllAdapter.ViewHolder> {

    LayoutInflater inflater;
    List<BanglaChannel> banglaChannels;

    public BanglaAllAdapter(Context ctx, List<BanglaChannel>banglaChannels){

        this.inflater = LayoutInflater.from(ctx);
        this.banglaChannels = banglaChannels ;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.channel_item, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.cName.setText(banglaChannels.get(position).getChannel_name());
        Picasso.get().load(banglaChannels.get(position).getChannel_logo()).placeholder(R.drawable.iconai).into(holder.cLogo);

    }

    @Override
    public int getItemCount() {

        return banglaChannels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView cName;
        ImageView cLogo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cName = itemView.findViewById(R.id.cName);
            cLogo = itemView.findViewById(R.id.cLogo);
        }
    }

}