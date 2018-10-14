package com.charlesrowland.popularmovies;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.charlesrowland.popularmovies.interfaces.ApiInterface;
import com.charlesrowland.popularmovies.interfaces.MovieAdapter;
import com.charlesrowland.popularmovies.model.MovieSortingWrapper;
import com.charlesrowland.popularmovies.model.MovieInfoResult;
import com.charlesrowland.popularmovies.network.ApiClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName() + " fart";
    public static int FADE_DELAY = 600;
    public static int START_DELAY = 1000;
    public static int POSTER_FADE_OUT_DELAY = 400;

    // quick intro to ButterKnife here: @BindView gets the views,
    // ButterKnife.bind() sets them. That is all.

    private final static String POSTER_SAVE_STATE = "poster_save_state";

    @BindView(R.id.fetching_movies) View fetchMoviesView;
    @BindView(R.id.no_network) View noNetwork;
    @BindView(R.id.no_results) View noResults;
    @BindView(R.id.movie_poster_recyclerview) RecyclerView mMoviePosterRecyclerView;
    @BindView(R.id.swipe_refresh) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.dots) pl.tajchert.waitingdots.DotsTextView dots;

    private MovieAdapter mAdapter;
    private List<MovieInfoResult> results;
    private GridLayoutManager gridLayoutManager;
    private View alertLayout;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private String appBarTitle = "";

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        // TODO: implement savedInstanceState == null || !savedInstanceState.containsKey(POSTER_SAVE_STATE)
        // TODO: for the love of god stop adding more shit! be done already!

