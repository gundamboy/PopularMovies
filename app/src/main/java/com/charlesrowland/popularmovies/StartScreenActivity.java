package com.charlesrowland.popularmovies;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StartScreenActivity extends AppCompatActivity {
    public static int START_DELAY = 10000;

    @BindView(R.id.fetching_movies) View fetchMoviesView;
    @BindView(R.id.app_info) View creditsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
        ButterKnife.bind(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(StartScreenActivity.this, MainActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        }, START_DELAY);
    }

    private void crossfade() {
        fetchMoviesView.setAlpha(0f);
        fetchMoviesView.setVisibility(View.VISIBLE);

        creditsText.animate().alpha(0f).setDuration(1000).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                creditsText.setVisibility(View.GONE);
            }
        });

        fetchMoviesView.animate().alpha(1f).setDuration(1000).setListener(null);
    }
}
