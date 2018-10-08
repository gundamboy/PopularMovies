package com.charlesrowland.popularmovies;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.charlesrowland.popularmovies.interfaces.ApiInterface;
import com.charlesrowland.popularmovies.interfaces.CastCrewAdapter;
import com.charlesrowland.popularmovies.interfaces.Credit;
import com.charlesrowland.popularmovies.ui.EqualSpacingItemDecoration;
import com.charlesrowland.popularmovies.interfaces.SimilarMoviesAdapter;
import com.charlesrowland.popularmovies.model.MovieAllDetailsResult;
import com.charlesrowland.popularmovies.model.MovieInfoResult;
import com.charlesrowland.popularmovies.network.ApiClient;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MovieDetailsActivity extends AppCompatActivity {
    private static final String TAG = MovieDetailsActivity.class.getSimpleName() + " fart";

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.backdrop) ImageView mBackdrop;
    @BindView(R.id.movie_poster) ImageView mPoster;
    @BindView(R.id.movie_title) TextView mTitle;
    @BindView(R.id.genres) TextView mGenres;
    @BindView(R.id.runtime) TextView mRuntime;
    @BindView(R.id.rating_text) TextView mRatingText;
    @BindView(R.id.imdb_link) TextView mIMDB;
    @BindView(R.id.mpaa_rating) TextView mMpaaRating;
    @BindView(R.id.release_date) TextView mReleaseDate;
    @BindView(R.id.director) TextView mDirector;
    @BindView(R.id.producers) TextView mProducers;
    @BindView(R.id.writers) TextView mWriters;
    @BindView(R.id.cast_intro) TextView mCastIntro;
    @BindView(R.id.overview) TextView mOverview;
    @BindView(R.id.tagline) TextView mTagline;
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
    String mpaaRating = "";
    String imdb_path_id = "";

    private static final String MOVIE_ID = "movie_id";
    private static final String MOVIE_TITLE = "original_title";
    private String mMovieTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        // this is just for testing while using the DetailsActivity as the starting activity
