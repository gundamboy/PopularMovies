package com.charlesrowland.popularmovies;


import android.content.Context;
import android.os.Bundle;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.constraint.ConstraintLayout;
import android.view.View;

import java.util.ArrayList;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.charlesrowland.popularmovies.model.MovieInfo;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private final static String POSTER_SAVE_STATE = "poster_save_state";

    private ArrayList<MovieInfo> movies = new ArrayList<>();
    private RecyclerView mMoviePosterRecyclerView;
    private ConstraintLayout noNetwork;
    private GridLayoutManager gridLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null || !savedInstanceState.containsKey(POSTER_SAVE_STATE)) {
            movies = new ArrayList<>();
        } else {

        }

        // separate method for recyclerview setup takes care of click listeners without conflicts
        setupRecyclerView();

        // check the network/internet status and show the proper layout
        if (checkNetworkStatus()) {
            showPosters();
        } else {
            showNoNetwork();
        }
    }

    private void showPosters() {
        noNetwork.setVisibility(View.INVISIBLE);
        mMoviePosterRecyclerView.setVisibility(View.VISIBLE);
    }
    private void showNoNetwork() {
        noNetwork.setVisibility(View.VISIBLE);
        mMoviePosterRecyclerView.setVisibility(View.INVISIBLE);
        swipeRefreshLayout.setRefreshing(false);
    }

    private void setupRecyclerView() {
        mMoviePosterRecyclerView = findViewById(R.id.movie_poster_recyclerview);
        noNetwork = findViewById(R.id.no_network);
        gridLayoutManager = new GridLayoutManager(this, 2);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);

    }

    private boolean checkNetworkStatus() {
        //Check for network status
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = Objects.requireNonNull(connectivityManager).getActiveNetworkInfo();
        boolean hasNetworkConn = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            hasNetworkConn = true;
        }
        return hasNetworkConn;
    }

    private void setSwipeRefreshListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (checkNetworkStatus()) {
                    showPosters();
                } else {
                    showNoNetwork();
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
