package com.charlesrowland.popularmovies.interfaces;

import com.charlesrowland.popularmovies.BuildConfig;
import com.charlesrowland.popularmovies.model.MovieCreditsWrapper;
import com.charlesrowland.popularmovies.model.MovieDetailsResult;
import com.charlesrowland.popularmovies.model.MovieSortingWrapper;
import com.charlesrowland.popularmovies.model.MovieVideoWrapper;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiInterface {
    String API_KEY = BuildConfig.ApiKey;

    @GET("movie/popular?api_key="+API_KEY)
    Call<MovieSortingWrapper> getPopularMovies();

    @GET("movie/top_rated?api_key="+API_KEY)
    Call<MovieSortingWrapper> getTopRatedMovies();

    @GET("movie/{movie_id}/videos?api_key="+API_KEY)
    Call<MovieVideoWrapper> getMovieTrailer(@Path("movie_id") int id);

    @GET("movie/{movie_id}/credits?api_key="+API_KEY)
    Call<MovieCreditsWrapper> getMovieCredits(@Path("movie_id") int id);

    @GET("movie/{movie_id}/similar?api_key="+API_KEY)
    Call<MovieSortingWrapper> getSimilarMovies(@Path("movie_id") int id);

    @GET("movie/{movie_id}?api_key="+API_KEY)
    Call<MovieDetailsResult> getMovieDetails(@Path("movie_id") int id);

    @GET("movie/{movie_id}/reviews?api_key="+API_KEY)
    Call getMovieReviews(@Path("movie_id") int id);

}
