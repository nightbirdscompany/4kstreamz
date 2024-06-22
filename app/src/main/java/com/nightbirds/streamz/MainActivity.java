package com.nightbirds.streamz;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomnavigationview;
    DrawerLayout drawlay;
    MaterialToolbar toolbar;
    FrameLayout framlay;
    NavigationView navview;
    View headerview;
    TextView headername, headeremail;
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

        //  meownav = findViewById(R.id.meownav);

        //================== find view by id end


        //================================ meow bottom navigation start

        //  replace( new MainhomeFragment());

//        meownav.show(2, true);

        //       meownav.add(new MeowBottomNavigation.Model(1, R.drawable.ic_tv));
//        meownav.add(new MeowBottomNavigation.Model(1, R.drawable.ic_home));
        //       meownav.add(new MeowBottomNavigation.Model(1, R.drawable.ic_movie));

        //     meownavigation();
        //================================ meow bottom navigation end



        //   FragmentManager fmanager = getSupportFragmentManager();
        //    FragmentTransaction fragmentTransaction =fmanager.beginTransaction();
        //   fragmentTransaction.add(R.id.framlay, new MainhomeFragment());
        //    fragmentTransaction.commit();


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                MainActivity.this, drawlay, toolbar, R.string.drawer_close, R.string.drawer_open);

        drawlay.addDrawerListener(toggle);

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
                    Toast.makeText(MainActivity.this, "yeh working", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(getApplicationContext(), "HOME", Toast.LENGTH_LONG).show();
                }

                else  if (menuItem.getItemId()==R.id.bottom_nav_tv){
                    Toast.makeText(getApplicationContext(), "TV", Toast.LENGTH_LONG).show();
                }

                else  if (menuItem.getItemId()==R.id.bottom_nav_movie){
                    Toast.makeText(getApplicationContext(), "MOVIE", Toast.LENGTH_LONG).show();
                }

                return true;
            }
        });



    }
    //========= ON creat end

    //================================ meow bottom navigation start


    //================================ meow bottom navigation end

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