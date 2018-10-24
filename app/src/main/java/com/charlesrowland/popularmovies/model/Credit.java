package com.charlesrowland.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Credit implements Parcelable {
    private String mProfilePath;
    private String mName;
    private String mCharacter;

    // this class is used to make a list of Credits for the Cast and Crew recycler view
    public Credit(String mProfilePath, String mName, String mCharacter) {
        this.mProfilePath = mProfilePath;
        this.mName = mName;
        this.mCharacter = mCharacter; // can also be the crew member job
    }

    protected Credit(Parcel in) {
        mProfilePath = in.readString();
        mName = in.readString();
        mCharacter = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mProfilePath);
        dest.writeString(mName);
        dest.writeString(mCharacter);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Credit> CREATOR = new Creator<Credit>() {
        @Override
        public Credit createFromParcel(Parcel in) {
            return new Credit(in);
        }

        @Override
        public Credit[] newArray(int size) {
            return new Credit[size];
        }
    };

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
