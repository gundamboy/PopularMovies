package com.charlesrowland.popularmovies.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface FavoriteMovieDao {

    @Insert
    void insert(FavoriteMovie favoriteMovie);

    @Update
    void update(FavoriteMovie favoriteMovie);

    @Delete
    void delete(FavoriteMovie favoriteMovie);

    @Query("SELECT movie_id FROM favorite_movies_table")
    void selectMovieIds();

    @Query("SELECT * FROM favorite_movies_table WHERE movie_id = :movieId")
    LiveData<List<FavoriteMovie>> getFavotireMovie(int movieId);

    @Query("SELECT * FROM favorite_movies_table")
    LiveData<List<FavoriteMovie>> getAllMovies();
}
