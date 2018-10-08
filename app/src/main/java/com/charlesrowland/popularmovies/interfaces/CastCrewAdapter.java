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

import com.charlesrowland.popularmovies.MovieDetailsActivity;
import com.charlesrowland.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CastCrewAdapter extends RecyclerView.Adapter<CastCrewAdapter.CastCrewViewHolder>{
    private static final String TAG = CastCrewAdapter.class.getSimpleName() + " fart";

    private ArrayList<Credit> mCredits;

    public class CastCrewViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cast_image) ImageView profileImage;
        @BindView(R.id.name) TextView name;
        @BindView(R.id.character) TextView character;

        CastCrewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public CastCrewAdapter(ArrayList<Credit> arrayList) {
        mCredits = arrayList;
    }

    @Override
    public int getItemCount() {
        return mCredits.size();
    }

    @NonNull
    @Override
    public CastCrewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cast_crew_list_item, parent, false);
        return new CastCrewViewHolder((view));
    }

    @Override
    public void onBindViewHolder(@NonNull CastCrewViewHolder holder, int position) {
        // this is pretty self explanatory stuff. Sets all the list item views

        Credit currentCredit = mCredits.get(position);
        Resources res = holder.itemView.getContext().getResources();
        String profilePath = res.getString(R.string.poster_url) + currentCredit.getmProfilePath();
        String name = currentCredit.getmName();
        String character = currentCredit.getmCharacter();

        Picasso.get().load(profilePath).placeholder(R.color.windowBackground).into(holder.profileImage);
        holder.name.setText(name);
        holder.character.setText(character);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}
