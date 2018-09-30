package com.charlesrowland.popularmovies;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Movie;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
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
import com.charlesrowland.popularmovies.interfaces.MovieAdapter;
import com.charlesrowland.popularmovies.interfaces.SimilarMoviesAdapter;
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
    @BindView(R.id.crew_recyclerview) RecyclerView crewRecyclerView;
    @BindView(R.id.similar_movies_recyclerview) RecyclerView similarMoviesRecyclerView;
    private int mMovieId;
    private MovieAllDetailsResult mMovieInfo;
    private List<MovieAllDetailsResult.MovieGenreResult> mGenresList;
    private List<MovieAllDetailsResult.CastResults> mCast;
    private List<MovieAllDetailsResult.CrewResults> mCrew;
    private List<MovieAllDetailsResult.VideoResults> mVideos;
    private List<MovieAllDetailsResult.ReviewResults> mReviews;
    private List<MovieAllDetailsResult.SimilarResults> mSimilarMovies;
    private CastCrewAdapter mCastAdapter;
    private CastCrewAdapter mCrewAdapter;
    private SimilarMoviesAdapter mSimilarMoviesAdapter;
    private ArrayList<Credit> mCreditsCast = new ArrayList<>();
    private ArrayList<Credit> mCreditsCrew = new ArrayList<>();

    String mGenreString = "";
    private static final String MOVIE_ID = "movie_id";
    private static final String MOVIE_TITLE = "original_title";
    private String mMovieTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        // this is just for testing
        mMovieId = 348350;
        mMovieTitle = "Solo: A Star Wars Story";

        Intent intent = getIntent();
        MovieInfoResult passedInfo = intent.getParcelableExtra(getResources().getString(R.string.parcelable_intent_key));
        Bundle extras;

        if (passedInfo != null) {
            mMovieId = passedInfo.getMovieId();
            mMovieTitle = passedInfo.getOriginalTitle();
        } else {
             extras = intent.getExtras();

            if (extras != null) {
                mMovieId = extras.getInt(MOVIE_ID);
                mMovieTitle = extras.getString(MOVIE_TITLE);
            }
        }

        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(mMovieTitle);
        setSupportActionBar(toolbar);

        getMovieInfo();
    }

    private void getMovieInfo() {
        Call<MovieAllDetailsResult> call;
        ApiInterface api = ApiClient.getRetrofit().create(ApiInterface.class);
        call = api.getAllMovieDetails(mMovieId);
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
                mSimilarMovies = mMovieInfo.getSimilar().getResults();

                // get the videos
                mVideos = mMovieInfo.getVideos().getResults();

                // get the reviews
                mReviews = mMovieInfo.getReviews().getResults();

                setImageViews();
                setTextViews();
                setRatingBar();
                setCastMembers();
                setCrewMembers();
                similarMovieViewSetup();

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

        mOverview.setText(mMovieInfo.getOverview());

    }

    private void setImageViews() {
        String backdropUrl = getResources().getString(R.string.backdrop_url) + mMovieInfo.getBackdrop_path();
        Picasso.get().load(backdropUrl).placeholder(R.color.colorAccent).into(mBackdrop);

        String posterUrl = getResources().getString(R.string.backdrop_url) + mMovieInfo.getPosterPath();
        Picasso.get().load(posterUrl).placeholder(R.color.colorAccent).into(mPoster);
    }

    private void setCastMembers() {
        String top_stars = getResources().getString(R.string.details_staring) +   " ";

        // set the short line for staring cast members
        for (MovieAllDetailsResult.CastResults cast_member : mCast) {
            if (cast_member.getOrder() <= 3) {
                top_stars += cast_member.getName() + ", ";
            }

            if (cast_member.getOrder() <= 7) {
                if(cast_member.getProfile_path() != null) {
                    mCreditsCast.add(new Credit(cast_member.getProfile_path(), cast_member.getName(), cast_member.getCharacter()));
                }
            }
        }

        top_stars = top_stars.substring(0, top_stars.length()-2);
        mCastIntro.setText(top_stars);

        castRecyclerViewSetup();
    }

    private void setCrewMembers() {
        String director = getResources().getString(R.string.details_director) +  " ";
        Boolean foundDirector = false;

        int count = 0;
        for (MovieAllDetailsResult.CrewResults crew_member : mCrew) {
            if (count <=7 && crew_member.getCrew_image() != null) {
                // why are you incrementing up to you ask...
                // because reasons. and science. see the director notes below. and ACTION!
                count++;

                if(crew_member.getJob().equals("Director")) {
                    foundDirector = true;
                    // i want a total of 8. if the directory was found i want them at the start
                    // if the directory was not found, i need to add them later
                    count--;
                    director += crew_member.getCrew_name();
                    mCreditsCrew.add(0,new Credit(crew_member.getCrew_image(), crew_member.getCrew_name(), crew_member.getJob()));
                } else {
                    mCreditsCrew.add(new Credit(crew_member.getCrew_image(), crew_member.getCrew_name(), crew_member.getJob()));
                }
            }
        }

        for (MovieAllDetailsResult.CrewResults crew_member : mCrew) {
            if (!foundDirector) {
                if(crew_member.getJob().equals("Director")) {
                    director += crew_member.getCrew_name();
                    mCreditsCrew.add(0, new Credit(crew_member.getCrew_image(), crew_member.getCrew_name(), crew_member.getJob()));
                    foundDirector = true;
                }
            }
        }

        Log.i(TAG, "setCrewMembers: crew memeber size: " + mCreditsCrew.size());

        mDirector.setText(director);
        crewRecyclerViewSetup();
    }

    private void setSimilarMovies() {
        int count = 0;

        for (MovieAllDetailsResult.SimilarResults sm : mSimilarMovies) {

        }
    }

    private void castRecyclerViewSetup() {
        LinearLayoutManager layoutManagerCast = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        castRecyclerView.setLayoutManager(layoutManagerCast);
        castRecyclerView.addItemDecoration(new EqualSpacingItemDecoration(36, EqualSpacingItemDecoration.HORIZONTAL));
        mCastAdapter = new CastCrewAdapter(mCreditsCast);
        castRecyclerView.setAdapter(mCastAdapter);

    }

    private void crewRecyclerViewSetup() {
        LinearLayoutManager layoutManagerCrew = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        crewRecyclerView.setLayoutManager(layoutManagerCrew);
        crewRecyclerView.addItemDecoration(new EqualSpacingItemDecoration(36, EqualSpacingItemDecoration.HORIZONTAL));
        mCrewAdapter = new CastCrewAdapter(mCreditsCrew);
        crewRecyclerView.setAdapter(mCrewAdapter);
    }

    private void similarMovieViewSetup() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, getResources().getInteger(R.integer.similar_movies_grid_span_count));
        similarMoviesRecyclerView.setLayoutManager(gridLayoutManager);
        mSimilarMoviesAdapter = new SimilarMoviesAdapter(mSimilarMovies);
        mSimilarMoviesAdapter.setData(mSimilarMovies);
        similarMoviesRecyclerView.setAdapter(mSimilarMoviesAdapter);

        mSimilarMoviesAdapter.setOnClickListener(new SimilarMoviesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, int movieId, String title) {
                Intent intent =  new Intent(MovieDetailsActivity.this, MovieDetailsActivity.class);
                intent.putExtra(MOVIE_ID, movieId);
                intent.putExtra(MOVIE_TITLE, title);

                startActivity(intent);
                MovieDetailsActivity.this.finish();

            }
        });
    }

    private void setRatingBar() {
        // the rating comes back based on 10. We have 5 stars. 5 is half of 10 so divide by 2
        // to get the 5 star rating. ITS MATHEMATICAL!
        Double ratingNum = mMovieInfo.getVoteAverage() / 2;
        String rating= ratingNum.toString() ;
        mRatingBar.setRating(Float.parseFloat(rating));
    }
}
