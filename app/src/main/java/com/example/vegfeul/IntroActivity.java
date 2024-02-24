package com.example.vegfeul;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        TextView textView = findViewById(R.id.startbtn);

        Handler handler = new Handler();
        int delayMillis = 1200;
        final Intent nextIntent = new Intent(this, MainActivity.class);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(nextIntent);
                finish(); // Optional: close the current activity if you don't want to go back to it
            }
        }, delayMillis);


    }
}