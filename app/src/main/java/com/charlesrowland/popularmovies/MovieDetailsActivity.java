package com.charlesrowland.popularmovies;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Movie;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.charlesrowland.popularmovies.interfaces.ApiInterface;
import com.charlesrowland.popularmovies.interfaces.CastCrewAdapter;
import com.charlesrowland.popularmovies.interfaces.Credit;
import com.charlesrowland.popularmovies.interfaces.EqualSpacingItemDecoration;
import com.charlesrowland.popularmovies.model.MovieAllDetailsResult;
import com.charlesrowland.popularmovies.model.MovieInfoResult;
import com.charlesrowland.popularmovies.model.MovieSortingWrapper;
import com.charlesrowland.popularmovies.network.ApiClient;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailsActivity extends AppCompatActivity {
    private static final String TAG = MovieDetailsActivity.class.getSimpleName() + " fart";

    @BindView(R.id.backdrop) ImageView mBackdrop;
    @BindView(R.id.movie_poster) ImageView mPoster;
    @BindView(R.id.movie_title) TextView mTitle;
    @BindView(R.id.genres) TextView mGenres;
    @BindView(R.id.runtime) TextView mRuntime;
    @BindView(R.id.ratingBar) RatingBar mRatingBar;
    @BindView(R.id.rating_text) TextView mRatingText;
    @BindView(R.id.release_date) TextView mReleaseDate;
    @BindView(R.id.director) TextView mDirector;
    @BindView(R.id.cast_intro) TextView mCastIntro;
    @BindView(R.id.overview) TextView mOverview;
    @BindView(R.id.cast_recyclerview) RecyclerView castRecyclerView;
    private int mMovieId;
    private MovieAllDetailsResult mMovieInfo;
    private List<MovieAllDetailsResult.MovieGenreResult> mGenresList;
    private List<MovieAllDetailsResult.CastResults> mCast;
    private List<MovieAllDetailsResult.CrewResults> mCrew;
    private List<MovieAllDetailsResult.SimilarResults> mSimilar;
    private List<MovieAllDetailsResult.VideoResults> mVideos;
    private List<MovieAllDetailsResult.ReviewResults> mReviews;
    private CastCrewAdapter mCastCrewAdapter;
    private ArrayList<Credit> mCredits = new ArrayList<>();

    String mGenreString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Intent intent = getIntent();
        MovieInfoResult passedInfo = intent.getParcelableExtra(getResources().getString(R.string.parcelable_intent_key));
        mMovieId = passedInfo.getMovieId();

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(passedInfo.getOriginalTitle());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getMovieInfo();
    }

    private void getMovieInfo() {
        Call<MovieAllDetailsResult> call;
        ApiInterface api = ApiClient.getRetrofit().create(ApiInterface.class);
        call = api.getAllMovieDetails(mMovieId);
        Log.i(TAG, "getMovieInfo: call urk: " + call.request());
        call.enqueue(new Callback<MovieAllDetailsResult>() {
            @Override
            public void onResponse(Call<MovieAllDetailsResult> call, Response<MovieAllDetailsResult> response) {
                mMovieInfo = response.body();

                // get the genres and built the genre string
                // the genres come back as an array of objects, genres: [ {...}, {...}, etc ]
                // in the results file this was just making them a List<> of type MovieGenreResult
                // which is the id and name (actual object keys)
                // the list can be looped through like this to get the data
                // this same setup will work for production_companies, production_countries, and spoken_languages
                mGenresList = mMovieInfo.getGenres();
                for (MovieAllDetailsResult.MovieGenreResult genre : mGenresList) {
                    mGenreString += genre.getName() + ", ";
                }

                // fix comma formatting for genres string. needs a comma, then kill the last comma in the string
                mGenreString = mGenreString.substring(0, mGenreString.length()-2);

                // get the cast and crew lists
                mCast = mMovieInfo.getCredits().getCast();
                mCrew = mMovieInfo.getCredits().getCrew();

                // get the similar movies
                mSimilar = mMovieInfo.getSimilar().getResults();

                // get the videos
                mVideos = mMovieInfo.getVideos().getResults();

                // get the reviews
                mReviews = mMovieInfo.getReviews().getResults();

                // set the images
                setImageViews();

                // set the text
                setTextViews();

                setRatingBar();

            }

            @Override
            public void onFailure(Call<MovieAllDetailsResult> call, Throwable t) {
                Log.i(TAG, "onFailure: there was an error: " + t);
            }
        });
    }

    private void setTextViews() {
        mTitle.setText(mMovieInfo.getOriginalTitle());
        mGenres.setText(mGenreString);

        String runtime = mMovieInfo.getRuntime() + getResources().getString(R.string.details_runtime_min_abbr);
        mRuntime.setText(runtime);

        mRatingText.setText(String.valueOf(mMovieInfo.getVoteAverage()));

        String releaseDate = getResources().getString(R.string.details_release_date) + " " + mMovieInfo.getReleaseDate();
        mReleaseDate.setText(releaseDate);

        // the Director and Stars gets set in the setCastCrew() method since they are 2 places
        setCastCrew();

        mOverview.setText(mMovieInfo.getOverview());

    }

    private void setImageViews() {
        String backdropUrl = getResources().getString(R.string.backdrop_url) + mMovieInfo.getBackdrop_path();
        Picasso.get().load(backdropUrl).placeholder(R.color.colorAccent).into(mBackdrop);

        String posterUrl = getResources().getString(R.string.backdrop_url) + mMovieInfo.getPosterPath();
        Picasso.get().load(posterUrl).placeholder(R.color.colorAccent).into(mPoster);
    }

    private void setCastCrew() {
        String director = getResources().getString(R.string.details_director) +  " ";
        String top_stars = getResources().getString(R.string.details_staring) +   " ";

        for (MovieAllDetailsResult.CrewResults crew_member : mCrew) {
            if (crew_member.getJob().equals("Director")) {
                director += crew_member.getCrew_name();
                mCredits.add(new Credit(crew_member.getCrew_image(), crew_member.getCrew_name(), crew_member.getJob()));
                break;
            }
        }

        mDirector.setText(director);

        // set the short line for staring cast members
        for (MovieAllDetailsResult.CastResults cast_member : mCast) {
            if (cast_member.getOrder() <= 3) {
                top_stars += cast_member.getName() + ", ";
            }

            if (cast_member.getOrder() <= 8) {
                mCredits.add(new Credit(cast_member.getProfile_path(), cast_member.getName(), cast_member.getCharacter()));
            }
        }

        Log.i(TAG, "setCastCrew: checking the mCredits arraylist size: " + mCredits.size());

        top_stars = top_stars.substring(0, top_stars.length()-2);
        mCastIntro.setText(top_stars);


        castRecyclerViewSetUp();
    }

    private void castRecyclerViewSetUp() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        castRecyclerView.setLayoutManager(layoutManager);
        castRecyclerView.addItemDecoration(new EqualSpacingItemDecoration(36, EqualSpacingItemDecoration.HORIZONTAL));
        mCastCrewAdapter = new CastCrewAdapter(mCredits);
        castRecyclerView.setAdapter(mCastCrewAdapter);
    }

    private void setRatingBar() {
        String rating = mMovieInfo.getVoteAverage().toString();
        mRatingBar.setRating(Float.parseFloat(rating));
    }
}
