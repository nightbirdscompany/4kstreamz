package com.nightbirds.streamz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.squareup.picasso.Picasso;

public class MovieActivity extends AppCompatActivity {

    Button movieWatch, movieDown;
    ImageView moviePos;
    TextView movieTitle;
   public static String movieLink, moviepos, movienam, moviedis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_movie);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        movieWatch = findViewById(R.id.movieWatch);
        movieDown = findViewById(R.id.movieDown);

      //  movieTitle.setText(movienam);

//        Picasso.get().load(moviepos).into(moviePos);
//
//        movieWatch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                PlayerActivity.videoUrl = (movieLink);
//
//                Intent i = new Intent(MovieActivity.this, PlayerActivity.class);
//                startActivity(i);
//            }
//        });


    }
}