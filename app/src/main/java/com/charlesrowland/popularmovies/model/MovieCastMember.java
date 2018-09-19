package com.charlesrowland.popularmovies.model;

public class MovieCastMember {
    private int mCastId;
    private int mOrder;
    private int mID;
    private String mCharacter;
    private String mCreditId;
    private String mName;
    private String mProfilePath;

    public MovieCastMember(int mCastId, int mOrder, int mID, String mCharacter, String mCreditId, String mName, String mProfilePath) {
        this.mCastId = mCastId;
        this.mOrder = mOrder;
        this.mID = mID;
        this.mCharacter = mCharacter;
        this.mCreditId = mCreditId;
        this.mName = mName;
        this.mProfilePath = mProfilePath;
    }

    public int getmCastId() {
        return mCastId;
    }

    public int getmOrder() {
        return mOrder;
    }

    public int getmID() {
        return mID;
    }

    public String getmCharacter() {
        return mCharacter;
    }

    public String getmCreditId() {
        return mCreditId;
    }

    public String getmName() {
        return mName;
    }

    public String getmProfilePath() {
        return mProfilePath;
    }
}
