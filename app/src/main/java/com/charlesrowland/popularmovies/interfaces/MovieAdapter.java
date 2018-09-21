package com.charlesrowland.popularmovies.interfaces;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.charlesrowland.popularmovies.R;
import com.charlesrowland.popularmovies.model.MovieInfoResult;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieHolder> {
    private static final String TAG = MovieAdapter.class.getSimpleName() + " fart";
    List<MovieInfoResult> results;

    public MovieAdapter(List<MovieInfoResult> results) {
        this.results = results;
    }

    @NonNull
    @Override
    public MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_poster_recycler_item, parent, false);
        return new MovieHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieHolder holder, int position) {
        String posterUrl = "https://image.tmdb.org/t/p/w185"+results.get(position).getPosterPath();
        Picasso.get().load(posterUrl).placeholder(R.color.colorAccent).into(holder.poster);
    }

    public void setData(List<MovieInfoResult> results) {
        this.results = results;
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    class MovieHolder extends RecyclerView.ViewHolder {
        ImageView poster;

        public MovieHolder(View itemView) {
            super(itemView);

            poster = itemView.findViewById(R.id.movie_poster);
        }
    }

}

