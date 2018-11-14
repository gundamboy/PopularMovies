package com.charlesrowland.popularmovies.interfaces;

import com.charlesrowland.popularmovies.BuildConfig;
import com.charlesrowland.popularmovies.model.MovieAllDetailsResult;
import com.charlesrowland.popularmovies.model.MovieSortingWrapper;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiInterface {
    // gets the api key from the super secret hidden file
    String API_KEY = BuildConfig.ApiKey;

    // these methods are for retrofit 2. they do the api calls
    @GET("movie/popular?region=US&api_key="+API_KEY)
    Call<MovieSortingWrapper> getPopularMovies();

    @GET("movie/top_rated?api_key="+API_KEY)
    Call<MovieSortingWrapper> getTopRatedMovies();

    @GET("movie/{movie_id}?api_key="+API_KEY+"&append_to_response=release_dates,credits,videos,reviews,similar")
    Call<MovieAllDetailsResult> getAllMovieDetails(@Path("movie_id") int id);

}
