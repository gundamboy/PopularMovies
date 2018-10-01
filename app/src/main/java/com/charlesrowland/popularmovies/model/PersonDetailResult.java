package com.charlesrowland.popularmovies.model;

import com.google.gson.annotations.SerializedName;

public class PersonDetailResult {

    @SerializedName("id")
    private int personId;

    @SerializedName("imdb_id")
    private String imdb_id;

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public String getImdb_id() {
        return imdb_id;
    }

    public void setImdb_id(String imdb_id) {
        this.imdb_id = imdb_id;
    }
}
