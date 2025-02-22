package com.nightbirds.streamz;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.ads.MobileAds;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.BuildConfig;

import android.Manifest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;




public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomnavigationview;
    DrawerLayout drawlay;
    MaterialToolbar toolbar;
    FrameLayout framlay;
    NavigationView navview;
    View headerview;
    TextView headername, headeremail;

    private static final int REQUEST_CODE_PERMISSION = 100;
    private static final int PERMISSION_REQUEST_CODE = 1;




    private static final int REQUEST_INSTALL_UNKNOWN_APPS = 2;
    private static final int REQUEST_PERMISSION_WRITE_STORAGE = 1;
    private static final String APK_FILE_NAME = "update.apk";


    //  MeowBottomNavigation meownav;

    private Handler handler = new Handler();

    // Runnable that performs the proxy/VPN check
    private Runnable checkProxyRunnable = new Runnable() {
        @Override
        public void run() {
            if (isProxyUsed() || isVpnUsed()) {
                // If a proxy or VPN is detected, block the user
                showProxyDetectedDialog();
            } else {
                // If no proxy or VPN is detected, continue checking
                handler.postDelayed(this, 5000); // Check every 5 seconds
            }
        }
    };



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        startService(new Intent(this, NotificationService.class));

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(android.R.color.black));
        }

        //============== finde view by id  start

        drawlay = findViewById(R.id.drawlay);
        toolbar =findViewById(R.id.toolbar);
        framlay =findViewById(R.id.framlay);
        navview = findViewById(R.id.navview);
        bottomnavigationview = findViewById(R.id.bottomnavigationview);
        headerview = navview.getHeaderView(0);
        headername = headerview.findViewById(R.id.headertext);
        headeremail = headerview.findViewById(R.id.headeremail);

        new Thread(
                () -> {
                    // Initialize the Google Mobile Ads SDK on a background thread.
                    MobileAds.initialize(this, initializationStatus -> {});
                })
                .start();



        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.framlay, new HomeFragment());
        fragmentTransaction.commit();




        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                MainActivity.this, drawlay, toolbar, R.string.drawer_close, R.string.drawer_open);

        drawlay.addDrawerListener(toggle);


        //=========== Toolbar Start

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (item.getItemId()==R.id.search){

                    Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                    startActivity(intent);

                }
                else if (item.getItemId()==R.id.download){

                    Intent intent = new Intent(MainActivity.this, DownloadActivity.class);
                    startActivity(intent);

                }
                else if (item.getItemId()==R.id.noti){


                    // Reset badge count
                    resetNotificationCount();
                    Intent intent = new Intent(MainActivity.this, NotificationsActivity.class);
                    startActivity(intent);


                }

                return false;
            }
        }); //=========== Toolbar End

        //================================ drawer navigation start
        navview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if (menuItem.getItemId()==R.id.nav_home){
                    Toast.makeText(MainActivity.this, "yeh working", Toast.LENGTH_LONG).show();
                    drawlay.closeDrawer(GravityCompat.START);
                }

                else   if (menuItem.getItemId()==R.id.stream_url){
                    Intent intent = new Intent(MainActivity.this, NetworkStreming.class);
                    startActivity(intent);
                    drawlay.closeDrawer(GravityCompat.START);
                }

                else   if (menuItem.getItemId()==R.id.share){

                    Intent intent = new Intent( Intent.ACTION_SEND);
                    intent.setType("Text/Plain");
                    String shareLink = "http://4kstreamz.free.nf/";
                    String shareSubject = "Enjoy Live Tv And Movie App";


                    intent.putExtra(Intent.EXTRA_TEXT,shareLink);
                    intent.putExtra(Intent.EXTRA_SUBJECT,shareSubject);
                    startActivity(Intent.createChooser( intent,"Enjoy Live Tv And Movie App"));

                    drawlay.closeDrawer(GravityCompat.START);
                }

                else   if (menuItem.getItemId()==R.id.fb_page){
                   openFacebookPage("https://www.facebook.com/nightbirdscompany/");
                    drawlay.closeDrawer(GravityCompat.START);
                    return true;
                }

                else   if (menuItem.getItemId()==R.id.yt_channel){
                    openYouTubeChannel("https://www.youtube.com/@nightbirdsofficial");
                    drawlay.closeDrawer(GravityCompat.START);
                    return true;
                } //else if (menuItem.getItemId()==R.id.tele_channel) {
