package com.charlesrowland.popularmovies.interfaces;

import com.charlesrowland.popularmovies.BuildConfig;
import com.charlesrowland.popularmovies.model.MovieAllDetailsResult;
import com.charlesrowland.popularmovies.model.MovieSortingWrapper;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiInterface {
    String API_KEY = BuildConfig.ApiKey;

    @GET("movie/popular?api_key="+API_KEY)
    Call<MovieSortingWrapper> getPopularMovies();

    @GET("movie/top_rated?api_key="+API_KEY)
    Call<MovieSortingWrapper> getTopRatedMovies();

    @GET("movie/{movie_id}?api_key="+API_KEY+"&append_to_response=videos,credits,similar,reviews")
    Call<MovieAllDetailsResult> getAllMovieDetails(@Path("movie_id") int id);

}
