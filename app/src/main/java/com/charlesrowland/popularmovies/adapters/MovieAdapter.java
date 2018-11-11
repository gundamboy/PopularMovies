package com.charlesrowland.popularmovies.adapters;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.charlesrowland.popularmovies.R;
import com.charlesrowland.popularmovies.model.MovieInfoResult;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieHolder> {
    private static final String TAG = MovieAdapter.class.getSimpleName() + " fart";
    List<MovieInfoResult> apiResults;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position, int movieId, String posterPath, String movieTitle);
    }

    public void setOnClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public MovieAdapter(List<MovieInfoResult> results) {
        this.apiResults = results;
    }

    @NonNull
    @Override
    public MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_poster_recycler_item, parent, false);
        return new MovieHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final MovieHolder holder, int position) {
        // set all the views... one view.. just the poster.
        Resources res = holder.itemView.getContext().getResources();
        final String posterUrl = res.getString(R.string.poster_url) + apiResults.get(position).getPosterPath();
        holder.mMovieId.setText(String.valueOf(apiResults.get(position).getMovieId()));
        holder.mPosterPath.setText(apiResults.get(position).getPosterPath());
        holder.mTitleView.setText(apiResults.get(position).getOriginalTitle());

        Picasso.get().load(posterUrl)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(holder.poster, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Picasso.get().load(posterUrl).into(holder.poster);
                    }
                });
    }

    public void setData(List<MovieInfoResult> results) {
        this.apiResults = results;
    }

    @Override
    public int getItemCount() {
        return apiResults.size();
    }

    class MovieHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.movie_poster_view) ImageView poster;
        @BindView(R.id.theMovieId) TextView mMovieId;
        @BindView(R.id.titleView) TextView mTitleView;
        @BindView(R.id.posterpathView) TextView mPosterPath;

        public MovieHolder(final View itemView, final OnItemClickListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            // sends some info out of the adapter into the parent activity for handling clicks
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    int movieId = Integer.parseInt(mMovieId.getText().toString());
                    String posterPath = mPosterPath.getText().toString();
                    String movieTitle = mTitleView.getText().toString();

                    if (position != RecyclerView.NO_POSITION) {
                        //listener.onItemClick(position, db_id);
                        listener.onItemClick(position, movieId, posterPath, movieTitle);
                    }
                }
            });
        }
    }
}

