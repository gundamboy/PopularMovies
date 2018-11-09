package com.charlesrowland.popularmovies.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "movies")
public class FavoriteMovie {

    @PrimaryKey
    private int movie_id;
    private String imdb_id;
    private String original_title;
    private String original_language;
    private String tagline;
    private String overview;
    private String vote_average;
    private String mpaa_rating;
    private String backdrop_path;
    private String poster_path;
    private String genres;
    private String runtime;
    private String release_date;
    private String cast_members;
    private String crew_members;
    private String similar_movie_titles;

    public FavoriteMovie(int movie_id, String imdb_id, String original_title, String original_language, String tagline, String overview, String vote_average, String mpaa_rating, String backdrop_path, String poster_path, String genres, String runtime, String release_date, String cast_members, String crew_members, String similar_movie_titles) {
        this.movie_id = movie_id;
        this.imdb_id = imdb_id;
        this.original_title = original_title;
        this.original_language = original_language;
        this.tagline = tagline;
        this.overview = overview;
        this.vote_average = vote_average;
        this.mpaa_rating = mpaa_rating;
        this.backdrop_path = backdrop_path;
        this.poster_path = poster_path;
        this.genres = genres;
        this.runtime = runtime;
        this.release_date = release_date;
        this.cast_members = cast_members;
        this.crew_members = crew_members;
        this.similar_movie_titles = similar_movie_titles;
    }

    public int getMovie_id() {
        return movie_id;
    }

    public String getImdb_id() {
        return imdb_id;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public String getTagline() {
        return tagline;
    }

    public String getOverview() {
        return overview;
    }

    public String getVote_average() {
        return vote_average;
    }

    public String getMpaa_rating() {
        return mpaa_rating;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getGenres() {
        return genres;
    }

    public String getRuntime() {
        return runtime;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getCast_members() {
        return cast_members;
    }

    public String getCrew_members() {
        return crew_members;
    }

    public String getSimilar_movie_titles() {
        return similar_movie_titles;
    }
}
