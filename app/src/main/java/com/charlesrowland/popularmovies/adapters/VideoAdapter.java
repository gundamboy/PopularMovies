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
import com.charlesrowland.popularmovies.model.MovieAllDetailsResult;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoHolder> {
    private static final String TAG = VideoAdapter.class.getSimpleName() + " fart";
    private List<MovieAllDetailsResult.VideoResults> results;
    private VideoAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position, String youtubeUrl, String video_key);
    }

    public void setOnClickListener(VideoAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public VideoAdapter(List<MovieAllDetailsResult.VideoResults> results) {
        this.results = results;
    }

    @NonNull
    @Override
    public VideoAdapter.VideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_list_item, parent, false);
        return new VideoHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoAdapter.VideoHolder holder, int position) {
        Resources res = holder.itemView.getContext().getResources();
        final String thumbnailUrl = res.getString(R.string.youtube_image_url, results.get(position).getKey());
        final String videoUrl = res.getString(R.string.youtube_video_url, results.get(position).getKey());
        String vkey = results.get(position).getKey();

        holder.trailer_title.setText(results.get(position).getName());
        holder.youtube_url.setText(videoUrl);
        holder.video_key.setText(vkey);
        Picasso.get().load(thumbnailUrl).placeholder(R.color.windowBackground).into(holder.trailer_image);
    }

    public void setData(List<MovieAllDetailsResult.VideoResults> results) {
        this.results = results;
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    class VideoHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.trailer_image) ImageView trailer_image;
        @BindView(R.id.trailer_title) TextView trailer_title;
        @BindView(R.id.youtube_url) TextView youtube_url;
        @BindView(R.id.video_key) TextView video_key;

        public VideoHolder(View itemView, final VideoAdapter.OnItemClickListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            // send info out to handle clicks.
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    String url = youtube_url.getText().toString();
                    String vkey = video_key.getText().toString();

                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position, url, vkey);
                    }
                }
            });
        }
    }
}
