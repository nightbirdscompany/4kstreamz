package com.nightbirds.streamz;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.media3.common.MediaItem;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ThreeBounce;

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

        MediaItem mediaItem = MediaItem.fromUri(videoUrl);
        exoPlayer.setMediaItem(mediaItem);
        exoPlayer.prepare();
        exoPlayer.setPlayWhenReady(true);
        exoPlayer.play();

        // For progress bar
        ProgressBar progressBar = findViewById(R.id.spin_kit);
        Sprite threeBounce = new ThreeBounce();
        progressBar.setIndeterminateDrawable(threeBounce);

        // For custom control
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

        setting_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isShowingTrackSelectionDialog
                        && TrackSelectionDialog.willHaveContent(exoPlayer)) {
                    isShowingTrackSelectionDialog = true;
                    TrackSelectionDialog trackSelectionDialog =
                            TrackSelectionDialog.createForPlayer(
                                    exoPlayer,
                                    /* onDismissListener= */ dismissedDialog -> isShowingTrackSelectionDialog = false);
                    trackSelectionDialog.show(getSupportFragmentManager(), /* tag= */ null);
                }
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

        fullscreen_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFullScreen) {
                    exo_title.setVisibility(View.INVISIBLE);
                    fullscreen_button.setImageDrawable(ContextCompat.getDrawable(PlayerActivity.this, R.drawable.ic_baseline_fullscreen_24));
                    if (getSupportActionBar() != null) getSupportActionBar().show();
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) playerView.getLayoutParams();
                    params.width = ConstraintLayout.LayoutParams.MATCH_PARENT;
                    params.height = (int) (200 + getApplicationContext().getResources().getDisplayMetrics().density);
                    playerView.setLayoutParams(params);
                    isFullScreen = false;
                } else {
                    exo_title.setVisibility(View.VISIBLE);
                    fullscreen_button.setImageDrawable(ContextCompat.getDrawable(PlayerActivity.this, R.drawable.baseline_fullscreen_exit_24));
                    if (getSupportActionBar() != null) getSupportActionBar().hide();
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                    ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) playerView.getLayoutParams();
                    params.width = ConstraintLayout.LayoutParams.MATCH_PARENT;
                    params.height = ConstraintLayout.LayoutParams.MATCH_PARENT;
                    isFullScreen = true;
                }
            }
        });
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

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        exoPlayer.pause();
    }

    @Override
    protected void onResume() {
        exoPlayer.play();
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        exoPlayer.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        exoPlayer.release();
    }
}
