package com.charlesrowland.popularmovies.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieAllDetailsResult {

    @SerializedName("backdrop_path")
    @Expose
    private String backdrop_path;

    @SerializedName("budget")
    @Expose
    private Integer budget;

    @SerializedName("genres")
    @Expose
    private List<MovieGenreResult> genres = null;

    @SerializedName("homepage")
    @Expose
    private String homepage;

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("imdb_id")
    @Expose
    private String imdb_id;

    @SerializedName("original_language")
    @Expose
    private String originalLanguage;

    @SerializedName("original_title")
    @Expose
    private String originalTitle;

    @SerializedName("overview")
    @Expose
    private String overview;

    @SerializedName("popularity")
    @Expose
    private Double popularity;

    @SerializedName("poster_path")
    @Expose
    private String posterPath;

    @SerializedName("release_date")
    @Expose
    private String releaseDate;

    @SerializedName("revenue")
    @Expose
    private Integer revenue;

    @SerializedName("runtime")
    @Expose
    private Integer runtime;

    @SerializedName("tagline")
    @Expose
    private String tagline;

    @SerializedName("vote_average")
    @Expose
    private Double voteAverage;

    @SerializedName("vote_count")
    @Expose
    private Integer voteCount;

    @SerializedName("videos")
    @Expose
    private List<MovieVideoResult> videos = null;

    @SerializedName("credits")
    @Expose
    private List<MovieCreditsCast> credits = null;

    @SerializedName("similar")
    @Expose
    private List<MovieSortingWrapper> similar = null;

    @SerializedName("reviews")
    @Expose
    private List<MovieReviewResults> reviews = null;

    // getters and setters
    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public Integer getBudget() {
        return budget;
    }

    public void setBudget(Integer budget) {
        this.budget = budget;
    }

    public List<MovieGenreResult> getGenres() {
        return genres;
    }

    public void setGenres(List<MovieGenreResult> genres) {
        this.genres = genres;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImdb_id() {
        return imdb_id;
    }

    public void setImdb_id(String imdb_id) {
        this.imdb_id = imdb_id;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Integer getRevenue() {
        return revenue;
    }

    public void setRevenue(Integer revenue) {
        this.revenue = revenue;
    }

    public Integer getRuntime() {
        return runtime;
    }

    public void setRuntime(Integer runtime) {
        this.runtime = runtime;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public List<MovieVideoResult> getVideos() {
        return videos;
    }

    public void setVideos(List<MovieVideoResult> videos) {
        this.videos = videos;
    }

    public List<MovieCreditsCast> getCredits() {
        return credits;
    }

    public void setCredits(List<MovieCreditsCast> credits) {
        this.credits = credits;
    }

    public List<MovieSortingWrapper> getSimilar() {
        return similar;
    }

    public void setSimilar(List<MovieSortingWrapper> similar) {
        this.similar = similar;
    }

    public List<MovieReviewResults> getReviews() {
        return reviews;
    }

    public void setReviews(List<MovieReviewResults> reviews) {
        this.reviews = reviews;
    }

    // classes for nested json
    public class MovieCreditsCast {

        @SerializedName("cast_id")
        @Expose
        private Integer cast_id;

        @SerializedName("character")
        @Expose
        private String character;

        @SerializedName("credit_id")
        @Expose
        private String credit_id;

        @SerializedName("gender")
        @Expose
        private Integer gender;

        @SerializedName("id")
        @Expose
        private Integer id;

        @SerializedName("name")
        @Expose
        private String name;

        @SerializedName("order")
        @Expose
        private Integer order;

        @SerializedName("profile_path")
        @Expose
        private String profile_path;

        public Integer getCast_id() {
            return cast_id;
        }

        public void setCast_id(Integer cast_id) {
            this.cast_id = cast_id;
        }

        public String getCharacter() {
            return character;
        }

        public void setCharacter(String character) {
            this.character = character;
        }

        public String getCredit_id() {
            return credit_id;
        }

        public void setCredit_id(String credit_id) {
            this.credit_id = credit_id;
        }

        public Integer getGender() {
            return gender;
        }

        public void setGender(Integer gender) {
            this.gender = gender;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getOrder() {
            return order;
        }

        public void setOrder(Integer order) {
            this.order = order;
        }

        public String getProfile_path() {
            return profile_path;
        }

        public void setProfile_path(String profile_path) {
            this.profile_path = profile_path;
        }
    }

    public class MovieGenreResult {

        @SerializedName("id")
        @Expose
        private Integer id;

        @SerializedName("name")
        @Expose
        private String name;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public class MovieReviewResults {
        String author;
        String content;
        String id;
        String url;

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public class MovieVideoResult {

        @SerializedName("id")
        @Expose
        private String id;

        @SerializedName("iso_639_1")
        @Expose
        private String iso_639_1;

        @SerializedName("iso_3166_1")
        @Expose
        private String iso_3166_1;

        @SerializedName("key")
        @Expose
        private String key;

        @SerializedName("name")
        @Expose
        private String name;

        @SerializedName("site")
        @Expose
        private String site;

        @SerializedName("size")
        @Expose
        private Integer size;

        @SerializedName("type")
        @Expose
        private String type;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIso_639_1() {
            return iso_639_1;
        }

        public void setIso_639_1(String iso_639_1) {
            this.iso_639_1 = iso_639_1;
        }

        public String getIso_3166_1() {
            return iso_3166_1;
        }

        public void setIso_3166_1(String iso_3166_1) {
            this.iso_3166_1 = iso_3166_1;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSite() {
            return site;
        }

        public void setSite(String site) {
            this.site = site;
        }

        public Integer getSize() {
            return size;
        }

        public void setSize(Integer size) {
            this.size = size;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

}
