package com.nightbirds.streamz;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class MyBackgroundService extends Service {

    private static final String CHANNEL_ID = "MyForegroundServiceChannel";
    private static final String CHANNEL_NAME = "Background Task";
    private static final int NOTIFICATION_ID = 1;

    private static final String PREF_NAME = "AppPreferences";
    private static final String PREF_KEY_NOTIFICATION_SHOWN = "NotificationShown";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("MyBackgroundService", "Service created");

        // Create notification channel if running on Android 8.0 and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("MyBackgroundService", "Service started");

        // Create and show the notification immediately to avoid RemoteServiceException
        Notification notification = createNotification();
        startForeground(NOTIFICATION_ID, notification);

        // Check if the notification has already been shown
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        boolean isNotificationShown = sharedPreferences.getBoolean(PREF_KEY_NOTIFICATION_SHOWN, false);

        if (!isNotificationShown) {
            // Mark the notification as shown
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(PREF_KEY_NOTIFICATION_SHOWN, true);
            editor.apply();
        }

        // Simulate a background task (you can replace this with real work)
        new Thread(() -> {
            try {
                for (int i = 0; i < 100; i++) {
                    Log.d("MyBackgroundService", "Task running in background: " + i);
                    Thread.sleep(1000); // Simulate a long-running task
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                stopForeground(true); // Stop foreground status when the task is done
                stopSelf(); // Stop the service
            }
        }).start();

        return START_STICKY; // Service will restart if killed by the system
    }


    private Notification createNotification() {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("4K Streamz Background Task")
                .setContentText("Download tasks are running in the background.")
                .setSmallIcon(R.drawable.noti_ic) // Replace with your notification icon
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null; // No binding to UI, we are only running tasks in the background
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("MyBackgroundService", "Service destroyed");
    }
}
