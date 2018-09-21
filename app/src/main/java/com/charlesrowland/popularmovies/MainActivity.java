package com.charlesrowland.popularmovies;


import android.content.Context;
import android.os.Bundle;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.charlesrowland.popularmovies.interfaces.ApiInterface;
import com.charlesrowland.popularmovies.interfaces.MovieAdapter;
import com.charlesrowland.popularmovies.model.Movie;
import com.charlesrowland.popularmovies.model.Result;
import com.charlesrowland.popularmovies.network.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName() + " fart";

    public static final String MOVIE_DB_API_URL = "https://api.themoviedb.org/3/";
    private final static String POSTER_SAVE_STATE = "poster_save_state";
    private final static String API_KEY = BuildConfig.ApiKey;

    private RecyclerView mMoviePosterRecyclerView;
    private MovieAdapter mAdapter;
    private List<Result> results;
    private ConstraintLayout noNetwork;
    private GridLayoutManager gridLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView noInternetTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO: implement savedInstanceState == null || !savedInstanceState.containsKey(POSTER_SAVE_STATE)

        noInternetTextView = findViewById(R.id.internet_out_message);

        // separate method for RecyclerView setup
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

    private void setSwipeRefreshLayout() {
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
    }

    private void setupRecyclerView() {
        mMoviePosterRecyclerView = findViewById(R.id.movie_poster_recyclerview);
        noNetwork = findViewById(R.id.no_network);
        gridLayoutManager = new GridLayoutManager(this, 2);
        mMoviePosterRecyclerView.setLayoutManager(gridLayoutManager);

        buildAdapter();

    }

    private void buildAdapter() {
        ApiInterface api = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<Movie> call = api.getPopularMovies(API_KEY);
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                Movie movie = response.body();
                mAdapter = new MovieAdapter(results);
                mAdapter.setData(movie.getResults());
                mMoviePosterRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {

            }
        });
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
        Log.i(TAG, "checkNetworkStatus: is there a network connection? ");
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.action_show_options:
                return true;
        }

        return super.onOptionsItemSelected(item);
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
