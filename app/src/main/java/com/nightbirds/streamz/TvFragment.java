package com.nightbirds.streamz;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class TvFragment extends Fragment {

    RecyclerView recyclerView, recyclerView1, recyclerView2;
    List<BanglaChannel> banglaChannels, banglaChannels2, banglaChannels3;

    private static String banglaJson = "https://nightbirdscompany.github.io/4kstreamzdata/app/json/bdtv.json";
    private static String indiaJson = "https://nightbirdscompany.github.io/4kstreamzdata/app/json/indiatv.json";
    private static String sportsJson = "https://nightbirdscompany.github.io/4kstreamzdata/app/json/sports.json";
    BanglaAllAdapter adapter;

    TextView bangladeshall, indiaall, sportsall;

    ProgressBar tvProg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View tvView = inflater.inflate(R.layout.fragment_tv, container, false);


        //======== for see all button


        AdView adView = tvView.findViewById(R.id.adView2);

        // Create an AdRequest and load the banner
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);



        bangladeshall = tvView.findViewById(R.id.bangladeshall);
        indiaall = tvView.findViewById(R.id.indiaall);
        sportsall = tvView.findViewById(R.id.sportsall);
        tvProg = tvView.findViewById(R.id.tvProg);

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

        //================= see all button end

        //================= for bangla recycler view

        recyclerView = tvView.findViewById(R.id.banglaatv);
        recyclerView1 = tvView.findViewById(R.id.indiatv);
        recyclerView2 = tvView.findViewById(R.id.sportstv);

        banglaChannels = new ArrayList<>();
        extractBanglaChannels();
        banglaChannels2 = new ArrayList<>();
        extractIndianChannel();
        banglaChannels3 = new ArrayList<>();
        extractSportsChannel();

        return tvView;

    }

    private void extractSportsChannel() {//sports start

        RequestQueue queue3 = Volley.newRequestQueue(getActivity());
        JsonArrayRequest jsonArrayRequest3 = new JsonArrayRequest(Request.Method.GET, sportsJson, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject sportsObject = jsonArray.getJSONObject(i);

                        BanglaChannel banglaChannel = new BanglaChannel();
                        banglaChannel.setChannel_name(sportsObject.getString("channel_name"));
                        banglaChannel.setChannel_logo(sportsObject.getString("channel_logo"));
                        banglaChannel.setChannel_url(sportsObject.getString("channel_url"));

                        banglaChannels3.add(banglaChannel);
                        tvProg.setVisibility(View.GONE);

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }

                recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                adapter = new BanglaAllAdapter(getActivity(), banglaChannels3);
                recyclerView2.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("tag", "onErrorResponse: " + volleyError.getMessage());
            }
        });

        queue3.add(jsonArrayRequest3);

    }//sports end

    private void extractIndianChannel() { // for india channel

        RequestQueue queue2 = Volley.newRequestQueue(getActivity());
        JsonArrayRequest jsonArrayRequest2 = new JsonArrayRequest(Request.Method.GET, indiaJson, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject indiaObject = jsonArray.getJSONObject(i);

                        BanglaChannel banglaChannel = new BanglaChannel();
                        banglaChannel.setChannel_name(indiaObject.getString("channel_name"));
                        banglaChannel.setChannel_logo(indiaObject.getString("channel_logo"));
                        banglaChannel.setChannel_url(indiaObject.getString("channel_url"));

                        banglaChannels2.add(banglaChannel);
                        tvProg.setVisibility(View.GONE);

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }

                recyclerView1.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                adapter = new BanglaAllAdapter(getActivity(), banglaChannels2);
                recyclerView1.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("tag", "onErrorResponse: " + volleyError.getMessage());
            }
        });

        queue2.add(jsonArrayRequest2);


    }// india end

    private void extractBanglaChannels() {

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, banglaJson, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject banglaObject = jsonArray.getJSONObject(i);

                        BanglaChannel banglaChannel = new BanglaChannel();
                        banglaChannel.setChannel_name(banglaObject.getString("channel_name"));
                        banglaChannel.setChannel_logo(banglaObject.getString("channel_logo"));
                        banglaChannel.setChannel_url(banglaObject.getString("channel_url"));

                        banglaChannels.add(banglaChannel);
                        tvProg.setVisibility(View.GONE);

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }

                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                adapter = new BanglaAllAdapter(getActivity(), banglaChannels);
                recyclerView.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("tag", "onErrorResponse: " + volleyError.getMessage());
            }
        });

        queue.add(jsonArrayRequest);
    }

}