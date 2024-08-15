package com.nightbirds.streamz;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

        holder.cName.setSelected(true);
        holder.cName.setText(banglaChannels.get(position).getChannel_name());
        Picasso.get().load(banglaChannels.get(position).getChannel_logo()).placeholder(R.drawable.iconai).into(holder.cLogo);

        holder.cLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PlayerActivity.videoUrl = (banglaChannels.get(position).getChannel_url());
                PlayerActivity.playerTitle = (banglaChannels.get(position).getChannel_name());

                Intent intent = new Intent(v.getContext(), PlayerActivity.class);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {

        return banglaChannels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

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