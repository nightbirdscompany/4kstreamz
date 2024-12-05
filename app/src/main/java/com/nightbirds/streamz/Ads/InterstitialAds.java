package com.nightbirds.streamz.Ads;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class InterstitialAds {

//    private static final String TAG = "InterstitialAds";  // Declare TAG for logging
//    public static InterstitialAd mInterstitialAd;
//
//    // Method to load interstitial ads
//    public static void loadAds(Activity activity) {
//
//        // Initialize Mobile Ads SDK
//        MobileAds.initialize(activity, new OnInitializationCompleteListener() {
//            @Override
//            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
//                Log.d(TAG, "MobileAds initialized.");
//            }
//        });
//
//        // Create ad request
//        AdRequest adRequest = new AdRequest.Builder().build();
//
//        // Load interstitial ad
//        InterstitialAd.load(activity, "ca-app-pub-7944048926495091/6883409135", adRequest,  // Replace with test ad unit ID during development
//                new InterstitialAdLoadCallback() {
//                    @Override
//                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
//                        mInterstitialAd = interstitialAd;  // Set the loaded ad
//                        Log.i(TAG, "Interstitial ad loaded.");
//                    }
//
//                    @Override
//                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
//                        // Handle ad load failure
//                        Log.e(TAG, "Failed to load interstitial ad: " + loadAdError.getMessage());
//                        mInterstitialAd = null;
//                    }
//                });
//    }
}
