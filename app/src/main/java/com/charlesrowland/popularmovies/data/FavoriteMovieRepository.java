package com.charlesrowland.popularmovies.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class FavoriteMovieRepository {
    private FavoriteMovieDao favoriteMovieDao;
    private LiveData<List<FavoriteMovie>> allFavoriteMovies;

    public FavoriteMovieRepository(Application application) {
        FavoriteMovieDatabase database = FavoriteMovieDatabase.getInstance(application);
        favoriteMovieDao = database.favoriteMovieDao();
        allFavoriteMovies = favoriteMovieDao.getAllFavoriteMovies();
    }

    public void insert(FavoriteMovie favoriteMovie) {
        new InsertFavoriteAsyncTask(favoriteMovieDao).execute(favoriteMovie);
    }

    public void update(FavoriteMovie favoriteMovie) {
        new UpdateFavoriteAsyncTask(favoriteMovieDao).execute(favoriteMovie);
    }

    public void delete(FavoriteMovie favoriteMovie) {
        new DeleteFavoriteAsyncTask(favoriteMovieDao).execute(favoriteMovie);
    }

    public LiveData<List<FavoriteMovie>> getAllFavoriteMovies() {
        return allFavoriteMovies;
    }

    public static class InsertFavoriteAsyncTask extends AsyncTask<FavoriteMovie, Void, Void> {
        private FavoriteMovieDao favoriteMovieDao;

        private InsertFavoriteAsyncTask(FavoriteMovieDao favoriteMovieDao) {
            this.favoriteMovieDao = favoriteMovieDao;
        }

        @Override
        protected Void doInBackground(FavoriteMovie... favoriteMovies) {
            favoriteMovieDao.insert(favoriteMovies[0]);
            return null;
        }
    }

    public static class UpdateFavoriteAsyncTask extends AsyncTask<FavoriteMovie, Void, Void> {
        private FavoriteMovieDao favoriteMovieDao;

        private UpdateFavoriteAsyncTask(FavoriteMovieDao favoriteMovieDao) {
            this.favoriteMovieDao = favoriteMovieDao;
        }

        @Override
        protected Void doInBackground(FavoriteMovie... favoriteMovies) {
            favoriteMovieDao.update(favoriteMovies[0]);
            return null;
        }
    }

    public static class DeleteFavoriteAsyncTask extends AsyncTask<FavoriteMovie, Void, Void> {
        private FavoriteMovieDao favoriteMovieDao;

        private DeleteFavoriteAsyncTask(FavoriteMovieDao favoriteMovieDao) {
            this.favoriteMovieDao = favoriteMovieDao;
        }

        @Override
        protected Void doInBackground(FavoriteMovie... favoriteMovies) {
            favoriteMovieDao.delete(favoriteMovies[0]);
            return null;
        }
    }
}
