package com.nightbirds.streamz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.startapp.sdk.adsbase.Ad;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;

public class NetworkStreming extends AppCompatActivity {

    EditText streamurl;
    Button gostream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_streming);

        streamurl = findViewById(R.id.stream_url);
        gostream = findViewById(R.id.gostream);

        StartAppSDK.init(NetworkStreming.this, "207488092", false);


        StartAppAd.showAd(NetworkStreming.this);
        StartAppAd.disableSplash();




        gostream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_url = streamurl.getText().toString();
                PlayerActivity.videoUrl = (user_url);

                if ( user_url.length() >10 ) {

                    Intent i = new Intent(NetworkStreming.this, PlayerActivity.class);
                    startActivity(i);
                }else {
                    streamurl.setError("Please Input Your Stream URL");
                    Toast.makeText(NetworkStreming.this, "Please Input Your Stream URL", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}