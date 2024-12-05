package com.nightbirds.streamz;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
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
    List<Event> events, events1, events2;
    private static String eventJson = "https://nightbirdscompany.github.io/4kstreamzdata/app/json/event.json";
    private static String eventJson1 = "https://nightbirdscompany.github.io/4kstreamzdata/app/json/football.json";
    private static String eventJson2 = "https://nightbirdscompany.github.io/4kstreamzdata/app/json/others.json";
    EventAdepter eventAdepter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_home, container, false);

//        AdView adView = myView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
//        adView.loadAd(adRequest);

        eventProg = myView.findViewById(R.id.eventProg);

        recyclerView = myView.findViewById(R.id.eventlist);   // For main event list
        recyclerView1 = myView.findViewById(R.id.footballList); // For football list
        recyclerView2 = myView.findViewById(R.id.othersList);  // For others list

        events = new ArrayList<>();
        events1 = new ArrayList<>();
        events2 = new ArrayList<>();

        extractEvent();
        extractCricket();
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
                        event.setEvent_title(eventObject.getString("event_title"));
                        event.setEvent_poster(eventObject.getString("event_poster"));
                        event.setEvent_url(eventObject.getString("event_url"));

                        events2.add(event);
                        eventProg.setVisibility(View.GONE);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if (isAdded() && getContext() != null) {
                    recyclerView2.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                    eventAdepter = new EventAdepter(getContext(), events2);
                    recyclerView2.setAdapter(eventAdepter);
                }
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
                        event.setEvent_title(eventObject.getString("event_title"));
                        event.setEvent_poster(eventObject.getString("event_poster"));
                        event.setEvent_url(eventObject.getString("event_url"));

                        events1.add(event);
                        eventProg.setVisibility(View.GONE);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if (isAdded() && getContext() != null) {
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                    eventAdepter = new EventAdepter(getContext(), events1);
                    recyclerView.setAdapter(eventAdepter);
                }
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
                        event.setEvent_title(eventObject.getString("event_title"));
                        event.setEvent_poster(eventObject.getString("event_poster"));
                        event.setEvent_url(eventObject.getString("event_url"));

                        events.add(event);
                        eventProg.setVisibility(View.GONE);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if (isAdded() && getContext() != null) {
                    recyclerView1.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                    eventAdepter = new EventAdepter(getContext(), events);
                    recyclerView1.setAdapter(eventAdepter);
                }
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
