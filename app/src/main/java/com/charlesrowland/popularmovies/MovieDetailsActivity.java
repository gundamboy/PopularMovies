package com.charlesrowland.popularmovies;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
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
import com.charlesrowland.popularmovies.data.FavoriteMovie;
import com.charlesrowland.popularmovies.data.FavoriteMovieRepository;
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
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

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
    private static final String APP_DIR = "movie_night";
    private static final String MOVIE_SORTED_BY_FAVS = "sorted_by_favs";
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
    @BindView(R.id.cast_header) TextView mCastHeader;
    @BindView(R.id.crew_header) TextView mCrewHeader;
    @BindView(R.id.similar_header) TextView mSimilarHeader;
    @BindView(R.id.videos_header) TextView mVideosHeader;
    @BindView(R.id.reviews_header) TextView mReviewsHeader;
    @BindView(R.id.favorite_icon) ImageView mFavoritesIcon;
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
    private String mImdbId;
    private String mMovieTitle;
    private String mLanguage;
    private String mTaglineText;
    private String mOverviewText;
    private double mVoteAverage;
    private String mMpaa_rating;
    private String mBackdropPath;
    private String mPosterPath;
    private String mGenreString = "";
    private String mRuntimeText;
    private String mReleaseDateText;
    private String mCastMembersString;
    private String mSimilarMovieTitles;
    private String mDirectorText;
    private String mWritersText;
    private String mProducersText;
    private boolean isSimilar = false;
    private boolean favoriteUseDatabase = false;
    private boolean postersSortedByFavorite = false;
    private boolean doBackAnimation = true;
    private Drawable favoriteIconOutline;
    private Drawable favoriteIconFilled;
    private FavoriteMovieRepository db_repo;
    private FavoriteMovie favoriteMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        ButterKnife.bind(this);
        favoriteIconOutline = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_favorite_outline, null);
        favoriteIconFilled = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_favorite_filled, null);

        if (isSimilar) {
            savedInstanceState = null;
        }

        Intent intent = getIntent();
        MovieInfoResult passedInfo = intent.getParcelableExtra(getResources().getString(R.string.parcelable_intent_key));
        Bundle extras = intent.getExtras();

        if (intent.hasExtra(MOVIE_SORTED_BY_FAVS)) {
            postersSortedByFavorite = true;
        }

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

            // do database check here
            // if the movieID is in the database, load from database, set fav icon to filled
            // else, load from the api
            db_repo = new FavoriteMovieRepository(getApplication());
            favoriteMovie = db_repo.getFavoriteMovieDetails(mMovieId);
            
            if (favoriteMovie != null && !checkNetworkStatus()) {
                // if its a favorite AND there is no network get the info from the db
                favoriteUseDatabase = true;
                mFavoritesIcon.setImageDrawable(favoriteIconFilled);
                loadFromFavorite();
            } else {
                // if its a favorite but there is a network use it, might as well so you can get
                // the full experience. Still need to set the heart though.
                if (favoriteMovie != null) {
                    mFavoritesIcon.setImageDrawable(favoriteIconFilled);
                }

                loadFromApi();
            }

            toggleFavorites();

        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(TITLE_SAVE_STATE, mMovieTitle);
        outState.putString(MPAA_SAVE_STATE, mMpaa_rating);
        outState.putString(GENRES_SAVE_STATE, mGenreString);
        outState.putString(DIRECTOR_SAVE_STATE, mDirector.getText().toString());
        outState.putString(PRODUCERS_SAVE_STATE, mProducers.getText().toString());
        outState.putString(WRITERS_SAVE_STATE, mWriters.getText().toString());
        outState.putString(CAST_INTRO_SAVE_STATE, mCastIntro.getText().toString());

        if (!favoriteUseDatabase) {
            outState.putParcelable(MOVIE_API_RESULTS_SAVE_STATE, mMovieInfo);

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

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if(savedInstanceState != null) {
            getSupportActionBar().setTitle(savedInstanceState.getString(TITLE_SAVE_STATE));
            mTitle.setText(savedInstanceState.getString(TITLE_SAVE_STATE));
            mCastIntro.setText(savedInstanceState.getString(CAST_INTRO_SAVE_STATE));
            mDirector.setText(savedInstanceState.getString(DIRECTOR_SAVE_STATE));
            mProducers.setText(savedInstanceState.getString(PRODUCERS_SAVE_STATE));
            mWriters.setText(savedInstanceState.getString(WRITERS_SAVE_STATE));
            mCastIntro.setText(savedInstanceState.getString(CAST_INTRO_SAVE_STATE));
            mGenreString = savedInstanceState.getString(GENRES_SAVE_STATE);
            mMpaa_rating = savedInstanceState.getString(MPAA_SAVE_STATE);

            if (!favoriteUseDatabase) {
                mMovieInfo = savedInstanceState.getParcelable(MOVIE_API_RESULTS_SAVE_STATE);

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
            }

            loadFromSavedInstanceState(savedInstanceState);
        }
    }

    private void loadFromSavedInstanceState(Bundle savedInstanceState) {

        // hide the full screen poster, we don't need it
        mPosterBlocker.setVisibility(View.GONE);

        if (savedInstanceState != null) {
            setDetailViews();
            setCastMembers();
            setCrewMembers();
            similarMovieViewSetup();
            videosViewSetup();
            reviewsSetup();
        }
    }

    private void toggleFavorites() {
        // NOTE: Cast, Crew, Similar Movies, Videos, and Reviews RecyclerViews are not available
        // offline for favorite movies. Text will be shown instead.
        mFavoritesIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Drawable currentIcon = mFavoritesIcon.getDrawable();
                Drawable.ConstantState currentIconState = currentIcon.getConstantState();
                Drawable.ConstantState outlineIconState = favoriteIconOutline.getConstantState();

                if (currentIconState.equals(outlineIconState)) {
                    mFavoritesIcon.setImageDrawable(favoriteIconFilled);
                    // set this movie as a favorite. it's just the greatest ever.
                    saveFavorite();
                } else {
                    mFavoritesIcon.setImageDrawable(favoriteIconOutline);
                    // user doesn't like this movie anymore so get rid of it. its garbage anyway. the cinematography is total crap.
                    deleteFavorite();
                }
            }
        });
    }

    private void loadFromFavorite() {
        mMovieTitle = favoriteMovie.getOriginal_title();
        mLanguage = favoriteMovie.getOriginal_language();
        mTaglineText = favoriteMovie.getTagline();
        mOverviewText = favoriteMovie.getOverview();
        mVoteAverage = Double.parseDouble(favoriteMovie.getVote_average());
        mMpaa_rating = favoriteMovie.getMpaa_rating();
        mBackdropPath = favoriteMovie.getBackdrop_path();
        mPosterPath = favoriteMovie.getPoster_path();
        mGenreString = favoriteMovie.getGenres();
        mRuntimeText = favoriteMovie.getRuntime();
        mReleaseDateText = favoriteMovie.getRelease_date();
        mDirectorText = favoriteMovie.getDirector();
        mWritersText = favoriteMovie.getWriters();
        mProducersText = favoriteMovie.getProducers();
        mCastMembersString = favoriteMovie.getCast();
        mSimilarMovieTitles = favoriteMovie.getSimilar_movie_titles();

        updateUi(favoriteUseDatabase);
    }

    private void saveFavorite() {
        // save the poster and the backdrop images to the device because why the F not.
        String backdropImageName = mBackdropPath.substring(1);
        String posterImageName = mPosterPath.substring(1);
        String backdropUrl = getResources().getString(R.string.backdrop_url) + mBackdropPath;
        String posterUrl = getResources().getString(R.string.backdrop_url) + mPosterPath;

        Picasso.get().load(backdropUrl).into(picassoImageTarget(getApplicationContext(), APP_DIR, backdropImageName));
        Picasso.get().load(posterUrl).into(picassoImageTarget(getApplicationContext(), APP_DIR, posterImageName));

        favoriteMovie = new FavoriteMovie(mMovieId, mImdbId, mMovieTitle, mLanguage, mTaglineText, mOverviewText, String.valueOf(mVoteAverage), mMpaa_rating, mBackdropPath, mPosterPath, mGenreString, mRuntimeText, mReleaseDateText, mDirectorText, mWritersText, mProducersText, mCastMembersString, mSimilarMovieTitles);
        db_repo.insert(favoriteMovie);
    }

    private void deleteFavorite() {
        db_repo.delete(favoriteMovie);

        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir(APP_DIR, Context.MODE_PRIVATE);
        File posterImageFile = new File(directory, mPosterPath.substring(1));
        File backdropImageFile = new File(directory, mBackdropPath.substring(1));

        if(posterImageFile.exists()) {
            posterImageFile.delete();
        }

        if(backdropImageFile.exists()) {
            backdropImageFile.delete();
        }

        if (postersSortedByFavorite) {
            doBackAnimation = false;
        }
    }

    private void loadFromApi() {
        Call<MovieAllDetailsResult> call;
        ApiInterface api = ApiClient.getRetrofit().create(ApiInterface.class);
        call = api.getAllMovieDetails(mMovieId);
        call.enqueue(new Callback<MovieAllDetailsResult>() {
            @Override
            public void onResponse(Call<MovieAllDetailsResult> call, Response<MovieAllDetailsResult> response) {
                mMovieInfo = response.body();

                // get the cast, crew, similar movies, videos, and reviews lists
                mCast = mMovieInfo.getCredits().getCast();
                mCrew = mMovieInfo.getCredits().getCrew();
                mSimilarMovies = mMovieInfo.getSimilar().getResults();
                mVideos = mMovieInfo.getVideos().getResults();
                mReviews = mMovieInfo.getReviews().getResults();

                if (!favoriteUseDatabase) {
                    // get the genres and built the genre string
                    // the genres come back as an array of objects, genres: [ {...}, {...}, etc ]
                    mGenresList = mMovieInfo.getGenres();
                    for (MovieAllDetailsResult.MovieGenreResult genre : mGenresList) {
                        mGenreString += genre.getName() + ", ";
                    }

                    // fix comma formatting for genres string. needs a comma, then kill the last comma in the string
                    mGenreString = mGenreString.substring(0, mGenreString.length() - 2);

                    mImdbId = mMovieInfo.getImdb_id();
                    mLanguage = mMovieInfo.getOriginalLanguage();
                    mTaglineText = mMovieInfo.getTagline();
                    mOverviewText = mMovieInfo.getOverview();
                    mVoteAverage = mMovieInfo.getVoteAverage();
                    mBackdropPath = mMovieInfo.getBackdrop_path();
                    mReleaseDateText = mMovieInfo.getReleaseDate();

                    int runtime = mMovieInfo.getRuntime() != null ? mMovieInfo.getRuntime() : 0;
                    mRuntimeText = convertRuntime(runtime);
                    mReleaseDateText = convertReleaseDate(mReleaseDateText);

                    mMpaa_rating = getMpaaRating();

                    // similar movies but just the titles to save in the db.
                    mSimilarMovieTitles = "Similar Movies: ";
                    for (MovieAllDetailsResult.SimilarResults similar : mSimilarMovies) {
                        mSimilarMovieTitles += similar.getOriginalTitle() + ", ";
                    }
                    mSimilarMovieTitles = mSimilarMovieTitles.substring(0, mSimilarMovieTitles.length() - 2);

                    updateUi(favoriteUseDatabase);
                }
            }

            @Override
            public void onFailure(Call<MovieAllDetailsResult> call, Throwable t) {
                Log.i(TAG, "onFailure: there was an error: " + t);
            }
        });
    }

    private void updateUi(boolean isFavorite) {
        if (!isFavorite) {
            setCastMembers();
            setCrewMembers();
            similarMovieViewSetup();
            videosViewSetup();
            reviewsSetup();
        } else {
            castRecyclerView.setVisibility(View.GONE);
            crewRecyclerView.setVisibility(View.GONE);
            mVideosRecyclerView.setVisibility(View.GONE);
            similarMoviesRecyclerView.setVisibility(View.GONE);
            mReviewsRecyclerView.setVisibility(View.GONE);
            mCastHeader.setVisibility(View.GONE);
            mCrewHeader.setVisibility(View.GONE);
            mSimilarHeader.setVisibility(View.GONE);
            mVideosHeader.setVisibility(View.GONE);
            mReviewsHeader.setVisibility(View.GONE);
        }

        setDetailViews();
        hideImageBlocker();
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

    private void setDetailViews() {
        if (favoriteUseDatabase) {
            ContextWrapper cw = new ContextWrapper(getApplicationContext());
            File directory = cw.getDir(APP_DIR, Context.MODE_PRIVATE);
            File posterImageFile = new File(directory, mPosterPath.substring(1));
            File backdropImageFile = new File(directory, mBackdropPath.substring(1));
            Picasso.get().load(posterImageFile).placeholder(R.color.windowBackground).into(mPoster);
            Picasso.get().load(backdropImageFile).placeholder(R.color.windowBackground).into(mBackdrop);
        } else {
            String backdropUrl = getResources().getString(R.string.backdrop_url) + mBackdropPath;
            final String posterUrl = getResources().getString(R.string.backdrop_url) + mPosterPath;
            Picasso.get().load(backdropUrl).placeholder(R.color.windowBackground).into(mBackdrop);
            Picasso.get().load(posterUrl).placeholder(R.color.windowBackground).into(mPoster);
        }

        mTitle.setText(mMovieTitle);
        mGenres.setText(mGenreString);
        mRuntime.setText(mRuntimeText);
        mRatingText.setText(String.valueOf(mVoteAverage));
        mReleaseDate.setText(mReleaseDateText);
        mTagline.setText(mTaglineText);
        mOverview.setText(mOverviewText);
        mDirector.setText(mDirectorText);
        mWriters.setText(mWritersText);
        mProducers.setText(mProducersText);
        mCastIntro.setText(mCastMembersString);

        if (favoriteUseDatabase) {
            // makes the Director: Producers: etc bold
            setSpannableText(mProducersText, mProducers);
            setSpannableText(mWritersText, mWriters);
            setSpannableText(mDirectorText, mDirector);
            setSpannableText(mCastMembersString, mCastIntro);
        }

        if (mMpaa_rating != null && !mMpaa_rating.isEmpty()) {
            // if the movie is rated R make the background red. other wise its green, which
            // is on the TextView by default.
            if (mMpaa_rating.equals("R")) {
                mpaaBackground = ContextCompat.getDrawable(this, R.drawable.rounded_corners_mpaa_r);
                mMpaaRating.setBackground(mpaaBackground);
            }
            mMpaaRating.setText(mMpaa_rating);
        } else {
            mMpaaRating.setVisibility(View.GONE);
        }

        if (checkNetworkStatus()) {
            mIMDB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToImdb(mImdbId);
                }
            });
        }
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

    private void setSpannableText(String str, TextView v) {
        str = str.substring(0, str.length()-2);
        SpannableString strSpan = new SpannableString(str);
        strSpan.setSpan(new StyleSpan(Typeface.BOLD),0,getSpannableCount(str),0);
        v.setText(strSpan, TextView.BufferType.SPANNABLE);
    }

    private void setCastMembers() {

        if(mCreditsCast.size() == 0) {
            mCastMembersString = getResources().getString(R.string.details_staring) +   " ";

            // set the short line for staring cast members
            for (MovieAllDetailsResult.CastResults cast_member : mCast) {
                if (cast_member.getOrder() <= 3) {
                    mCastMembersString += cast_member.getName() + ", ";
                }

                if (cast_member.getOrder() <= 7) {
                    if(cast_member.getProfile_path() != null) {
                        mCreditsCast.add(new Credit(cast_member.getProfile_path(), cast_member.getName(), cast_member.getCharacter()));
                    }
                }
            }

            mCastMembersString = mCastMembersString.substring(0, mCastMembersString.length()-2);
        } else {
            mCastMembersString = mCastIntro.getText().toString();
        }
        setSpannableText(mCastMembersString, mCastIntro);
        castRecyclerViewSetup();
    }

    private void setCrewMembers() {

        if(mCreditsCrew.size() == 0) {
            mDirectorText = getResources().getString(R.string.details_director) +  " ";
            mWritersText = getResources().getString(R.string.details_writers) +  " ";;
            mProducersText = getResources().getString(R.string.details_producers) +  " ";

            int count = 0;
            for (MovieAllDetailsResult.CrewResults crew_member : mCrew) {
                if (count <=7 && crew_member.getCrew_image() != null && !crew_member.getJob().equals("Director")) {
                    // why are you incrementing up to you ask...
                    // because reasons. and science. see the director notes below. and ACTION!
                    mCreditsCrew.add(new Credit(crew_member.getCrew_image(), crew_member.getCrew_name(), crew_member.getJob()));
                    count++;
                }

                if(crew_member.getJob().equals("Story") || crew_member.getJob().equals("Screenplay")) {
                    mWritersText += crew_member.getCrew_name() + ", ";
                }

                if(crew_member.getJob().equals("Producer") || crew_member.getJob().equals("Executive Producer")) {
                    mProducersText += crew_member.getCrew_name() + ", ";
                }

                if(crew_member.getJob().equals("Director")) {
                    // i want a total of 8. if the directory was found i want them at the start
                    // if the directory was not found, i need to add them later
                    mDirectorText += crew_member.getCrew_name() + ", ";
                    mCreditsCrew.add(0,new Credit(crew_member.getCrew_image(), crew_member.getCrew_name(), crew_member.getJob()));
                }
            }

            // removes the last comma from the strings
            mWritersText = mWritersText.substring(0, mWritersText.length()-2);
            mProducersText = mProducersText.substring(0, mProducersText.length()-2);
            mDirectorText = mDirectorText.substring(0, mDirectorText.length()-2);

            // some movies have more than 1 director. Adjust the "Director" text to be "Directors" if so
            if (mDirectorText.contains(",")) {
                StringBuilder sb = new StringBuilder(mDirectorText);
                sb.insert(getSpannableCount(mDirectorText), "s");
                mDirectorText = sb.toString();
            }
        } else {
            mProducersText = mProducers.getText().toString();
            mWritersText = mWriters.getText().toString();
            mDirectorText = mDirector.getText().toString();
        }


        // makes the Director: Producers: etc bold
        setSpannableText(mProducersText, mProducers);
        setSpannableText(mWritersText, mWriters);
        setSpannableText(mDirectorText, mDirector);

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

        if (!doBackAnimation) {
            finish();
        } else {
            super.onBackPressed();
        }

    }

    private Target picassoImageTarget(Context context, final String imageDir, final String imageName) {
        ContextWrapper cw = new ContextWrapper(context);

        // path to /data/data/yourapp/app_imageDir
        final File directory = cw.getDir(imageDir, Context.MODE_PRIVATE);
        return new Target() {

            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final File myImageFile = new File(directory, imageName); // Create image file
                        FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(myImageFile);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                fos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                if (placeHolderDrawable != null) {}
            }
        };
    }
}
