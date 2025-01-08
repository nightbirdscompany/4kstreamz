package com.nightbirds.streamz;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.PictureInPictureParams;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Rational;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.Player;
import androidx.media3.common.util.Log;
import androidx.media3.datasource.DefaultDataSourceFactory;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.hls.HlsMediaSource;
import androidx.media3.ui.PlayerView;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ThreeBounce;
import java.io.File;

public class PlayerActivity extends AppCompatActivity {

    public static String videoUrl;
    public static String playerTitle;

    ImageView back_button, forward_button, backward_button, play_pause_button, fullscreen_button, setting_button;
    TextView exo_title, errorPlay;
    PlayerView playerView;
    ExoPlayer exoPlayer;

    boolean isFullScreen = false;

    private boolean isShowingTrackSelectionDialog;
    private static final int MAX_RETRY = 100; // Max retry attempts
    private int retryCount = 0; // Current retry count

    // For proxy
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
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // Hide notch and fill full screen
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }
        setContentView(R.layout.activity_player);

        // For player
        playerView = findViewById(R.id.exoPlayer);
        exoPlayer = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(exoPlayer);

        Intent intent = getIntent();
        if (Intent.ACTION_VIEW.equals(intent.getAction()) || Intent.ACTION_SEND.equals(intent.getAction())) {
            Uri videoUri = intent.getData();
            if (videoUri != null) {
                videoUrl = videoUri.toString();
                playerTitle = getVideoTitle(videoUri);
                playVideo(videoUri);
            }
        }

        MediaItem mediaItem;
        if (isLocalFile(videoUrl)) {
            mediaItem = MediaItem.fromUri(Uri.fromFile(new File(videoUrl)));
        } else {
            mediaItem = MediaItem.fromUri(videoUrl);
        }
        exoPlayer.setMediaItem(mediaItem);
        exoPlayer.prepare();
        exoPlayer.setPlayWhenReady(true);
        exoPlayer.play();

        ProgressBar progressBar = findViewById(R.id.spin_kit);
        Sprite threeBounce = new ThreeBounce();
        progressBar.setIndeterminateDrawable(threeBounce);

        back_button = playerView.findViewById(R.id.backExo);
        forward_button = playerView.findViewById(R.id.fwd);
        backward_button = playerView.findViewById(R.id.rew);
        play_pause_button = playerView.findViewById(R.id.play_pause);
        fullscreen_button = playerView.findViewById(R.id.exo_full);
        setting_button = playerView.findViewById(R.id.exo_quality_setting);
        exo_title = playerView.findViewById(R.id.exoTitle);
        errorPlay = playerView.findViewById(R.id.errorPlay);

        exo_title.setText(playerTitle);
        exo_title.setSelected(true);

        exoPlayer.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int state) {
                if (state == Player.STATE_READY) {
                    progressBar.setVisibility(View.GONE);
                    play_pause_button.setVisibility(View.VISIBLE);
                    errorPlay.setVisibility(View.GONE);
                } else if (state == Player.STATE_BUFFERING) {
                    progressBar.setVisibility(View.VISIBLE);
                    play_pause_button.setVisibility(View.INVISIBLE);
                    errorPlay.setVisibility(View.GONE);
                } else if (state == Player.STATE_ENDED) {
                    handlePlaybackEnd();
                }
            }

            @Override
            public void onPlayerError(PlaybackException error) {
                handlePlaybackError();
            }
        });

        setting_button.setOnClickListener(v -> {
            if (!isShowingTrackSelectionDialog && TrackSelectionDialog.willHaveContent(exoPlayer)) {
                isShowingTrackSelectionDialog = true;
                TrackSelectionDialog trackSelectionDialog =
                        TrackSelectionDialog.createForPlayer(
                                exoPlayer,
                                dismissedDialog -> isShowingTrackSelectionDialog = false);
                trackSelectionDialog.show(getSupportFragmentManager(), null);
            }
        });

        play_pause_button.setOnClickListener(v -> {
            exoPlayer.setPlayWhenReady(!exoPlayer.getPlayWhenReady());
            play_pause_button.setImageResource(Boolean.TRUE.equals(exoPlayer.getPlayWhenReady()) ? R.drawable.pause : R.drawable.play);
        });

        forward_button.setOnClickListener(v -> exoPlayer.seekTo(exoPlayer.getCurrentPosition() + 10000));

        backward_button.setOnClickListener(v -> {
            long num = exoPlayer.getCurrentPosition() - 10000;
            exoPlayer.seekTo(Math.max(num, 0));
        });

        back_button.setOnClickListener(v -> finish());

        fullscreen_button.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                enterPictureInPictureMode(new PictureInPictureParams.Builder()
                        .setAspectRatio(new Rational(16, 9))
                        .build());
            }
        });

        handler.post(checkProxyRunnable);
    }

    public boolean isProxyUsed() {
        String proxyAddress = System.getProperty("http.proxyHost");
        String proxyPort = System.getProperty("http.proxyPort");
        return proxyAddress != null && proxyPort != null;
    }

    public boolean isVpnUsed() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if (cm != null) {
            Network[] networks = cm.getAllNetworks();
            for (Network network : networks) {
                NetworkCapabilities capabilities = cm.getNetworkCapabilities(network);
                if (capabilities != null && capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void showProxyDetectedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Proxy or VPN Detected")
                .setMessage("Please disable proxy or VPN to use this app.")
                .setPositiveButton("Exit", (dialog, id) -> finish());

        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void playVideo(Uri videoUri) {
        MediaItem mediaItem = MediaItem.fromUri(videoUri);
        exoPlayer.setMediaItem(mediaItem);
        exoPlayer.prepare();
        exoPlayer.setPlayWhenReady(true);
    }

    private String getVideoTitle(Uri videoUri) {
        String fileName = videoUri.getLastPathSegment();
        return (fileName != null) ? fileName : "Unknown Title";
    }

    private void handlePlaybackEnd() {
        if (retryCount < MAX_RETRY) {
            retryCount++;
            Toast.makeText(this, "Stream stopped. Retrying... (" + retryCount + "/" + MAX_RETRY + ")", Toast.LENGTH_SHORT).show();
            refreshVideo();
        } else {
            Toast.makeText(this, "Stream stopped. Max retry attempts reached.", Toast.LENGTH_LONG).show();
            errorPlay.setVisibility(View.VISIBLE);
        }
    }

    private void handlePlaybackError() {
        if (retryCount < MAX_RETRY) {
            retryCount++;
            refreshVideo();
        } else {
            Toast.makeText(this, "Playback error. Max retry attempts reached.", Toast.LENGTH_LONG).show();
            errorPlay.setVisibility(View.VISIBLE);
        }
    }

    private void refreshVideo() {
        exoPlayer.stop();
        exoPlayer.prepare();
        exoPlayer.setPlayWhenReady(true);
    }

    private boolean isLocalFile(String url) {
        return url != null && (url.startsWith("/") || url.startsWith("file://"));
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            enterPictureInPictureMode(new PictureInPictureParams.Builder()
                    .setAspectRatio(new Rational(16, 9))
                    .build());
        }
    }

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode, Configuration newConfig) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig);
        if (isInPictureInPictureMode) {
            hideSystemUI();
            if (exoPlayer != null) {
                exoPlayer.play();
            }
        } else {
            showSystemUI();
            if (exoPlayer != null) {
                exoPlayer.pause();
            }
        }
    }

    private void hideSystemUI() {
        if (playerView != null) {
            playerView.hideController();
        }
    }

    private void showSystemUI() {
        if (playerView != null) {
            playerView.showController();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (exoPlayer != null && !isInPictureInPictureMode()) {
            exoPlayer.play();
        }
    }

    @Override
    protected void onPause() {
        if (exoPlayer != null && !isInPictureInPictureMode()) {
            exoPlayer.pause();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (exoPlayer != null) {
            exoPlayer.release();
            exoPlayer = null;
        }
        super.onDestroy();
        handler.removeCallbacks(checkProxyRunnable);
    }

    @Override
    public void onBackPressed() {
        if (isInPictureInPictureMode()) {
            if (exoPlayer != null) {
                exoPlayer.pause();
                exoPlayer.seekTo(0);
            }
            finish();
        } else {
            super.onBackPressed();
        }
    }
}
