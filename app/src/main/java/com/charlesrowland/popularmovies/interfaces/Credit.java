package com.charlesrowland.popularmovies.interfaces;

public class Credit {
    private String mProfilePath;
    private String mName;
    private String mCharacter;

    // this class is used to make a list of Credits for the Cast and Crew recycler view
    public Credit(String mProfilePath, String mName, String mCharacter) {
        this.mProfilePath = mProfilePath;
        this.mName = mName;
        this.mCharacter = mCharacter; // can also be the crew member job
    }

    public String getmProfilePath() {
        return mProfilePath;
    }

    public String getmName() {
        return mName;
    }

    public String getmCharacter() {
        return mCharacter;
    }
}
