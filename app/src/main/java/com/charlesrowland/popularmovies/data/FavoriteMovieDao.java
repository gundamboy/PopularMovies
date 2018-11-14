package com.charlesrowland.popularmovies.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface FavoriteMovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(FavoriteMovie favoriteMovie);

    @Update
    void update(FavoriteMovie favoriteMovie);

    @Delete
    void delete(FavoriteMovie favoriteMovie);

    @Query("SELECT * FROM movies WHERE movie_id = :movieId")
    FavoriteMovie getFavoriteMovieDetails(int movieId);

    @Query("SELECT * FROM movies")
    LiveData<List<FavoriteMovie>> getAllFavoriteMovies();
}
