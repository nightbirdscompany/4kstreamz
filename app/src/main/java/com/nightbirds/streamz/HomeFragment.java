package com.nightbirds.streamz;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

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

public class HomeFragment extends Fragment {


    RecyclerView recyclerView, recyclerView1, recyclerView2;
    ProgressBar eventProg;

    List<Event>events, events1, events2;
    private static String eventJson = "https://nightbirdscompany.github.io/4kstreamzdata/app/json/event.json";
    private static String eventJson1 = "https://nightbirdscompany.github.io/4kstreamzdata/app/json/football.json";
    private static String eventJson2 = "https://nightbirdscompany.github.io/4kstreamzdata/app/json/others.json";

    EventAdepter eventAdepter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View myView = inflater.inflate(R.layout.fragment_home, container, false);


        AdView adView = myView.findViewById(R.id.adView);

        // Create an AdRequest and load the banner
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);



        eventProg = myView.findViewById(R.id.eventProg);

        recyclerView = myView.findViewById(R.id.eventlist);// for recycler view
        recyclerView1 = myView.findViewById(R.id.footballList);
        recyclerView2 = myView.findViewById(R.id.othersList);


        events = new ArrayList<>();
        extractEvent();

        events1 = new ArrayList<>();
        extractCricket();

        events2 = new ArrayList<>();
        extractOthers();

        return myView;
    }

    private void extractOthers() {

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, eventJson2, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject eventObject = jsonArray.getJSONObject(i);

                        Event event = new Event();
                        event.setEvent_title(eventObject.getString("event_title").toString());
                        event.setEvent_poster(eventObject.getString("event_poster").toString());
                        event.setEvent_url(eventObject.getString("event_url").toString());

                        events2.add(event);
                        eventProg.setVisibility(View.GONE);


                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }

                recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                eventAdepter = new EventAdepter(getActivity(), events2);
                recyclerView2.setAdapter(eventAdepter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("tag", "onErrorResponse: " + volleyError.getMessage());
            }
        });

        queue.add(jsonArrayRequest);
    }


    private void extractCricket() {

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, eventJson, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject eventObject = jsonArray.getJSONObject(i);

                        Event event = new Event();
                        event.setEvent_title(eventObject.getString("event_title").toString());
                        event.setEvent_poster(eventObject.getString("event_poster").toString());
                        event.setEvent_url(eventObject.getString("event_url").toString());

                        events1.add(event);
                        eventProg.setVisibility(View.GONE);


                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }

                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                eventAdepter = new EventAdepter(getActivity(), events1);
                recyclerView.setAdapter(eventAdepter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("tag", "onErrorResponse: " + volleyError.getMessage());
            }
        });

        queue.add(jsonArrayRequest);
    }



    private void extractEvent() {

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, eventJson1, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject eventObject = jsonArray.getJSONObject(i);

                        Event event = new Event();
                        event.setEvent_title(eventObject.getString("event_title").toString());
                        event.setEvent_poster(eventObject.getString("event_poster").toString());
                        event.setEvent_url(eventObject.getString("event_url").toString());

                        events.add(event);
                        eventProg.setVisibility(View.GONE);


                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }

                recyclerView1.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                eventAdepter = new EventAdepter(getActivity(), events);
                recyclerView1.setAdapter(eventAdepter);
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