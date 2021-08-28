package com.example.eattendance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {
    private static int SPLASH = 3000;
    VideoView vintro;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        // above 3 lines of codes hides action bar

        setContentView(R.layout.activity_main);
        vintro = findViewById(R.id.vd_intro);
        vintro.setVideoPath("android.resource://" + getPackageName() + "/" +R.raw.eaintro);
        vintro.start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this,addclass.class);
                startActivity(intent);
                finish();
            }
        },SPLASH);
    }
}