//
//                    openTelegramChannel("https://t.me/c4kstreamz");
//                    drawlay.closeDrawer(GravityCompat.START);
//                    return true;
//             }
               else   if (menuItem.getItemId()==R.id.settings){
                    Toast.makeText(MainActivity.this, "yeh working", Toast.LENGTH_LONG).show();
                    drawlay.closeDrawer(GravityCompat.START);
                }
               else if (menuItem.getItemId()==R.id.developerInfo) {

                   Intent intent = new Intent(MainActivity.this, AboutDeveloper.class);
                   startActivity(intent);

                }
               else if (menuItem.getItemId()==R.id.help) {
                    sendSupportEmail();
                }

                else   if (menuItem.getItemId()==R.id.exit){
                    new AlertDialog.Builder(MainActivity.this )
                            .setTitle("EXIT")
                            .setMessage("ARE YOU SURE EXIT THIS APP")
                            .setIcon(R.drawable.exit)
                            .setNegativeButton("NO THANKS", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })

                            .setPositiveButton("EXIT", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    finishAndRemoveTask();
                                }
                            })

                            .show();

                    drawlay.closeDrawer(GravityCompat.START);
                }
                return true;
            }
        });
        //================================ drawer navigation end

        //======== bottom navigation view

        bottomnavigationview.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if (menuItem.getItemId()==R.id.bottom_nav_home){
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.framlay, new HomeFragment());
                    fragmentTransaction.commit();
                }

                else  if (menuItem.getItemId()==R.id.bottom_nav_tv){
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.framlay, new TvFragment());
                    fragmentTransaction.commit();
                }

                else  if (menuItem.getItemId()==R.id.bottom_nav_movie){
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.framlay, new MovieFragment());
                    fragmentTransaction.commit();
                }

                return true;
            }
        });

        // Add focus change listener for bottom navigation items
        for (int i = 0; i < bottomnavigationview.getMenu().size(); i++) {
            MenuItem menuItem = bottomnavigationview.getMenu().getItem(i);
            View itemView = bottomnavigationview.findViewById(menuItem.getItemId());

            if (itemView != null) {
                itemView.setOnFocusChangeListener((v, hasFocus) -> {
                    if (hasFocus) {
                        v.setBackgroundResource(R.drawable.tv_control); // Highlight background
                    } else {
                        v.setBackgroundResource(android.R.color.transparent); // Reset background
                    }
                });
            }
        }

        ColorStateList colorStateList = ContextCompat.getColorStateList(this, R.color.bottom_nav_color);
        bottomnavigationview.setItemIconTintList(colorStateList);

        ColorStateList rippleColor = ContextCompat.getColorStateList(this, R.color.bottom_nav_ripple_color);
        bottomnavigationview.setItemRippleColor(rippleColor);


        //======= for update noti


       checkForUpdate();

       // ========== for proxy detected

        handler.post(checkProxyRunnable);



        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Request permission if it's not granted
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
        }

        // Check and request permission on Android 14 and above
        requestPostNotificationsPermission();

        loadNotificationCount();

    }// ============== on create end

    //============== for noti
    private void loadNotificationCount() {
        SharedPreferences preferences = getSharedPreferences("NotificationPrefs", MODE_PRIVATE);
        int notificationCount = preferences.getInt("NotificationCount", 0);
        updateNotificationBadge(notificationCount);
    }

    private void resetNotificationCount() {
        SharedPreferences preferences = getSharedPreferences("NotificationPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("NotificationCount", 0);  // Reset the notification count
        editor.apply();

        updateNotificationBadge(0);  // Update badge with 0 notifications
    }

    private void updateNotificationBadge(int count) {
        MenuItem menuItem = toolbar.getMenu().findItem(R.id.noti);
        if (menuItem != null) {
            View actionView = menuItem.getActionView();
            if (actionView != null) {
                TextView badge = actionView.findViewById(R.id.badge);
                if (badge != null) {
                    if (count > 0) {
                        badge.setText(String.valueOf(count));
                        badge.setVisibility(View.VISIBLE);
                    } else {
                        badge.setVisibility(View.GONE);
                    }
                }
            }
        }
    }

    private void addNewNotification(String id, String title, String message) {
        SharedPreferences preferences = getSharedPreferences("NotificationPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        // Store the new notification
        String jsonString = preferences.getString("StoredNotifications", "[]");
        try {
            JSONArray notificationsArray = new JSONArray(jsonString);
            JSONObject newNotification = new JSONObject();
            newNotification.put("id", id);
            newNotification.put("title", title);
            newNotification.put("message", message);
            notificationsArray.put(newNotification);

            editor.putString("StoredNotifications", notificationsArray.toString());

            // Increment notification count and update badge
            int currentCount = preferences.getInt("NotificationCount", 0);
            editor.putInt("NotificationCount", currentCount + 1);
            editor.apply();

            // Update badge to reflect new notification count
            updateNotificationBadge(currentCount + 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }





    //========== for support email

    private void sendSupportEmail() {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");  // This sets the type to email

        // Set the recipient email address, subject, and optional body text
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"nightbirdscompany@gmail.com"});  // Replace with your support email
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Support Request");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Hi, I need help with...");  // Optional, you can pre-fill body text if needed

        // Ensure Gmail is chosen as the preferred app
        emailIntent.setPackage("com.google.android.gm");  // Target Gmail directly

        try {
            startActivity(emailIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            // If Gmail app is not installed
            Toast.makeText(this, "Gmail is not installed", Toast.LENGTH_SHORT).show();
        }
    }

    //============== for intent telegram

    private void openTelegramChannel(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.setPackage("org.telegram.messenger");
        try {
            startActivity(intent);
        } catch (Exception e) {
            // Telegram app not installed, open in browser
            intent.setPackage(null);
            startActivity(intent);
        }
    }

    private void openYouTubeChannel(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.setPackage("com.google.android.youtube");
        try {
            startActivity(intent);
        } catch (Exception e) {
            // YouTube app not installed, open in browser
            intent.setPackage(null);
            startActivity(intent);
        }
    }

    private void openFacebookPage(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.setPackage("com.facebook.katana");
        try {
            startActivity(intent);
        } catch (Exception e) {
            // Facebook app not installed, open in browser
            intent.setPackage(null);
            startActivity(intent);
        }
    }// =======for intent telegram end

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(checkProxyRunnable); // Stop the handler to prevent memory leaks
    }

    // Method to detect proxy usage
    public boolean isProxyUsed() {
        String proxyAddress = System.getProperty("http.proxyHost");
        String proxyPort = System.getProperty("http.proxyPort");

        return proxyAddress != null && proxyPort != null;
    }

    // Method to detect VPN usage
    public boolean isVpnUsed() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
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

    // Method to show a dialog if proxy or VPN is detected
    private void showProxyDetectedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Proxy or VPN Detected")
                .setMessage("Please disable proxy or VPN to use this app.")
                .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish(); // Close the app
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.setCancelable(false); // Prevent back button dismiss
        dialog.setCanceledOnTouchOutside(false); // Prevent outside touch dismiss
        dialog.show();
    }
    //========= proxy detected end

    //================================ fragment start

    //=============== exit alert


    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        new AlertDialog.Builder(MainActivity.this )
                .setTitle("EXIT")
                .setMessage("ARE YOU SURE EXIT THIS APP")
                .setIcon(R.drawable.exit)
                .setNegativeButton("NO THANKS", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })

                .setPositiveButton("EXIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
//                        finishAndRemoveTask();
                        moveTaskToBack(true);
                    }
                })

                .show();
    }

    //================= exit alert end


    private void checkForUpdate() {
    String url = "https://nightbirdscompany.github.io/4kstreamzdata/app/update.json"; // Replace with your API URL

    OkHttpClient client = new OkHttpClient();
    Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            runOnUiThread(() -> Toast.makeText(MainActivity.this, "Failed to check for updates", Toast.LENGTH_SHORT).show());
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            if (response.isSuccessful()) {
                String responseData = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(responseData);
                    int latestVersionCode = jsonObject.getInt("versionCode");
                    String apkUrl = jsonObject.getString("apkUrl");

                    if (latestVersionCode > 11) {
                        runOnUiThread(() -> showUpdateDialog(apkUrl));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    });
}

    private void showUpdateDialog(String apkUrl) {
         AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                builder2.setTitle("New Update Available")
                .setMessage("A new version of the app is available. Would you like to update now?")
                .setPositiveButton("Update", (dialog, which) -> downloadApk(apkUrl));
        AlertDialog dialog = builder2.create();
        dialog.setCancelable(false); // Prevent back button dismiss
        dialog.setCanceledOnTouchOutside(false); // Prevent outside touch dismiss
        dialog.show();

    }




    private void downloadApk(String apkUrl) {
        new DownloadApkTask().execute(apkUrl);
    }

    private class DownloadApkTask extends AsyncTask<String, Integer, File> {
        @Override
        protected File doInBackground(String... params) {
            String apkUrl = params[0];
            File file = new File(getExternalFilesDir(null), "update.apk");

            try {
                URL url = new URL(apkUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream inputStream = connection.getInputStream();
                FileOutputStream outputStream = new FileOutputStream(file);

                byte[] buffer = new byte[1024];
                int len;
                while ((len = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, len);
                }

                outputStream.close();
                inputStream.close();

                return file;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(File file) {
            if (file != null) {
                installApk(file);
            } else {
                Toast.makeText(MainActivity.this, "Download failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void installApk(File file) {
        Uri apkUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", file);

        Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
        intent.setData(apkUri);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);
    }

    private static final int REQUEST_PERMISSIONS = 1;

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSIONS);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !getPackageManager().canRequestPackageInstalls()) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, REQUEST_PERMISSIONS);
        } else {
            checkForUpdate();
        }
    }

    public void requestPostNotificationsPermission() {
        // Check if the app is running on Android 14 (API level 30) or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Check if the permission is already granted
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                // Request permission
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, PERMISSION_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with accessing external storage or any other logic
                // You can put your logic to handle external storage access here
            } else {
                // Permission denied, handle appropriately (e.g., show a message to the user)
//                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                // Optionally, you can provide more info about why the permission is needed
            }
        } else if (requestCode == REQUEST_PERMISSIONS) {
            // Handle your other permission requests (if applicable)
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkForUpdate();
            } else {
                //Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        } else if  (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with notifications


            } else {
                // Permission denied, handle accordingly
            }
        }

        }
    }

