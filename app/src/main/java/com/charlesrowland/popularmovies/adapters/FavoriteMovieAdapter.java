package com.charlesrowland.popularmovies.adapters;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.charlesrowland.popularmovies.MainActivity;
import com.charlesrowland.popularmovies.R;
import com.charlesrowland.popularmovies.data.FavoriteMovie;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoriteMovieAdapter extends RecyclerView.Adapter<FavoriteMovieAdapter.FavoriteMovieHolder> {
    private static final String TAG = FavoriteMovieAdapter.class.getSimpleName() + " fart";
    private List<FavoriteMovie> favoriteMovies = new ArrayList<>();
    private OnItemClickListener mListener;
    private static final String APP_DIR = "movie_night";

    public interface OnItemClickListener {
        void onItemClick(int position, int movieId, String posterPath, String movieTitle);
    }

    public void setOnClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public FavoriteMovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_poster_recycler_item, parent, false);
        return new FavoriteMovieHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final FavoriteMovieHolder holder, int position) {
        Resources res = holder.itemView.getContext().getResources();
        FavoriteMovie currentMovie = favoriteMovies.get(position);

        ContextWrapper cw = new ContextWrapper(holder.itemView.getContext());
        File directory = cw.getDir(APP_DIR, Context.MODE_PRIVATE);
        File posterImageFile = new File(directory, currentMovie.getPoster_path().substring(1));

        final String posterUrl = res.getString(R.string.poster_url) + currentMovie.getPoster_path();
        holder.mMovieId.setText(String.valueOf(currentMovie.getMovie_id()));
        holder.mPosterPath.setText(currentMovie.getPoster_path());
        holder.mTitleView.setText(currentMovie.getOriginal_title());

        Picasso.get().load(posterImageFile)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(holder.mPoster, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Picasso.get().load(posterUrl).into(holder.mPoster);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return favoriteMovies.size();
    }

    public void setData(List<FavoriteMovie> favoriteMovies) {
        this.favoriteMovies = favoriteMovies;
        notifyDataSetChanged();
    }

    class FavoriteMovieHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.movie_poster_view) ImageView mPoster;
        @BindView(R.id.theMovieId) TextView mMovieId;
        @BindView(R.id.titleView) TextView mTitleView;
        @BindView(R.id.posterpathView) TextView mPosterPath;

        public FavoriteMovieHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            // sends some info out of the adapter into the parent activity for handling clicks
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    int movieId = favoriteMovies.get(position).getMovie_id();
                    String posterPath = favoriteMovies.get(position).getPoster_path();
                    String movieTitle = favoriteMovies.get(position).getOriginal_title();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position, movieId, posterPath, movieTitle);
                    }
                }
            });
        }
    }
}
