package com.nightbirds.streamz;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomnavigationview;
    DrawerLayout drawlay;
    MaterialToolbar toolbar;
    FrameLayout framlay;
    NavigationView navview;
    View headerview;
    TextView headername, headeremail;

    FirebaseRemoteConfig remoteConfig;



    //  MeowBottomNavigation meownav;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //============== finde view by id  start

        drawlay = findViewById(R.id.drawlay);
        toolbar =findViewById(R.id.toolbar);
        framlay =findViewById(R.id.framlay);
        navview = findViewById(R.id.navview);
        bottomnavigationview = findViewById(R.id.bottomnavigationview);
        headerview = navview.getHeaderView(0);
        headername = headerview.findViewById(R.id.headertext);
        headeremail = headerview.findViewById(R.id.headeremail);







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
                else if (item.getItemId()==R.id.share){

                    Intent intent = new Intent( Intent.ACTION_SEND);
                    intent.setType("Text/Plain");
                    String shareLink = "http://4kstreamz.free.nf/";
                    String shareSubject = "Enjoy Live Tv And Movie App";


                    intent.putExtra(Intent.EXTRA_TEXT,shareLink);
                    intent.putExtra(Intent.EXTRA_SUBJECT,shareSubject);
                    startActivity(Intent.createChooser( intent,"Enjoy Live Tv And Movie App"));


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

                else   if (menuItem.getItemId()==R.id.nav_noti){

                    drawlay.closeDrawer(GravityCompat.START);
                }

                else   if (menuItem.getItemId()==R.id.fb_page){
                    Toast.makeText(MainActivity.this, "yeh working", Toast.LENGTH_LONG).show();
                    drawlay.closeDrawer(GravityCompat.START);
                }

                else   if (menuItem.getItemId()==R.id.yt_channel){
                    Toast.makeText(MainActivity.this, "yeh working", Toast.LENGTH_LONG).show();
                    drawlay.closeDrawer(GravityCompat.START);
                }

                else   if (menuItem.getItemId()==R.id.settings){
                    Toast.makeText(MainActivity.this, "yeh working", Toast.LENGTH_LONG).show();
                    drawlay.closeDrawer(GravityCompat.START);
                }

                else if (menuItem.getItemId()==R.id.help) {
                    Intent intent = new Intent(MainActivity.this, HelpActivity.class);
                    startActivity(intent);
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
                    fragmentTransaction.add(R.id.framlay, new HomeFragment());
                    fragmentTransaction.commit();
                }

                else  if (menuItem.getItemId()==R.id.bottom_nav_tv){
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.framlay, new TvFragment());
                    fragmentTransaction.commit();
                }

                else  if (menuItem.getItemId()==R.id.bottom_nav_movie){
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.framlay, new MovieFragment());
                    fragmentTransaction.commit();
                }

                return true;
            }
        });



    }

    //========= ON creat end

    
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
                        finishAndRemoveTask();
                    }
                })

                .show();
    }

    //================= exit alert end
}