package com.charlesrowland.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent goToStart = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(goToStart);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }
}
