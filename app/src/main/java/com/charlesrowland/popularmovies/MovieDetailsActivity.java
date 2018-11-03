package com.charlesrowland.popularmovies;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.transition.ChangeBounds;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.charlesrowland.popularmovies.adapters.ReviewsAdapter;
import com.charlesrowland.popularmovies.adapters.VideoAdapter;
import com.charlesrowland.popularmovies.fragments.BottomReviewFragment;
import com.charlesrowland.popularmovies.interfaces.ApiInterface;
import com.charlesrowland.popularmovies.adapters.CastCrewAdapter;
import com.charlesrowland.popularmovies.model.Credit;
import com.charlesrowland.popularmovies.ui.EqualSpacingItemDecoration;
import com.charlesrowland.popularmovies.adapters.SimilarMoviesAdapter;
import com.charlesrowland.popularmovies.model.MovieAllDetailsResult;
import com.charlesrowland.popularmovies.model.MovieInfoResult;
import com.charlesrowland.popularmovies.network.ApiClient;
import com.squareup.picasso.NetworkPolicy;
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
    // constants
    private static final String TAG = MovieDetailsActivity.class.getSimpleName() + " fart";
    private static final String MOVIE_ID = "movie_id";
    private static final String MOVIE_TITLE = "original_title";
    private static final String MOVIE_POSTER = "poster_path";
    private static final String IS_SIMILAR = "is_similar";
    private static final int START_DELAY = 2000;
    private static final int POSTER_FADE_OUT_DELAY = 400;

    // api results save state
    private static final String MOVIE_API_RESULTS_SAVE_STATE = "info_results_object_save_state";
    private static final String CAST_API_RESULTS_SAVE_STATE = "cast_results_object_save_state";
    private static final String CREW_API_RESULTS_SAVE_STATE = "crew_results_object_save_state";
    private static final String SIMILAR_API_RESULTS_SAVE_STATE = "similar_results_object_save_state";
    private static final String REVIEW_API_RESULTS_SAVE_STATE = "review_results_object_save_state";
    private static final String VIDEO_API_RESULTS_SAVE_STATE = "video_results_object_save_state";

    // adapter save states
    private static final String CAST_ADAPTER_SAVE_STATE = "cast_results_object_save_state";
    private static final String CREW_ADAPTER_SAVE_STATE = "crew_results_object_save_state";

    // RecyclerView save states
    private static final String CAST_RECYCLER_SAVE_STATE = "cast_recycler_save_state";
    private static final String CREW_RECYCLER_SAVE_STATE = "crew_recycler_save_state";
    private static final String SIMILAR_RECYCLER_SAVE_STATE = "similar_recycler_save_state";
    private static final String REVIEWS_RECYCLER_SAVE_STATE = "reviews_recycler_save_state";
    private static final String VIDEOS_RECYCLER_SAVE_STATE = "videos_recycler_save_state";

    // some string save states
    private static final String TITLE_SAVE_STATE = "title_save_state";
    private static final String GENRES_SAVE_STATE = "genres_save_state";
    private static final String MPAA_SAVE_STATE = "mpaa_save_state";
    private static final String DIRECTOR_SAVE_STATE = "director_save_state";
    private static final String PRODUCERS_SAVE_STATE = "producers_save_state";
    private static final String WRITERS_SAVE_STATE = "writers_save_state";
    private static final String CAST_INTRO_SAVE_STATE = "cast_intro_save_state";
    private static final String REVIEW_AUTHOR_SAVE_STATE = "review_author_save_state";
    private static final String REVIEW_CONTENT_SAVE_STATE = "review_content_save_state";

    // butterknife view bindings
    @BindView(R.id.movie_details_inner_layout) ConstraintLayout mMovieDetailsLayout;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.backdrop) ImageView mBackdrop;
    @BindView(R.id.movie_poster) ImageView mPoster;
    @BindView(R.id.movie_poster_blocker) ImageView mPosterBlocker;
    @BindView(R.id.similar_movie_poster_blocker) ImageView mSimilarPosterBlocker;
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
    @BindView(R.id.similar_header) TextView mSimilarHeader;
    @BindView(R.id.videos_header) TextView mVideosHeader;
    @BindView(R.id.reviews_header) TextView mReviewsHeader;
    @BindView(R.id.cast_recyclerview) RecyclerView castRecyclerView;
    @BindView(R.id.crew_recyclerview) RecyclerView crewRecyclerView;
    @BindView(R.id.similar_movies_recyclerview) RecyclerView similarMoviesRecyclerView;
    @BindView(R.id.videos_recyclerview) RecyclerView mVideosRecyclerView;
    @BindView(R.id.reviews_recyclerview) RecyclerView mReviewsRecyclerView;

    private Drawable mpaaBackground;
    private BottomSheetBehavior mBottomSheetBehavior;

    // class instance for getting some results
    private MovieAllDetailsResult mMovieInfo;

    // lists that hold the api results
    private List<MovieAllDetailsResult.MovieGenreResult> mGenresList;
    private List<MovieAllDetailsResult.CastResults> mCast;
    private List<MovieAllDetailsResult.CrewResults> mCrew;
    private List<MovieAllDetailsResult.SimilarResults> mSimilarMovies;
    private List<MovieAllDetailsResult.ReviewResults> mReviews;
    private List<MovieAllDetailsResult.VideoResults> mVideos;

    // RecyclerView adapters
    private CastCrewAdapter mCastAdapter;
    private CastCrewAdapter mCrewAdapter;
    private SimilarMoviesAdapter mSimilarMoviesAdapter;
    private ArrayList<Credit> mCreditsCast = new ArrayList<>();
    private ArrayList<Credit> mCreditsCrew = new ArrayList<>();
    private VideoAdapter mVideosAdapter;
    private ReviewsAdapter mReviewsAdapter;

    // layout managers
    private LinearLayoutManager layoutManagerCast;
    private GridLayoutManager layoutManagerSimilar;
    private LinearLayoutManager layoutManagerCrew;
    private LinearLayoutManager layoutManagerReviews;
    private LinearLayoutManager layoutManagerVideos;

    // parcelable variables for restore state stuff
    private Parcelable mCastRecycler = null;
    private Parcelable mCrewRecycler = null;
    private Parcelable mSimilarMoviesRecycler = null;
    private Parcelable mReviewsRecycler = null;
    private Parcelable mVideosRecycler = null;

    private int mMovieId;
    private String mMovieTitle;
    private String mGenreString = "";
    private String mpaaRating = "";
    private String mPosterPath = "";
    private boolean isSimilar = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        ButterKnife.bind(this);

        if (isSimilar) {
            savedInstanceState = null;
        }

        Intent intent = getIntent();
        MovieInfoResult passedInfo = intent.getParcelableExtra(getResources().getString(R.string.parcelable_intent_key));
        Bundle extras = intent.getExtras();

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.textColorPrimary), PorterDuff.Mode.SRC_ATOP);

        if (savedInstanceState == null) {
            // speeds up the scale transition to help hide data flashes
            getWindow().setSharedElementEnterTransition(new ChangeBounds().setDuration(200));

            if (passedInfo != null & !intent.hasExtra(MOVIE_POSTER)) {
                mMovieId = passedInfo.getMovieId();
                mMovieTitle = passedInfo.getOriginalTitle();
                mPosterPath = passedInfo.getPosterPath();
                showImageBlocker(passedInfo, null);
            } else {
                // this handles the intent from similar movies
                if (extras != null) {
                    isSimilar = extras.getBoolean(IS_SIMILAR);
                    mMovieId = extras.getInt(MOVIE_ID);
                    mMovieTitle = extras.getString(MOVIE_TITLE);
                    mPosterPath = extras.getString(MOVIE_POSTER);

                    if (isSimilar) {
                        showSimilarPosterBlocker(mPosterPath);
                    } else {
                        showImageBlocker(null, mPosterPath);
                    }

                }
            }

            getSupportActionBar().setTitle(mMovieTitle);
            getMovieInfo();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(MOVIE_API_RESULTS_SAVE_STATE, mMovieInfo);
        outState.putString(TITLE_SAVE_STATE, mMovieTitle);
        outState.putString(MPAA_SAVE_STATE, mpaaRating);
        outState.putString(GENRES_SAVE_STATE, mGenreString);
        outState.putString(DIRECTOR_SAVE_STATE, mDirector.getText().toString());
        outState.putString(PRODUCERS_SAVE_STATE, mProducers.getText().toString());
        outState.putString(WRITERS_SAVE_STATE, mWriters.getText().toString());
        outState.putString(CAST_INTRO_SAVE_STATE, mCastIntro.getText().toString());

        // time to get the cast members.
        // need to get the scroll position of the layout manage
        mCastRecycler = layoutManagerCast.onSaveInstanceState();
        outState.putParcelable(CAST_RECYCLER_SAVE_STATE, mCastRecycler);

        // need to get mCast which is a List<MovieAllDetailsResult.CastResults>
        outState.putParcelableArrayList(CAST_API_RESULTS_SAVE_STATE, new ArrayList<>(mCast));

        // need to get the adapter, mCreditsCast which is a Parcelable ArrayList<Credit>
        outState.putParcelableArrayList(CAST_ADAPTER_SAVE_STATE, new ArrayList<>(mCreditsCast));

        // time to get the crew members.
        // need to get the scroll position of the layout manage
        mCrewRecycler = layoutManagerCrew.onSaveInstanceState();
        outState.putParcelable(CREW_RECYCLER_SAVE_STATE, mCastRecycler);

        // need to get mCast which is a List<MovieAllDetailsResult.CastResults>
        outState.putParcelableArrayList(CREW_API_RESULTS_SAVE_STATE, new ArrayList<>(mCrew));

        // need to get the adapter, mCreditsCast which is a Parcelable ArrayList<Credit>
        outState.putParcelableArrayList(CREW_ADAPTER_SAVE_STATE, new ArrayList<>(mCreditsCrew));


        // time to get the videos.
        // need to get the scroll position of the layout manage
        mVideosRecycler = layoutManagerVideos.onSaveInstanceState();
        outState.putParcelable(VIDEOS_RECYCLER_SAVE_STATE, mVideosRecycler);

        // need to get mCast which is a List<MovieAllDetailsResult.VideoResults>
        outState.putParcelableArrayList(VIDEO_API_RESULTS_SAVE_STATE, new ArrayList<>(mVideos));

        // time to get the similar movies.
        // there is no scrolling for similar images at the moment, but that may change. Future
        // proof all the things!
        mSimilarMoviesRecycler = layoutManagerSimilar.onSaveInstanceState();
        outState.putParcelable(SIMILAR_RECYCLER_SAVE_STATE, mCastRecycler);

        // need to get mCast which is a List<MovieAllDetailsResult.SimilarResults>
        outState.putParcelableArrayList(SIMILAR_API_RESULTS_SAVE_STATE, new ArrayList<>(mSimilarMovies));

        // time to get the reviews.
        // need to get the scroll position of the layout manage
        if (mReviewsAdapter != null) {
            mReviewsRecycler = layoutManagerReviews.onSaveInstanceState();
            outState.putParcelable(REVIEWS_RECYCLER_SAVE_STATE, mReviewsRecycler);
        }

        // need to get mCast which is a List<MovieAllDetailsResult.ReviewResults>
        outState.putParcelableArrayList(REVIEW_API_RESULTS_SAVE_STATE, new ArrayList<>(mReviews));

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState != null) {
            getSupportActionBar().setTitle(savedInstanceState.getString(TITLE_SAVE_STATE));
            mMovieInfo = savedInstanceState.getParcelable(MOVIE_API_RESULTS_SAVE_STATE);
            mGenreString = savedInstanceState.getString(GENRES_SAVE_STATE);
            mpaaRating = savedInstanceState.getString(MPAA_SAVE_STATE);

            // temps. these get set in the adapter setup for each
            mCastIntro.setText(savedInstanceState.getString(CAST_INTRO_SAVE_STATE));
            mDirector.setText(savedInstanceState.getString(DIRECTOR_SAVE_STATE));
            mProducers.setText(savedInstanceState.getString(PRODUCERS_SAVE_STATE));
            mWriters.setText(savedInstanceState.getString(WRITERS_SAVE_STATE));
            mCastIntro.setText(savedInstanceState.getString(CAST_INTRO_SAVE_STATE));

            // time to set the cast members.
            // need to populate mCast.
            mCast = savedInstanceState.getParcelableArrayList(CAST_API_RESULTS_SAVE_STATE);

            // need to set the cast adapter
            mCreditsCast = savedInstanceState.getParcelableArrayList(CAST_ADAPTER_SAVE_STATE);

            // need to set scroll position
            mCastRecycler = savedInstanceState.getParcelable(CAST_RECYCLER_SAVE_STATE);

            // time to set the cast members.
            // need to populate mCast.
            mCrew = savedInstanceState.getParcelableArrayList(CREW_API_RESULTS_SAVE_STATE);

            // need to set the cast adapter
            mCreditsCrew = savedInstanceState.getParcelableArrayList(CREW_ADAPTER_SAVE_STATE);

            // need to set scroll position
            mCrewRecycler = savedInstanceState.getParcelable(CREW_RECYCLER_SAVE_STATE);

            // time to set similar movies.
            mSimilarMovies = savedInstanceState.getParcelableArrayList(SIMILAR_API_RESULTS_SAVE_STATE);

            // need to set scroll position
            mSimilarMoviesRecycler = savedInstanceState.getParcelable(SIMILAR_RECYCLER_SAVE_STATE);

            // need to set the videos adapter
            mVideos = savedInstanceState.getParcelableArrayList(VIDEO_API_RESULTS_SAVE_STATE);

            // set the scroll position
            mVideosRecycler = savedInstanceState.getParcelable(VIDEOS_RECYCLER_SAVE_STATE);

            // need to set the videos adapter
            mReviews = savedInstanceState.getParcelableArrayList(REVIEW_API_RESULTS_SAVE_STATE);

            // set the scroll position
            mReviewsRecycler = savedInstanceState.getParcelable(REVIEWS_RECYCLER_SAVE_STATE);

            loadFromSavedInstanceState(savedInstanceState);
        }
    }

    private void loadFromSavedInstanceState(Bundle savedInstanceState) {

        // hide the full screen poster, we don't need it
        mPosterBlocker.setVisibility(View.GONE);

        if (savedInstanceState != null) {
            setImageViews();
            setTextViews();
            setCastMembers();
            setCrewMembers();
            similarMovieViewSetup();
            videosViewSetup();
            reviewsSetup();
        }
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
                Log.i(TAG, "onResponse: next is cast members");
                setCastMembers();

                Log.i(TAG, "onResponse: next is crew members");
                setCrewMembers();

                Log.i(TAG, "onResponse: next is similar");
                similarMovieViewSetup();
                hideImageBlocker();

                Log.i(TAG, "onResponse: next is videos");
                videosViewSetup();

                Log.i(TAG, "onResponse: next is reviews");
                reviewsSetup();
            }

            @Override
            public void onFailure(Call<MovieAllDetailsResult> call, Throwable t) {
                Log.i(TAG, "onFailure: there was an error: " + t);
            }
        });
    }

    private String getMpaaRating() {
        List<MovieAllDetailsResult.ReleaseDatesResults> rdWrapper = mMovieInfo.getRelease_dates().getResults();
        List<MovieAllDetailsResult.ReleaseDatesResultsContent> content;
        String rating = "";
        for(MovieAllDetailsResult.ReleaseDatesResults rd : rdWrapper) {
            if (rd.getIso_3166_1().equals("US")) {
                content = rd.getReleaseDateContents();
                for(MovieAllDetailsResult.ReleaseDatesResultsContent inner_contents : content) {
                    String cert = inner_contents.getCertification();
                    if (cert != null && !cert.isEmpty()) {
                        if (cert.equals("G") || cert.equals("PG") || cert.equals("PG-13") || cert.equals("R") || cert.equals("NC-17")) {
                            rating = inner_contents.getCertification();
                            break;
                        }
                    }

                }
            }
        }
        return rating;
    }

    private void hideImageBlocker() {
        final ImageView blocker = isSimilar ? mSimilarPosterBlocker : mPosterBlocker;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                blocker.animate().alpha(0f).setDuration(POSTER_FADE_OUT_DELAY).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        blocker.setVisibility(View.GONE);
                    }
                });
            }
        }, START_DELAY);
    }

    public void showSimilarPosterBlocker(String posterPath) {
        mPosterBlocker.setVisibility(View.GONE);
        mSimilarPosterBlocker.setVisibility(View.VISIBLE);
        mSimilarPosterBlocker.setAlpha(1f);
        Picasso.get().load(posterPath).into(mSimilarPosterBlocker);
    }

    private void showImageBlocker(MovieInfoResult passedInfo, String poster_path) {
        final String posterUrl = passedInfo != null ? getResources().getString(R.string.poster_url) + passedInfo.getPosterPath() : getResources().getString(R.string.poster_url) + poster_path;

        final Picasso p = Picasso.get();

        // set the Picasso instance to be p so we can do p.setIndicatorsEnabled(true) to get debug info
        // from picasso letting us know where the image came from > memory, cache, or web.
        // if picasso fails to find the image in memory or cache, it calls it from the web.
        p.load(posterUrl)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(mPosterBlocker, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {}

                    @Override
                    public void onError(Exception e) {
                        p.load(posterUrl).into(mPosterBlocker);
                    }
                });

        Picasso.get().load(posterUrl)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(mPoster, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {}

                    @Override
                    public void onError(Exception e) {
                        Picasso.get().load(posterUrl).into(mPoster);
                    }
                });
    }

    private void setTextViews() {
        mTitle.setText(mMovieInfo.getOriginalTitle());
        mGenres.setText(mGenreString);

        int runtime = mMovieInfo.getRuntime() != null ? mMovieInfo.getRuntime() : 0;
        String runtimeString = convertRuntime(runtime);
        mRuntime.setText(runtimeString);
        mRatingText.setText(String.valueOf(mMovieInfo.getVoteAverage()));
        mReleaseDate.setText(convertReleaseDate(mMovieInfo.getReleaseDate()));
        mTagline.setText(mMovieInfo.getTagline());
        mOverview.setText(mMovieInfo.getOverview());

        mpaaRating = getMpaaRating();
        if (mpaaRating != null && !mpaaRating.isEmpty()) {
            // if the movie is rated R make the background red. other wise its green, which
            // is on the TextView by default.
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
                goToImdb(mMovieInfo.getImdb_id());
            }
        });
    }

    private void setImageViews() {
        String backdropUrl = getResources().getString(R.string.backdrop_url) + mMovieInfo.getBackdrop_path();
        final String posterUrl = getResources().getString(R.string.backdrop_url) + mMovieInfo.getPosterPath();

        Picasso.get().load(backdropUrl).placeholder(R.color.windowBackground).into(mBackdrop);
        Picasso.get().load(posterUrl).placeholder(R.color.windowBackground).into(mPoster);
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
        if (fullMinutes == 0) {
            return "N/A";
        }

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

    private void setSpanableText(String str, TextView v) {
        str = str.substring(0, str.length()-2);
        SpannableString strSpan = new SpannableString(str);
        strSpan.setSpan(new StyleSpan(Typeface.BOLD),0,getSpannableCount(str),0);
        v.setText(strSpan, TextView.BufferType.SPANNABLE);
    }

    private void setCastMembers() {
        String top_stars;

        if(mCreditsCast.size() == 0) {
            top_stars = getResources().getString(R.string.details_staring) +   " ";

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
        } else {
            top_stars = mCastIntro.getText().toString();
        }

        setSpanableText(top_stars, mCastIntro);
        castRecyclerViewSetup();
    }

    private void setCrewMembers() {
        String director;
        String writers;
        String producers;

        if(mCreditsCrew.size() == 0) {
            director = getResources().getString(R.string.details_director) +  " ";
            writers = getResources().getString(R.string.details_writers) +  " ";;
            producers = getResources().getString(R.string.details_producers) +  " ";

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
        } else {
            producers = mProducers.getText().toString();
            writers = mWriters.getText().toString();
            director = mDirector.getText().toString();
        }


        // makes the Director: Producers: etc bold
        setSpanableText(producers, mProducers);
        setSpanableText(writers, mWriters);
        setSpanableText(director, mDirector);

        crewRecyclerViewSetup();
    }

    private void castRecyclerViewSetup() {
        layoutManagerCast = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        if (mCastRecycler != null) {
            layoutManagerCast.onRestoreInstanceState(mCastRecycler);
        }

        castRecyclerView.setLayoutManager(layoutManagerCast);
        castRecyclerView.addItemDecoration(new EqualSpacingItemDecoration(36, EqualSpacingItemDecoration.HORIZONTAL));
        mCastAdapter = new CastCrewAdapter(mCreditsCast);
        castRecyclerView.setAdapter(mCastAdapter);
    }

    private void crewRecyclerViewSetup() {
        layoutManagerCrew = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        if (mCrewRecycler != null) {
            layoutManagerCrew.onRestoreInstanceState(mCrewRecycler);
        }

        crewRecyclerView.setLayoutManager(layoutManagerCrew);
        crewRecyclerView.addItemDecoration(new EqualSpacingItemDecoration(36, EqualSpacingItemDecoration.HORIZONTAL));
        mCrewAdapter = new CastCrewAdapter(mCreditsCrew);
        crewRecyclerView.setAdapter(mCrewAdapter);
    }

    private void similarMovieViewSetup() {
        if (mSimilarMovies.size() > 0 ) {
            layoutManagerSimilar = new GridLayoutManager(this, getResources().getInteger(R.integer.similar_movies_grid_span_count));

            if (mSimilarMoviesRecycler != null) {
                layoutManagerSimilar.onRestoreInstanceState(mSimilarMoviesRecycler);
            }

            similarMoviesRecyclerView.setLayoutManager(layoutManagerSimilar);
            mSimilarMoviesAdapter = new SimilarMoviesAdapter(mSimilarMovies);
            mSimilarMoviesAdapter.setData(mSimilarMovies);
            similarMoviesRecyclerView.setAdapter(mSimilarMoviesAdapter);

            mSimilarMoviesAdapter.setOnClickListener(new SimilarMoviesAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position, int movieId, String title, String posterPath) {

                    View selectedView = similarMoviesRecyclerView.getLayoutManager().findViewByPosition(position);
                    ImageView imageView = selectedView.findViewById(R.id.movie_poster_view);
                    imageView.setTransitionName(getResources().getString(R.string.similar_poster_scale_transition));

                    Intent intent =  new Intent(MovieDetailsActivity.this, MovieDetailsActivity.class);
                    intent.putExtra(MOVIE_ID, movieId);
                    intent.putExtra(MOVIE_TITLE, title);
                    intent.putExtra(MOVIE_POSTER, posterPath);
                    intent.putExtra(IS_SIMILAR, true);
                    startActivity(intent);

                }
            });
        } else {
            similarMoviesRecyclerView.setVisibility(View.GONE);
            mSimilarHeader.setVisibility(View.GONE);
            setVideosConstraint();
        }
    }

    private void videosViewSetup() {
        if (mVideos.size() > 0 ) {
            layoutManagerVideos = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

            if (mVideosRecycler != null) {
                layoutManagerVideos.onRestoreInstanceState(mVideosRecycler);
            }

            mVideosRecyclerView.setLayoutManager(layoutManagerVideos);
            mVideosRecyclerView.addItemDecoration(new EqualSpacingItemDecoration(36, EqualSpacingItemDecoration.HORIZONTAL));
            mVideosAdapter = new VideoAdapter(mVideos);
            mVideosAdapter.setData(mVideos);
            mVideosRecyclerView.setAdapter(mVideosAdapter);

            mVideosAdapter.setOnClickListener(new VideoAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position, String youtubeUrl, String video_key) {
                    Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + video_key));
                    Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeUrl));

                    try {
                        startActivity(appIntent);
                    } catch (ActivityNotFoundException ex) {
                        if (webIntent.resolveActivity(getPackageManager()) != null) {
                            startActivity(webIntent);
                        }
                    }
                }
            });
        } else {
            mVideosRecyclerView.setVisibility(View.GONE);
            mVideosHeader.setVisibility(View.GONE);
        }
    }

    private void setVideosConstraint() {
        ConstraintSet set = new ConstraintSet();
        mMovieDetailsLayout.addView(mVideosHeader,0);
        set.clone(mMovieDetailsLayout);
        set.connect(mVideosHeader.getId(), ConstraintSet.BOTTOM, mMovieDetailsLayout.getId(), ConstraintSet.BOTTOM, 0);
    }

    private void reviewsSetup() {
        if (mReviews.size() > 0) {
            layoutManagerReviews = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

            if (mReviewsRecycler != null) {
                layoutManagerReviews.onRestoreInstanceState(mReviewsRecycler);
            }

            mReviewsRecyclerView.setLayoutManager(layoutManagerReviews);
            mReviewsRecyclerView.addItemDecoration(new EqualSpacingItemDecoration(36, EqualSpacingItemDecoration.HORIZONTAL));
            mReviewsAdapter = new ReviewsAdapter(mReviews);
            mReviewsAdapter.setData(mReviews);
            mReviewsRecyclerView.setAdapter(mReviewsAdapter);

            mReviewsAdapter.setOnClickListener(new ReviewsAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position, String author, String content) {
                    showBottomSheetDialog(author, content);
                }
            });
        } else {
            mReviewsRecyclerView.setVisibility(View.GONE);
            mReviewsHeader.setVisibility(View.GONE);
        }
    }

    public void showBottomSheetDialog(String author, String content) {
        View view = getLayoutInflater().inflate(R.layout.review_bottom_sheet, null);
        TextView review_author = view.findViewById(R.id.bs_review_author);
        TextView review_content = view.findViewById(R.id.bs_review_content);

        review_author.setText(author);
        review_content.setText(content);

        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
        dialog.show();
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

    private void fadeBlockerIn() {
        mPosterBlocker.setVisibility(View.VISIBLE);

        mPosterBlocker.animate().alpha(1f).setDuration(POSTER_FADE_OUT_DELAY).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }
        });
    }

    @Override
    public void onBackPressed() {

        fadeBlockerIn();

        super.onBackPressed();
    }

}
