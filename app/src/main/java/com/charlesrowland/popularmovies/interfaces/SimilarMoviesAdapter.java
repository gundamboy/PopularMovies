package com.charlesrowland.popularmovies.interfaces;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.charlesrowland.popularmovies.R;
import com.charlesrowland.popularmovies.model.MovieAllDetailsResult;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SimilarMoviesAdapter extends RecyclerView.Adapter<SimilarMoviesAdapter.MovieHolder> {
    private static final String TAG = SimilarMoviesAdapter.class.getSimpleName() + " fart";
    List<MovieAllDetailsResult.SimilarResults> results;
    private SimilarMoviesAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position, int theMovieId, String title);
    }

    public void setOnClickListener(SimilarMoviesAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public SimilarMoviesAdapter(List<MovieAllDetailsResult.SimilarResults> results) {
        this.results = results;
    }

    @NonNull
    @Override
    public SimilarMoviesAdapter.MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_poster_recycler_item, parent, false);
        return new SimilarMoviesAdapter.MovieHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SimilarMoviesAdapter.MovieHolder holder, int position) {
        Resources res = holder.itemView.getContext().getResources();

        String posterUrl = res.getString(R.string.poster_url) + results.get(position).getPosterPath();
        Picasso.get().load(posterUrl).placeholder(R.color.colorAccent).into(holder.poster);

        holder.theMovieId.setText(String.valueOf(results.get(position).getMovieId()));
        holder.titleView.setText(results.get(position).getOriginalTitle());
    }

    public void setData(List<MovieAllDetailsResult.SimilarResults> results) {
        this.results = results;
    }

    @Override
    public int getItemCount() {
//        return results.size();
        return 6;
    }

    class MovieHolder extends RecyclerView.ViewHolder {
        ImageView poster;
        TextView theMovieId;
        TextView titleView;

        public MovieHolder(View itemView, final SimilarMoviesAdapter.OnItemClickListener listener) {
            super(itemView);

            poster = itemView.findViewById(R.id.movie_poster);
            theMovieId = itemView.findViewById(R.id.theMovieId);
            titleView = itemView.findViewById(R.id.titleView);


            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    String theId = theMovieId.getText().toString();
                    String title = titleView.getText().toString();

                    if (position != RecyclerView.NO_POSITION) {
                        //listener.onItemClick(position, db_id);
                        listener.onItemClick(position, Integer.parseInt(theId), title);
                    }
                }
            });
        }
    }

}


