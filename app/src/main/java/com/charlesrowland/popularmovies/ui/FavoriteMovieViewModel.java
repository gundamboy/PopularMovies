package com.charlesrowland.popularmovies.ui;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.charlesrowland.popularmovies.data.FavoriteMovie;
import com.charlesrowland.popularmovies.data.FavoriteMovieRepository;

import java.util.List;

public class FavoriteMovieViewModel extends AndroidViewModel {
    private FavoriteMovieRepository repository;
    private LiveData<List<FavoriteMovie>> allFavoriteMovies;
    private List<FavoriteMovie> favoriteMovie;

    public FavoriteMovieViewModel(@NonNull Application application) {
        super(application);
        repository = new FavoriteMovieRepository(application);
        allFavoriteMovies = repository.getAllFavoriteMovies();
    }

    public void insert(FavoriteMovie favoriteMovie) {
        repository.insert(favoriteMovie);
    }

    public void update(FavoriteMovie favoriteMovie) {
        repository.update(favoriteMovie);
    }

    public void delete(FavoriteMovie favoriteMovie) {
        repository.delete(favoriteMovie);
    }

    public LiveData<List<FavoriteMovie>> getAllFavoriteMovies() {
        return allFavoriteMovies;
    }

    public List<FavoriteMovie> getFavoriteMovieDetails(int movie_id) {
        return favoriteMovie;
    }

}
