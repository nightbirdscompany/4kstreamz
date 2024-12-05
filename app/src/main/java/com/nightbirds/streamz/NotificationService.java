package com.nightbirds.streamz;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NotificationService extends Service {
    private static final String TAG = "NotificationService";
    private static final String SERVER_URL = "https://nightbirdscompany.github.io/4kstreamzdata/app/json/notification.json";
    private final OkHttpClient client = new OkHttpClient();
    private final Handler handler = new Handler();
    private final int POLLING_INTERVAL = 60000; // 1 minute

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler.post(fetchNotifications);
        return START_STICKY;
    }

    private final Runnable fetchNotifications = new Runnable() {
        @Override
        public void run() {
            Request request = new Request.Builder().url(SERVER_URL).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(TAG, "Failed to fetch notifications", e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String jsonData = response.body().string();
                        parseNotifications(jsonData);
                    } else {
                        Log.e(TAG, "Response failed with code: " + response.code());
                    }
                }
            });
            handler.postDelayed(this, POLLING_INTERVAL);
        }
    };

    private void parseNotifications(String jsonData) {
        try {
            // Parse the root JSON object
            JSONObject rootObject = new JSONObject(jsonData);
            JSONArray notifications = rootObject.getJSONArray("notifications");

            for (int i = 0; i < notifications.length(); i++) {
                JSONObject notification = notifications.getJSONObject(i);
                String title = notification.getString("title");
                String message = notification.getString("message");
                Log.d(TAG, "Notification received: " + title + " - " + message);
                // TODO: Display the notification using NotificationManager
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to parse notifications", e);
        }
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
