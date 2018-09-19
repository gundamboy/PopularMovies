package com.charlesrowland.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

public class MovieInfo implements Parcelable {
    private int mMovieId;
    private int mVoteCount;
    private long mVoteAverage;
    private long mPopularity;
    private String mOriginalTitle;
    private String mPosterPath;
    private String mBackdropPath;
    private String mOverview;
    private String mReleaseDate;
    private String mLanguage;

    // empty constructor
    public MovieInfo() {}

    // constructor for actual movie information
    public MovieInfo(int mMovieId, int mVoteCount, long mVoteAverage, long mPopularity, String mOriginalTitle, String mPosterPath, String mBackdropPath, String mOverview, String mReleaseDate, String mLanguage) {
        this.mMovieId = mMovieId;
        this.mVoteCount = mVoteCount;
        this.mVoteAverage = mVoteAverage;
        this.mPopularity = mPopularity;
        this.mOriginalTitle = mOriginalTitle;
        this.mPosterPath = mPosterPath;
        this.mBackdropPath = mBackdropPath;
        this.mOverview = mOverview;
        this.mReleaseDate = mReleaseDate;
        this.mLanguage = mLanguage;
    }

    // insert into parcel
    private MovieInfo(Parcel in) {
        mMovieId = in.readInt();
        mVoteCount = in.readInt();
        mVoteAverage = in.readLong();
        mPopularity = in.readLong();
        mOriginalTitle = in.readString();
        mPosterPath = in.readString();
        mBackdropPath = in.readString();
        mOverview = in.readString();
        mReleaseDate = in.readString();
        mLanguage = in.readString();
    }

    // flatten object to Parcel
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(mMovieId);
        parcel.writeInt(mVoteCount);
        parcel.writeLong(mVoteAverage);
        parcel.writeLong(mPopularity);
        parcel.writeString(mOriginalTitle);
        parcel.writeString(mPosterPath);
        parcel.writeString(mBackdropPath);
        parcel.writeString(mOverview);
        parcel.writeString(mReleaseDate);
        parcel.writeString(mLanguage);
    }

    // Creator, creates parcelable
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public MovieInfo createFromParcel(Parcel in) {
            return new MovieInfo(in);
        }

        public MovieInfo[] newArray(int size) {
            return new MovieInfo[size];
        }
    };

    // special objects... nah
    @Override
    public int describeContents() {
        return 0;
    }

    // get all the things!
    public int getmMovieId() {
        return mMovieId;
    }

    public int getmVoteCount() {
        return mVoteCount;
    }

    public long getmVoteAverage() {
        return mVoteAverage;
    }

    public long getmPopularity() {
        return mPopularity;
    }

    public String getmOriginalTitle() {
        return mOriginalTitle;
    }

    public String getmPosterPath() {
        return mPosterPath;
    }

    public String getmBackdropPath() {
        return mBackdropPath;
    }

    public String getmOverview() {
        return mOverview;
    }

    public String getmReleaseDate() {
        return mReleaseDate;
    }

    public String getmLanguage() {
        return mLanguage;
    }
}
