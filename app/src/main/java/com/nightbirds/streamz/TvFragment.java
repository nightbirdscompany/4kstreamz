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
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class TvFragment extends Fragment {

    RecyclerView recyclerView;
    List<BanglaChannel> banglaChannels;

    private static String banglaJson = "https://developerhasan4.github.io/tsports/video/channel.json";
    BanglaAllAdapter adapter;

    TextView bangladeshall, indiaall, sportsall;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View tvView = inflater.inflate(R.layout.fragment_tv, container, false);

        //======== for see all button

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

        //================= see all button end

        //================= for bangla recycler view

        recyclerView = tvView.findViewById(R.id.banglaatv);

        banglaChannels = new ArrayList<>();
        extractBanglaChannels();

        return tvView;

    }

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