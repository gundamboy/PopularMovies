package com.charlesrowland.popularmovies.interfaces;

import com.charlesrowland.popularmovies.model.MovieCastMember;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MovieDbApiCastMembers {

    // should be movie/{movie_id}/credits
    @GET("movie/")
    Call<List<MovieCastMember>> getMovieCastMembers();
}
