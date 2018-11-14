package com.charlesrowland.popularmovies.fragments;


import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.charlesrowland.popularmovies.R;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class BottomReviewFragment extends BottomSheetDialogFragment {
    @BindView(R.id.bs_author_header) TextView bs_author_header;
    @BindView(R.id.bs_review_author) TextView bs_review_author;
    @BindView(R.id.bs_review_content) TextView bs_review_content;

    public BottomReviewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.review_bottom_sheet, container, false);
    }

}
