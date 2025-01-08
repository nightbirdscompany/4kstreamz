package com.nightbirds.streamz;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.nightbirds.streamz.Ads.InterstitialAds;

public class SplashActivity extends AppCompatActivity {

    private static int splash_screen = 3000;


//    @Override
//    protected void onResume() {
//        super.onResume();
//        // Show the App Open Ad if available
//        ((MyApplication) getApplication()).showAppOpenAdIfAvailable();
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });




        if (isProxySet() || isVpnUsed()) {
            // Show a dialog and block access
            showProxyDetectedDialog();
        } else {
            // Proceed with normal app flow
            // You can load your main content here
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                    if (networkInfo!= null && networkInfo.isConnected()) {


                        Intent serviceIntent = new Intent(SplashActivity.this, NotificationService.class);
                        startService(serviceIntent);

                        Intent i = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        Intent i = new Intent(SplashActivity.this, NoInternet.class);
                        startActivity(i);
                        finish();
                    }

                }
            },splash_screen);
        }
    }

    public boolean isProxySet() {
        String proxyAddress = System.getProperty("http.proxyHost");
        String proxyPort = System.getProperty("http.proxyPort");
        return proxyAddress != null && proxyPort != null;
    }

    // Method to detect if VPN is being used
    public boolean isVpnUsed() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            Network[] networks = cm.getAllNetworks();
            for (Network network : networks) {
                NetworkCapabilities capabilities = cm.getNetworkCapabilities(network);
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // Method to show a dialog and block access
    private void showProxyDetectedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Proxy Detected")
                .setMessage("Please disable proxy or VPN to use this app.")
                .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish(); // Close the app
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();

    }
}