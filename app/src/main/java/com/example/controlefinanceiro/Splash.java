package com.example.controlefinanceiro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Splash extends AppCompatActivity implements Runnable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler handler = new Handler();
        handler.postDelayed(Splash.this,Integer.parseInt(getString(R.string.tempo_splash)));
    }

    @Override
    public void run() {

        startActivity(new Intent(Splash.this, MainActivity.class));
        finish();
    }
}
