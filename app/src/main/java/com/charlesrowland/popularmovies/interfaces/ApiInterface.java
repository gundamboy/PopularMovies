package com.charlesrowland.popularmovies.interfaces;

import com.charlesrowland.popularmovies.model.Movie;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("movie/popular")
    Call<Movie> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/top_rated")
    Call getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("movie/{movie_id}/videos")
    Call getMovieTrailer(@Path("movie_id") int id, @Query("api_key") String apiKey);

    @GET("movie/{movie_id}/credits")
    Call getMovieCredits(@Path("movie_id") int id, @Query("api_key") String apiKey);

}
