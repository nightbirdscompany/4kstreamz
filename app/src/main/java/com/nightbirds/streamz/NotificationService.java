package com.nightbirds.streamz;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NotificationService extends Service {
    private static final String TAG = "NotificationService";
    private static final String SERVER_URL = "https://nightbirdscompany.github.io/4kstreamzdata/app/json/notification.json";
    private static final String CHANNEL_ID = "NotificationServiceChannel";
    private static final String PREFS_NAME = "NotificationPrefs";
    private static final String PREFS_KEY = "FetchedNotificationIds";

    private final OkHttpClient client = new OkHttpClient();
    private final Handler handler = new Handler();
    private final int POLLING_INTERVAL = 60000; // 1 minute

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel(); // Create the notification channel
        startForegroundService(); // Make this a foreground service
        handler.post(fetchNotifications);
        return START_STICKY;
    }

    private void startForegroundService() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("4K Streamz")
                .setContentText("Running Background")
                .setSmallIcon(R.drawable.iconai) // Your app's notification icon
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();

        startForeground(1, notification);
    }

    private final Runnable fetchNotifications = new Runnable() {
        @Override
        public void run() {
            Request request = new Request.Builder().url(SERVER_URL).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.e(TAG, "Failed to fetch notifications", e);
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful() && response.body() != null) {
                        String jsonData = response.body().string();
                        parseAndNotify(jsonData);
                    } else {
                        Log.e(TAG, "Response failed with code: " + response.code());
                    }
                }
            });
            handler.postDelayed(this, POLLING_INTERVAL); // Re-run after the polling interval
        }
    };

    private void parseAndNotify(String jsonData) {
        try {
            JSONObject rootObject = new JSONObject(jsonData);
            JSONArray notifications = rootObject.getJSONArray("notifications");

            // Load stored notification IDs
            Set<String> storedNotificationIds = getStoredNotificationIds();

            for (int i = 0; i < notifications.length(); i++) {
                JSONObject notification = notifications.getJSONObject(i);
                String id = notification.getString("id"); // Unique identifier for the notification
                String title = notification.getString("title");
                String message = notification.getString("message");

                // Check if the notification has already been fetched
                if (!storedNotificationIds.contains(id)) {
                    storedNotificationIds.add(id); // Add to the set
                    saveNotificationIds(storedNotificationIds); // Save the updated set

                    // Save the new notification to storage
                    saveNotificationToStorage(id, title, message);

                    // Show new notification
                    showNotification(title, message);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to parse notifications", e);
        }
    }

    private void saveNotificationToStorage(String id, String title, String message) {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        JSONArray storedNotifications;
        try {
            // Retrieve existing stored notifications
            String jsonString = preferences.getString("StoredNotifications", "[]");
            storedNotifications = new JSONArray(jsonString);
        } catch (Exception e) {
            storedNotifications = new JSONArray();
        }

        try {
            // Create a new notification object
            JSONObject newNotification = new JSONObject();
            newNotification.put("id", id);
            newNotification.put("title", title);
            newNotification.put("message", message);

            // Add the new notification to the array
            storedNotifications.put(newNotification);

            // Save the updated array back to SharedPreferences
            editor.putString("StoredNotifications", storedNotifications.toString());
            editor.apply();
        } catch (Exception e) {
            Log.e(TAG, "Failed to save notification to storage", e);
        }
    }

    private void showNotification(String title, String message) {
        Intent intent = new Intent(this, MainActivity.class); // Change to your app's main activity
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.iconai) // Add your notification icon here
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify((int) System.currentTimeMillis(), builder.build());
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Notification Service",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Channel for background notifications");
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    private Set<String> getStoredNotificationIds() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return preferences.getStringSet(PREFS_KEY, new HashSet<>());
    }

    private void saveNotificationIds(Set<String> notificationIds) {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putStringSet(PREFS_KEY, notificationIds);
        editor.apply();
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(fetchNotifications);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
