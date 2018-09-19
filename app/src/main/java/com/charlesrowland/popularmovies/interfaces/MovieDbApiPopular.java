package com.charlesrowland.popularmovies.interfaces;

import com.charlesrowland.popularmovies.BuildConfig;
import com.charlesrowland.popularmovies.model.MovieInfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieDbApiPopular {

    @GET("movie/popular")
    Call<List<MovieInfo>> getPopularMovies();
}
