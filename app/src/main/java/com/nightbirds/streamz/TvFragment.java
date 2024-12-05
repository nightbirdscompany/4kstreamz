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

    private RecyclerView recyclerView, recyclerView1, recyclerView2;
    private List<BanglaChannel> banglaChannels, banglaChannels2, banglaChannels3;
    private static final String banglaJson = "https://nightbirdscompany.github.io/4kstreamzdata/app/json/bdtv.json";
    private static final String indiaJson = "https://nightbirdscompany.github.io/4kstreamzdata/app/json/indiatv.json";
    private static final String sportsJson = "https://nightbirdscompany.github.io/4kstreamzdata/app/json/sports.json";
    private BanglaAllAdapter adapter;
    private TextView bangladeshall, indiaall, sportsall;
    private ProgressBar tvProg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View tvView = inflater.inflate(R.layout.fragment_tv, container, false);
        initializeViews(tvView);
//        loadAds(tvView);
        setClickListeners();
        initializeChannelsLists();
        loadChannelsData();
        return tvView;
    }

    private void initializeViews(View tvView) {
        recyclerView = tvView.findViewById(R.id.banglaatv);
        recyclerView1 = tvView.findViewById(R.id.indiatv);
        recyclerView2 = tvView.findViewById(R.id.sportstv);
        bangladeshall = tvView.findViewById(R.id.bangladeshall);
        indiaall = tvView.findViewById(R.id.indiaall);
        sportsall = tvView.findViewById(R.id.sportsall);
        tvProg = tvView.findViewById(R.id.tvProg);
    }

//    private void loadAds(View tvView) {
//        AdView adView = tvView.findViewById(R.id.adView2);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        adView.loadAd(adRequest);
//    }

    private void setClickListeners() {
        bangladeshall.setOnClickListener(v -> startActivity(new Intent(getActivity(), BangladeshiTv.class)));
        indiaall.setOnClickListener(v -> startActivity(new Intent(getActivity(), IndianTv.class)));
        sportsall.setOnClickListener(v -> startActivity(new Intent(getActivity(), SportsTv.class)));
    }

    private void initializeChannelsLists() {
        banglaChannels = new ArrayList<>();
        banglaChannels2 = new ArrayList<>();
        banglaChannels3 = new ArrayList<>();
    }

    private void loadChannelsData() {
        extractBanglaChannels();
        extractIndianChannel();
        extractSportsChannel();
    }

    private void extractSportsChannel() {
        fetchChannels(sportsJson, banglaChannels3, recyclerView2);
    }

    private void extractIndianChannel() {
        fetchChannels(indiaJson, banglaChannels2, recyclerView1);
    }

    private void extractBanglaChannels() {
        fetchChannels(banglaJson, banglaChannels, recyclerView);
    }

    private void fetchChannels(String url, List<BanglaChannel> channelList, RecyclerView recyclerView) {
        tvProg.setVisibility(View.VISIBLE); // Show progress bar while loading

        RequestQueue queue = Volley.newRequestQueue(getContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                jsonArray -> {
                    tvProg.setVisibility(View.GONE); // Hide progress bar after loading
                    parseChannelData(jsonArray, channelList, recyclerView);
                }, volleyError -> {
            tvProg.setVisibility(View.GONE); // Hide progress bar if error occurs
            Log.e("VolleyError", "Error fetching channels: " + volleyError.getMessage());
        });

        queue.add(jsonArrayRequest);
    }

    private void parseChannelData(JSONArray jsonArray, List<BanglaChannel> channelList, RecyclerView recyclerView) {
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject channelObject = jsonArray.getJSONObject(i);
                BanglaChannel banglaChannel = new BanglaChannel();
                banglaChannel.setChannel_name(channelObject.getString("channel_name"));
                banglaChannel.setChannel_logo(channelObject.getString("channel_logo"));
                banglaChannel.setChannel_url(channelObject.getString("channel_url"));
                channelList.add(banglaChannel);
            } catch (JSONException e) {
                Log.e("JsonParseError", "Error parsing JSON data: " + e.getMessage());
            }
        }
        setUpRecyclerView(recyclerView, channelList);
    }

    private void setUpRecyclerView(RecyclerView recyclerView, List<BanglaChannel> channelList) {
        if (getActivity() != null) { // Check if activity is still valid
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
            adapter = new BanglaAllAdapter(getActivity(), channelList);
            recyclerView.setAdapter(adapter);
        } else {
            Log.e("TvFragment", "Activity is null, cannot set up RecyclerView");
        }
    }
}