//        getWindow().setSharedElementReturnTransition(null);
//        getWindow().setSharedElementReenterTransition(null);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        appBarTitle = getResources().getString(R.string.appBarTitle_popular);

        // you want to refresh? i got your refresh right here buddy!
        setSwipeRefreshLayout();

        // separate method for RecyclerView setup
        setupRecyclerView();

        // check the network/internet status and show the proper layout
        if (!checkNetworkStatus()) {
            showNoNetwork();
        }
    }

    private void setTitle() {
        SpannableString s = new SpannableString(appBarTitle);
        s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.textColorPrimary)), 0, appBarTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }

    private void showPosters() {
        noNetwork.setVisibility(View.GONE);
        noResults.setVisibility(View.GONE);
        mMoviePosterRecyclerView.setVisibility(View.VISIBLE);
        hideFetchingMovies();
    }

    private void showNoNetwork() {
        noNetwork.setVisibility(View.VISIBLE);
        mMoviePosterRecyclerView.setVisibility(View.GONE);
        noResults.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);
    }

    private void showNoResults() {
        noResults.setVisibility(View.VISIBLE);
        noNetwork.setVisibility(View.GONE);
        mMoviePosterRecyclerView.setVisibility(View.GONE);
    }

    private void hideFetchingMovies() {
        if (fetchMoviesView.getVisibility() == View.VISIBLE) {
            mMoviePosterRecyclerView.setAlpha(0f);
            mMoviePosterRecyclerView.setVisibility(View.VISIBLE);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setTitle();
                    fetchMoviesView.animate().alpha(0f).setDuration(FADE_DELAY).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            fetchMoviesView.setVisibility(View.GONE);
                            if (dots.isPlaying()) {
                                dots.stop();
                            }
                            mMoviePosterRecyclerView.setClickable(true);
                        }
                    });
                }
            }, START_DELAY);

            mMoviePosterRecyclerView.animate().alpha(1f).setDuration(FADE_DELAY).setListener(null);
        }
    }

    private void setSwipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (checkNetworkStatus()) {
                    showPosters();
                    buildAdapter();
                } else {
                    showNoNetwork();
                }
            }
        });
    }

    private void setupRecyclerView() {
        mMoviePosterRecyclerView = findViewById(R.id.movie_poster_recyclerview);
        noNetwork = findViewById(R.id.no_network);
        gridLayoutManager = new GridLayoutManager(this, getResources().getInteger(R.integer.grid_span_count));
        mMoviePosterRecyclerView.setLayoutManager(gridLayoutManager);

        buildAdapter();
    }

    private void buildAdapter() {
        swipeRefreshLayout.setRefreshing(false);

        Call<MovieSortingWrapper> call;
        ApiInterface api = ApiClient.getRetrofit().create(ApiInterface.class);

        if (getSharedPreferenceOrderbyValue().equals(getResources().getString(R.string.settings_order_by_default))) {
            call = api.getPopularMovies();
            appBarTitle = getResources().getString(R.string.appBarTitle_popular);
        } else {
            call = api.getTopRatedMovies();
            appBarTitle = getResources().getString(R.string.appBarTitle_toprated);
        }
        
        call.enqueue(new Callback<MovieSortingWrapper>() {
            @Override
            public void onResponse(Call<MovieSortingWrapper> call, Response<MovieSortingWrapper> response) {

                // i found an issue with some errors coming back and not triggering onFailure so this
                // was my solution.
                if(response.code() != 200) {
                    showNoResults();
                    return;
                }

                // we have a response so show the posters
                showPosters();

                final MovieSortingWrapper movie = response.body();
                results = movie.getResults();

                // wtf is this about! I am removing all results that are not english movies.
                // this isn't because I don't like non english movies though. Some of the movies
                // from other countries have various pieces of data missing and its crashing the app
                // adding in region=US in the api call is supposed to do this, but guess what, it
                // doesn't always work....
                List<MovieInfoResult> non_english_results = new ArrayList<>();
                for (MovieInfoResult m : results) {
                    if (!m.getOriginalLanguage().equals("en")) {
                        non_english_results.add(m);
                    }
                }

                results.removeAll(non_english_results);
                mAdapter = new MovieAdapter(results);
                mAdapter.setData(results);
                mMoviePosterRecyclerView.setAdapter(mAdapter);

                mAdapter.setOnClickListener(new MovieAdapter.OnItemClickListener() {
                    @SuppressLint("ClickableViewAccessibility")
                    @Override
                    public void onItemClick(int position) {
                        int total = mAdapter.getItemCount();
                        View selectedView = mMoviePosterRecyclerView.getLayoutManager().findViewByPosition(position);
                        ImageView imageView = selectedView.findViewById(R.id.movie_poster_view);

                        Intent intent =  new Intent(MainActivity.this, MovieDetailsActivity.class);
                        intent.putExtra(getResources().getString(R.string.parcelable_intent_key), results.get(position));

                        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, imageView, ViewCompat.getTransitionName(imageView));
                        startActivity(intent, options.toBundle());

                        resetPosterTransitions(total);
                    }
                });
            }

            @Override
            public void onFailure(Call<MovieSortingWrapper> call, Throwable t) {
                showNoResults();
            }
        });
    }

    public void resetPosterTransitions(int total) {
        for (int i = 0; i < total; i++) {
            View itemView = mMoviePosterRecyclerView.getLayoutManager().findViewByPosition(i);
            if (itemView != null) {
                itemView.setTransitionName(null);
            }
        }
    }

    private String getSharedPreferenceOrderbyValue() {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String orderby_key = getResources().getString(R.string.settings_order_by_key);

        return mPreferences.getString(orderby_key, getResources().getString(R.string.settings_order_by_default));
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
                Log.i(TAG, "onOptionsItemSelected: CLICKED");
                showSortOptionsDialog();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onPause() {
        super.onPause();
        overridePendingTransition(0, R.string.poster_scale_transition);
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void showSortOptionsDialog() {
        LayoutInflater inflater = getLayoutInflater();
        alertLayout = inflater.inflate(R.layout.settings_dialog, null);

        final RadioGroup sortGroup = alertLayout.findViewById(R.id.movieSortOrder);
        RadioButton popularity_button = alertLayout.findViewById(R.id.popularity);
        RadioButton toprated_button = alertLayout.findViewById(R.id.top_rated);

        if (getSharedPreferenceOrderbyValue().equals(getResources().getString(R.string.settings_order_by_default))) {
            popularity_button.setChecked(true);
        } else {
            toprated_button.setChecked(true);
        }

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(alertLayout);
        alert.setMessage("Options");
        alert.setCancelable(false);

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String sortOrder = getSelectedSortOrder(sortGroup);

                if (!getSharedPreferenceOrderbyValue().equals(sortOrder)) {
                    mEditor = mPreferences.edit();
                    mEditor.putString(getResources().getString(R.string.settings_order_by_key), sortOrder);
                    mEditor.commit();
                    buildAdapter();
                }
            }
        });

        AlertDialog dialog = alert.create();
        dialog.show();
    }

    private String getSelectedSortOrder(RadioGroup sortGroup) {
        int radioButtonID = sortGroup.getCheckedRadioButtonId();
        View radioButton = sortGroup.findViewById(radioButtonID);
        int idx = sortGroup.indexOfChild(radioButton);
        RadioButton r = (RadioButton)  sortGroup.getChildAt(idx);
        return r.getText().toString().toLowerCase();
    }
}
