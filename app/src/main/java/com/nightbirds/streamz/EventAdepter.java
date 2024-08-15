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
import com.startapp.sdk.adsbase.StartAppAd;

import java.util.List;

public class EventAdepter extends RecyclerView.Adapter<EventAdepter.ViewHolder> {


    LayoutInflater inflater;
    List<Event> events;

    public EventAdepter (Context ctx, List<Event>events){

        this.inflater = LayoutInflater.from(ctx);
        this.events = events;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.event_item, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {



        holder.eventTitle.setSelected(true);
        holder.eventTitle.setText(events.get(position).getEvent_title());
        Picasso.get().load(events.get(position).getEvent_poster()).placeholder(R.drawable.event).into(holder.eventImg);

        holder.eventLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PlayerActivity.videoUrl = (events.get(position).getEvent_url());
                PlayerActivity.playerTitle = (events.get(position).getEvent_title());

                Intent intent = new Intent(v.getContext(), PlayerActivity.class);
                v.getContext().startActivity(intent);
                StartAppAd.showAd(v.getContext());
            }
        });

    }

    @Override
    public int getItemCount() {

        return events.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

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
