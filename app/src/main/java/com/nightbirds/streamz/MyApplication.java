package com.nightbirds.streamz;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
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
    private boolean isAdIntervalFetched = false; // Flag to indicate if the interval is fetched

    @Override
    public void onCreate() {
        super.onCreate();

        // Start background service
        Intent serviceIntent = new Intent(this, MyBackgroundService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ContextCompat.startForegroundService(this, serviceIntent);
        } else {
            startService(serviceIntent);
        }


        // Initialize AdMob
        MobileAds.initialize(this, initializationStatus -> {});

        // Register lifecycle observer
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);

        // Register activity lifecycle callbacks
        registerActivityLifecycleCallbacks(this);

        // Fetch the ad display interval from the server
        fetchAdDisplayInterval();

        // Load the first app open ad
        loadAppOpenAd();

        // Request notification permission if required for Android 14+
        requestPostNotificationsPermission();
    }

    private void fetchAdDisplayInterval() {
        String url = "https://nightbirdscompany.github.io/4kstreamzdata/app/json/appopenad.json";  // Replace with your JSON server URL

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        AD_DISPLAY_INTERVAL = response.getInt("AD_DISPLAY_INTERVAL");
                        isAdIntervalFetched = true;  // Mark as fetched
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    // Handle any errors here, e.g., log the error or set a fallback interval
                    error.printStackTrace();
                    isAdIntervalFetched = true;  // Mark as fetched to avoid further fetching
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
        // Make sure the ad interval is fetched before showing the ad
        if (!isAdIntervalFetched) {
            return;
        }

        if (appOpenAd != null && currentActivity != null) {
            appOpenAd.show(currentActivity);
            appOpenAd = null;  // Ensure the ad is only shown once
            loadAppOpenAd();   // Load a new ad for the next time
        } else {
            loadAppOpenAd(); // If the ad isn't available, load another one
        }
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        currentActivity = activity;
    }

    @Override
    public void onActivityStarted(Activity activity) {
        currentActivity = activity;

        // Increment app open count only when the app is launched from a cold start
        if (activity.isTaskRoot()) {
            appOpenCount++;
        }

        // Check if it's time to show the ad
        if (appOpenCount >= AD_DISPLAY_INTERVAL) {
            showAdIfAvailable();  // Show the ad
            appOpenCount = 0;     // Reset the counter after showing the ad
        }
    }

    @Override public void onActivityResumed(Activity activity) { currentActivity = activity; }
    @Override public void onActivityPaused(Activity activity) { }
    @Override public void onActivityStopped(Activity activity) { }
    @Override public void onActivitySaveInstanceState(Activity activity, Bundle outState) { }
    @Override public void onActivityDestroyed(Activity activity) { }

    private void requestPostNotificationsPermission() {
        // Check if the app is running on Android 14 (API level 30) or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Request permission in MainActivity
            if (currentActivity instanceof MainActivity) {
                ((MainActivity) currentActivity).requestPostNotificationsPermission();
            }
        }
    }
}
