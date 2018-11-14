package com.charlesrowland.popularmovies.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

@Database(entities = FavoriteMovie.class, version = 1)
public abstract class FavoriteMovieDatabase extends RoomDatabase {

    private static FavoriteMovieDatabase instance;

    public abstract FavoriteMovieDao favoriteMovieDao();

    public static synchronized FavoriteMovieDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), FavoriteMovieDatabase.class,
                    "favorite_movie_database").fallbackToDestructiveMigration().build();

        }

        return instance;
    }
}
