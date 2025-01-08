package com.nightbirds.streamz;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AboutDeveloper extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_developer);


        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.getNavigationIcon().setTint(getResources().getColor(R.color.white));



        ImageView phoneIcon = findViewById(R.id.call);
        ImageView facebookIcon = findViewById(R.id.facebook);
        ImageView whatsappIcon = findViewById(R.id.whatsApp);
        ImageView instagram = findViewById(R.id.instagram);
        ImageView telegram = findViewById(R.id.telegram);
        ImageView emailIcon = findViewById(R.id.mail);


        // Phone icon click
        phoneIcon.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:+8801892733656"));
            startActivity(intent);
        });

        // Facebook icon click
        facebookIcon.setOnClickListener(v -> {
            String facebookUrl = "https://www.facebook.com/developerhasan4"; // Replace with your Facebook profile link
            String facebookAppUrl = "fb://facewebmodal/f?href=" + facebookUrl;

            Intent facebookIntent = new Intent(Intent.ACTION_VIEW);

            try {
                // Try to open in the Facebook app
                facebookIntent.setData(Uri.parse(facebookAppUrl));
                startActivity(facebookIntent);
            } catch (ActivityNotFoundException e) {
                // If the Facebook app is not installed, open in the browser
                facebookIntent.setData(Uri.parse(facebookUrl));
                startActivity(facebookIntent);
            }
        });

        // WhatsApp icon click
        whatsappIcon.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/+8801892733656"));
            startActivity(intent);
        });

        instagram.setOnClickListener(v -> {
            Uri uri = Uri.parse("http://instagram.com/_u/developerhasan4"); // Replace 'developerhasan4' with the desired username
            Intent instagramIntent = new Intent(Intent.ACTION_VIEW, uri);
            instagramIntent.setPackage("com.instagram.android");

            try {
                startActivity(instagramIntent);
            } catch (ActivityNotFoundException e) {
                // Instagram app is not installed, open in browser
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/developerhasan4"));
                startActivity(browserIntent);
            }
        });

        telegram.setOnClickListener(v -> {
            Uri uri = Uri.parse("https://t.me/muhammadhasanmia"); // Replace 'username' with the Telegram username
            Intent telegramIntent = new Intent(Intent.ACTION_VIEW, uri);
            telegramIntent.setPackage("org.telegram.messenger");

            try {
                startActivity(telegramIntent);
            } catch (ActivityNotFoundException e) {
                // Telegram app is not installed, open in browser
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/muhammadhasanmia"));
                startActivity(browserIntent);
            }
        });



        // Email icon click
        emailIcon.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:developerhasan4@gmail.com"));
            startActivity(intent);
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}