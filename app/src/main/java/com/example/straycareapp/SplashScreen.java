package com.example.straycareapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Objects;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        Objects.requireNonNull(getSupportActionBar()).hide();
        TextView heading=findViewById(R.id.splashMainHeading);
        Intent intent = new Intent(getApplicationContext(), Dashboard.class);
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                heading.setText("Stray Animal Care");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                startActivity(intent);
                finish();
            }
        };
        thread.start();
    }
}