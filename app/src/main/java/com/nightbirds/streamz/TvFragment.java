package com.nightbirds.streamz;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class TvFragment extends Fragment {

    TextView bangladeshall, indiaall, sportsall;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View tvView = inflater.inflate(R.layout.fragment_tv, container, false);

        bangladeshall = tvView.findViewById(R.id.bangladeshall);
        indiaall = tvView.findViewById(R.id.indiaall);
        sportsall = tvView.findViewById(R.id.sportsall);

        bangladeshall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(getActivity().getApplication(), BangladeshiTv.class);
                startActivity(intent);
            }
        });

        indiaall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(getActivity().getApplication(), IndianTv.class);
                startActivity(intent);
            }
        });

        sportsall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(getActivity().getApplication(), SportsTv.class);
                startActivity(intent);
            }
        });

        return tvView;

    }
}