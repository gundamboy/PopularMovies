package com.charlesrowland.popularmovies.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieCreditsWrapper {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("cast")
    @Expose
    private List<MovieCreditsCast> cast = null;

    public Integer getId() {
        return id;
    }

    public List<MovieCreditsCast> getCast() {
        return cast;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCast(List<MovieCreditsCast> cast) {
        this.cast = cast;
    }
}
