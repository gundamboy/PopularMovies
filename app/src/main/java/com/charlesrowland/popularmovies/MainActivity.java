package com.charlesrowland.popularmovies;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
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
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName() + " fart";

    public static final String MOVIE_DB_API_URL = "https://api.themoviedb.org/3/";
    private final static String POSTER_SAVE_STATE = "poster_save_state";

    private RecyclerView mMoviePosterRecyclerView;
    private MovieAdapter mAdapter;
    private List<MovieInfoResult> results;
    private ConstraintLayout noNetwork;
    private GridLayoutManager gridLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView noInternetTextView;
    private View alertLayout;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String title = getResources().getString(R.string.app_name);
        SpannableString s = new SpannableString(title);
        s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.textColorPrimary)), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);

        // TODO: implement savedInstanceState == null || !savedInstanceState.containsKey(POSTER_SAVE_STATE)
        // TODO: add a view that indicates data is being fetched. hide this view in the show posters method. only show this view if the network check passes.
        // TODO: add the film reel vector to the drawables folders. uses this for the splash screen and the new fetching data screen.
        // TODO: for the love of god stop adding more shit! be done already!

        noInternetTextView = findViewById(R.id.internet_out_message);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        setSwipeRefreshLayout();

        // separate method for RecyclerView setup
        setupRecyclerView();

        // check the network/internet status and show the proper layout
        if (!checkNetworkStatus()) {
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
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                buildAdapter();
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
        } else {
            call = api.getTopRatedMovies();
        }
        
        call.enqueue(new Callback<MovieSortingWrapper>() {
            @Override
            public void onResponse(Call<MovieSortingWrapper> call, Response<MovieSortingWrapper> response) {
                // we have a response so show the posters
                showPosters();

                final MovieSortingWrapper movie = response.body();
                results = movie.getResults();

                // wtf is this about! I am removing all results that are not english movies.
                // this isn't because I don't like non english movies though. Some of the movies
                // from other countries have various pieces of data missing and its crashing the app
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
                    @Override
                    public void onItemClick(int position) {
                        Intent intent =  new Intent(MainActivity.this, MovieDetailsActivity.class);
                        intent.putExtra(getResources().getString(R.string.parcelable_intent_key), movie.getResults().get(position));
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onFailure(Call<MovieSortingWrapper> call, Throwable t) {
                showNoNetwork();
            }
        });
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

    private void setSwipeRefreshListener() {
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