//        mMovieId = 348350;
//        mMovieTitle = "Solo: A Star Wars Story";

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
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.textColorPrimary), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setTitle(mMovieTitle);

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

                imdb_path_id = mMovieInfo.getImdb_id();

                // the MPAA rating is a massive PITA to get. its nested 3 levels deep.
                List<MovieAllDetailsResult.ReleaseDatesResults> rdWrapper = mMovieInfo.getRelease_dates().getResults();
                List<MovieAllDetailsResult.ReleaseDatesResultsContent> content;
                for(MovieAllDetailsResult.ReleaseDatesResults rd : rdWrapper) {
                    if (rd.getIso_3166_1().equals("US")) {
                        content = rd.getReleaseDateContents();
                        for(MovieAllDetailsResult.ReleaseDatesResultsContent inner_contents : content) {
                            String cert = inner_contents.getCertification();
                            if (cert != null && !cert.isEmpty()) {
                                if (cert.equals("G") || cert.equals("PG") || cert.equals("PG-13") || cert.equals("R") || cert.equals("NC-17")) {
                                    mpaaRating = inner_contents.getCertification();
                                    break;
                                }
                            }

                        }
                    }
                }

                setImageViews();
                setTextViews();
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
        Drawable mpaaBackground;

        mTitle.setText(mMovieInfo.getOriginalTitle());
        mGenres.setText(mGenreString);

        int runtime = mMovieInfo.getRuntime() != null ? mMovieInfo.getRuntime() : 0;
        String runtimeString = convertRuntime(runtime);
        mRuntime.setText(runtimeString);

        mRatingText.setText(String.valueOf(mMovieInfo.getVoteAverage()));

        String releaseDate = mMovieInfo.getReleaseDate();
        mReleaseDate.setText(convertReleaseDate(releaseDate));

        String tagLine = mMovieInfo.getTagline();
        mTagline.setText(tagLine);

        mOverview.setText(mMovieInfo.getOverview());

        if (mpaaRating != null && !mpaaRating.isEmpty()) {
            // if the movie is rated R make the background red. other wise its green, which is on the
            // textview by default.
            if (mpaaRating.equals("R")) {
                mpaaBackground = ContextCompat.getDrawable(this, R.drawable.rounded_corners_mpaa_r);
                mMpaaRating.setBackground(mpaaBackground);
            }

            mMpaaRating.setText(mpaaRating);
        } else {
            mMpaaRating.setVisibility(View.GONE);
        }

        mIMDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToImdb(imdb_path_id);
            }
        });

    }

    private String convertReleaseDate(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        try {
            Date releaseDate = dateFormat.parse(date);
            dateFormat.applyPattern("M d, yyyy");
            return dateFormat.format(releaseDate);
        } catch (ParseException e) {
            Log.e(TAG, "Problem formatting the date: ", e);
            return null;
        }
    }

    private String convertRuntime(int fullMinutes ) {
        int hours = fullMinutes / 60;
        int minutes = fullMinutes % 60;
        return String.valueOf(hours) + "h " + String.valueOf(minutes) + "min";
    }

    private void goToImdb(String imdb_id) {
        String imdb_link = "https://www.imdb.com/title/" + imdb_id + "/";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(imdb_link));
        startActivity(i);
    }

    private int getSpannableCount(String str) {
        String[] parts = str.split(":");
        return parts[0].length();
    }

    private void setImageViews() {
        String backdropUrl = getResources().getString(R.string.backdrop_url) + mMovieInfo.getBackdrop_path();
        Picasso.get().load(backdropUrl).placeholder(R.color.windowBackground).into(mBackdrop);

        String posterUrl = getResources().getString(R.string.backdrop_url) + mMovieInfo.getPosterPath();
        Picasso.get().load(posterUrl).placeholder(R.color.windowBackground).into(mPoster);
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

        SpannableString topStarsSpan = new SpannableString(top_stars);
        topStarsSpan.setSpan(new StyleSpan(Typeface.BOLD),0,getSpannableCount(top_stars),0);
        mCastIntro.setText(topStarsSpan, TextView.BufferType.SPANNABLE);

        castRecyclerViewSetup();
    }

    private void setCrewMembers() {
        String director = getResources().getString(R.string.details_director) +  " ";
        String writers = getResources().getString(R.string.details_writers) +  " ";;
        String producers = getResources().getString(R.string.details_producers) +  " ";

        int count = 0;
        for (MovieAllDetailsResult.CrewResults crew_member : mCrew) {
            if (count <=7 && crew_member.getCrew_image() != null && !crew_member.getJob().equals("Director")) {
                // why are you incrementing up to you ask...
                // because reasons. and science. see the director notes below. and ACTION!
                mCreditsCrew.add(new Credit(crew_member.getCrew_image(), crew_member.getCrew_name(), crew_member.getJob()));
                count++;
            }

            if(crew_member.getJob().equals("Story") || crew_member.getJob().equals("Screenplay")) {
                writers += crew_member.getCrew_name() + ", ";
            }

            if(crew_member.getJob().equals("Producer") || crew_member.getJob().equals("Executive Producer")) {
                producers += crew_member.getCrew_name() + ", ";
            }

            if(crew_member.getJob().equals("Director")) {
                // i want a total of 8. if the directory was found i want them at the start
                // if the directory was not found, i need to add them later
                director += crew_member.getCrew_name() + ", ";
                mCreditsCrew.add(0,new Credit(crew_member.getCrew_image(), crew_member.getCrew_name(), crew_member.getJob()));
            }
        }

        // removes the last comma from the strings
        writers = writers.substring(0, writers.length()-2);
        producers = producers.substring(0, producers.length()-2);
        director = director.substring(0, director.length()-2);

        // some movies have more than 1 director. Adjust the "Director" text to be "Directors" if so
        if (director.contains(",")) {
            StringBuilder sb = new StringBuilder(director);
            sb.insert(getSpannableCount(director), "s");
            director = sb.toString();
        }

        // makes the Director: Producers: etc bold
        SpannableString producersSpan = new SpannableString(producers);
        producersSpan.setSpan(new StyleSpan(Typeface.BOLD),0,getSpannableCount(producers),0);
        mProducers.setText(producersSpan, TextView.BufferType.SPANNABLE);

        SpannableString writersSpan = new SpannableString(writers);
        writersSpan.setSpan(new StyleSpan(Typeface.BOLD),0,getSpannableCount(writers),0);
        mWriters.setText(writersSpan, TextView.BufferType.SPANNABLE);

        SpannableString directorSpan = new SpannableString(director);
        directorSpan.setSpan(new StyleSpan(Typeface.BOLD),0,getSpannableCount(director),0);
        mDirector.setText(directorSpan, TextView.BufferType.SPANNABLE);

        crewRecyclerViewSetup();
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
                Intent intent = new Intent(MovieDetailsActivity.this, MovieDetailsActivity.class);
                intent.putExtra(MOVIE_ID, movieId);
                intent.putExtra(MOVIE_TITLE, title);

                startActivity(intent);
                MovieDetailsActivity.this.finish();

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
