package com.charlesrowland.popularmovies.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.charlesrowland.popularmovies.R;
import com.charlesrowland.popularmovies.model.MovieAllDetailsResult;
import com.charlesrowland.popularmovies.model.MovieInfoResult;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewHolder> {
    private static final String TAG = ReviewsAdapter.class.getSimpleName() + " fart";
    List<MovieAllDetailsResult.ReviewResults> results;
    private ReviewsAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position, String author, String content);
    }

    public void setOnClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public ReviewsAdapter(List<MovieAllDetailsResult.ReviewResults> results) {
        this.results = results;
    }

    @NonNull
    @Override
    public ReviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_list_item, parent, false);
        Log.i(TAG, "onCreateViewHolder: hello????");
        return new ReviewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final ReviewHolder holder, int position) {
        holder.reviewAuthor.setText(results.get(position).getAuthor());
        holder.reviewContent.setText(results.get(position).getContent());
    }

    public void setData(List<MovieAllDetailsResult.ReviewResults> results) {
        this.results = results;
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    class ReviewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.review_author) TextView reviewAuthor;
        @BindView(R.id.review_content) TextView reviewContent;
        @BindView(R.id.readMoreButton) Button readMore;

        public ReviewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            readMore.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION) {
                        String author = reviewAuthor.getText().toString();
                        String content = reviewContent.getText().toString();
                        listener.onItemClick(position, author, content);
                    }
                }
            });
        }
    }
}
