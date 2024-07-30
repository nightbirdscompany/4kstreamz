package com.nightbirds.streamz;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionParameters;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.StyledPlayerView;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;


public class PlayerActivity extends AppCompatActivity {

    public static String videoUrl;

    StyledPlayerView exoPlayer;
    ExoPlayer eplayer;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        //hide notch and fill full screen
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }
        setContentView(R.layout.activity_player);

        exoPlayer =findViewById(R.id.exoPlayer);


    }

    public void intilizeplayer (){

        eplayer = new ExoPlayer.Builder(this).build();
        exoPlayer.setPlayer(eplayer);

        exoPlayer.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);

        MediaItem mediaItem = MediaItem.fromUri(videoUrl);

        eplayer.setMediaItem(mediaItem);
        eplayer.prepare();
        eplayer.play();

    }

    @Override
    protected void onStart() {
        super.onStart();

        intilizeplayer();
    }

    @Override
    protected void onPause() {

        if (eplayer == null){

            intilizeplayer();

        }
        super.onPause();
    }


    @Override
    protected void onResume() {

        if (eplayer == null){

            intilizeplayer();

        }
        super.onResume();
    }

    @Override

    public void onDestroy(){


        eplayer.release();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (eplayer.isPlaying()){
            eplayer.stop();
            finish();

        }else {

        }
    }





}