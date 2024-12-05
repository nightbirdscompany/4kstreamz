package com.nightbirds.streamz;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.ProcessLifecycleOwner;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.appopen.AppOpenAd;
import org.json.JSONException;
import org.json.JSONObject;

public class MyApplication extends Application implements Application.ActivityLifecycleCallbacks, LifecycleObserver {
    private AppOpenAd appOpenAd = null;
    private Activity currentActivity;
    private int appOpenCount = 0;  // Counter for app opens
    private static int AD_DISPLAY_INTERVAL = 3;  // Default interval if JSON load fails

    @Override
    public void onCreate() {
        super.onCreate();
        MobileAds.initialize(this, initializationStatus -> {});
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        registerActivityLifecycleCallbacks(this);

        fetchAdDisplayInterval();  // Fetch the interval from JSON server
        loadAppOpenAd();           // Load the first ad
    }

    private void fetchAdDisplayInterval() {
        String url = "https://nightbirdscompany.github.io/4kstreamzdata/app/json/appopenad.json";  // Replace with your JSON server URL

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        AD_DISPLAY_INTERVAL = response.getInt("AD_DISPLAY_INTERVAL");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    // Handle any errors here, e.g., log the error or set a fallback interval
                    error.printStackTrace();
                }
        );

        // Add the request to your request queue
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);
    }

    private void loadAppOpenAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        AppOpenAd.load(
                this,
                "ca-app-pub-7944048926495091/1525603228", // Replace with your actual App Open Ad unit ID
                adRequest,
                AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
                new AppOpenAd.AppOpenAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull AppOpenAd ad) {
                        appOpenAd = ad;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        appOpenAd = null;
                    }
                });
    }

    public void showAdIfAvailable() {
        if (appOpenAd != null && currentActivity != null) {
            appOpenAd.show(currentActivity);
            appOpenAd = null;  // Ensure the ad is only shown once
            loadAppOpenAd();   // Load a new ad for next time
        } else {
            loadAppOpenAd(); // If ad isn't available, load another one
        }
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        currentActivity = activity;
    }

    @Override
    public void onActivityStarted(Activity activity) {
        currentActivity = activity;

        // Increment app open count and check if it's time to show the ad
        appOpenCount++;
        if (appOpenCount >= AD_DISPLAY_INTERVAL) {
            showAdIfAvailable();  // Show the ad
            appOpenCount = 0;     // Reset the counter after showing the ad
        }
    }

    // Other lifecycle methods for ActivityLifecycleCallbacks
    @Override public void onActivityResumed(Activity activity) { currentActivity = activity; }
    @Override public void onActivityPaused(Activity activity) { }
    @Override public void onActivityStopped(Activity activity) { }
    @Override public void onActivitySaveInstanceState(Activity activity, Bundle outState) { }
    @Override public void onActivityDestroyed(Activity activity) { }
}
