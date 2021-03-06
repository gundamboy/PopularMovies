package com.charlesrowland.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.sql.Wrapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This holds results from the retrofit api calls. This is AAAAALLLLLL the movie info you could
 * every need
 */
public class MovieAllDetailsResult implements Parcelable {

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
    private Integer movieId;

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

    @SerializedName("release_dates")
    @Expose
    private ReleaseDatesWrapper singleMovieReleaseDates = null;

    @SerializedName("credits")
    @Expose
    private CreditsWrapper credits = null;

    @SerializedName("videos")
    @Expose
    private VideosWrapper videos = null;

    @SerializedName("similar")
    @Expose
    private SimilarWrapper similar = null;

    @SerializedName("reviews")
    @Expose
    private ReviewsWrapper reviews = null;

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

    public Integer getmovieId() {
        return movieId;
    }

    public void setmovieId(Integer id) {
        this.movieId = id;
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

    public ReleaseDatesWrapper getRelease_dates() {
        return singleMovieReleaseDates;
    }

    public void setRelease_dates(ReleaseDatesWrapper singleMovieReleaseDates) {
        this.singleMovieReleaseDates = singleMovieReleaseDates;
    }

    public CreditsWrapper getCredits() {
        return credits;
    }

    public void setCredits(CreditsWrapper credits) {
        this.credits = credits;
    }

    public VideosWrapper getVideos() {
        return videos;
    }

    public void setVideos(VideosWrapper videos) {
        this.videos = videos;
    }

    public SimilarWrapper getSimilar() {
        return similar;
    }

    public void setSimilar(SimilarWrapper similar) {
        this.similar = similar;
    }

    public ReviewsWrapper getReviews() {
        return reviews;
    }

    public void setReviews(ReviewsWrapper reviews) {
        this.reviews = reviews;
    }

    // classes for nested json
    // private List<MovieGenreResult> genres = null;
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

    public class CreditsWrapper {
        @SerializedName("cast")
        @Expose
        private List<CastResults> cast = null;

        @SerializedName("crew")
        @Expose
        private List<CrewResults> crew = null;

        public List<CastResults> getCast() {
            return cast;
        }

        public void setCast(List<CastResults> cast) {
            this.cast = cast;
        }

        public List<CrewResults> getCrew() {
            return crew;
        }

        public void setCrew(List<CrewResults> crew) {
            this.crew = crew;
        }
    }

    public static class CastResults implements Parcelable {
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

        protected CastResults(Parcel in) {
            if (in.readByte() == 0) {
                cast_id = null;
            } else {
                cast_id = in.readInt();
            }
            character = in.readString();
            credit_id = in.readString();
            if (in.readByte() == 0) {
                gender = null;
            } else {
                gender = in.readInt();
            }
            if (in.readByte() == 0) {
                id = null;
            } else {
                id = in.readInt();
            }
            name = in.readString();
            if (in.readByte() == 0) {
                order = null;
            } else {
                order = in.readInt();
            }
            profile_path = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            if (cast_id == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(cast_id);
            }
            dest.writeString(character);
            dest.writeString(credit_id);
            if (gender == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(gender);
            }
            if (id == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(id);
            }
            dest.writeString(name);
            if (order == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(order);
            }
            dest.writeString(profile_path);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<CastResults> CREATOR = new Creator<CastResults>() {
            @Override
            public CastResults createFromParcel(Parcel in) {
                return new CastResults(in);
            }

            @Override
            public CastResults[] newArray(int size) {
                return new CastResults[size];
            }
        };

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

    public static class CrewResults implements Parcelable {
        @SerializedName("job")
        @Expose
        private String job;

        @SerializedName("name")
        @Expose
        private String crew_name;

        @SerializedName("profile_path")
        @Expose
        private String crew_image;

        protected CrewResults(Parcel in) {
            job = in.readString();
            crew_name = in.readString();
            crew_image = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(job);
            dest.writeString(crew_name);
            dest.writeString(crew_image);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<CrewResults> CREATOR = new Creator<CrewResults>() {
            @Override
            public CrewResults createFromParcel(Parcel in) {
                return new CrewResults(in);
            }

            @Override
            public CrewResults[] newArray(int size) {
                return new CrewResults[size];
            }
        };

        public String getJob() {
            return job;
        }

        public void setJob(String job) {
            this.job = job;
        }

        public String getCrew_name() {
            return crew_name;
        }

        public void setCrew_name(String crew_name) {
            this.crew_name = crew_name;
        }

        public String getCrew_image() {
            return crew_image;
        }

        public void setCrew_image(String crew_image) {
            this.crew_image = crew_image;
        }
    }

    public class VideosWrapper {
        @SerializedName("results")
        @Expose
        private List<VideoResults> results = null;

        public List<VideoResults> getResults() {
            return results;
        }

        public void setResults(List<VideoResults> results) {
            this.results = results;
        }
    }

    public static class VideoResults implements Parcelable {

        @SerializedName("id")
        @Expose
        private String videoId;

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

        protected VideoResults(Parcel in) {
            videoId = in.readString();
            iso_639_1 = in.readString();
            iso_3166_1 = in.readString();
            key = in.readString();
            name = in.readString();
            site = in.readString();
            if (in.readByte() == 0) {
                size = null;
            } else {
                size = in.readInt();
            }
            type = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(videoId);
            dest.writeString(iso_639_1);
            dest.writeString(iso_3166_1);
            dest.writeString(key);
            dest.writeString(name);
            dest.writeString(site);
            if (size == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(size);
            }
            dest.writeString(type);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<VideoResults> CREATOR = new Creator<VideoResults>() {
            @Override
            public VideoResults createFromParcel(Parcel in) {
                return new VideoResults(in);
            }

            @Override
            public VideoResults[] newArray(int size) {
                return new VideoResults[size];
            }
        };

        public String getId() {
            return videoId;
        }

        public void setId(String id) {
            this.videoId = id;
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

    public class SimilarWrapper {
        @SerializedName("results")
        @Expose
        private List<SimilarResults> results = null;

        public List<SimilarResults> getResults() {
            return results;
        }

        public void setResults(List<SimilarResults> results) {
            this.results = results;
        }
    }

    public static class SimilarResults implements Parcelable {
        @SerializedName("vote_count")
        @Expose
        private Integer voteCount;

        @SerializedName("id")
        @Expose
        private Integer movieId;

        @SerializedName("video")
        @Expose
        private Boolean video;

        @SerializedName("vote_average")
        @Expose
        private Double voteAverage;

        @SerializedName("title")
        @Expose
        private String title;

        @SerializedName("popularity")
        @Expose
        private Double popularity;

        @SerializedName("poster_path")
        @Expose
        private String posterPath;

        @SerializedName("original_language")
        @Expose
        private String originalLanguage;

        @SerializedName("original_title")
        @Expose
        private String originalTitle;

        @SerializedName("genre_ids")
        @Expose
        private List<Integer> genreIds = null;

        @SerializedName("backdrop_path")
        @Expose
        private String backdropPath;

        @SerializedName("adult")
        @Expose
        private Boolean adult;

        @SerializedName("overview")
        @Expose
        private String overview;

        @SerializedName("release_date")
        @Expose
        private String releaseDate;

        private Map<String, Object> additionalProperties = new HashMap<String, Object>();

        protected SimilarResults(Parcel in) {
            if (in.readByte() == 0) {
                voteCount = null;
            } else {
                voteCount = in.readInt();
            }
            if (in.readByte() == 0) {
                movieId = null;
            } else {
                movieId = in.readInt();
            }
            byte tmpVideo = in.readByte();
            video = tmpVideo == 0 ? null : tmpVideo == 1;
            if (in.readByte() == 0) {
                voteAverage = null;
            } else {
                voteAverage = in.readDouble();
            }
            title = in.readString();
            if (in.readByte() == 0) {
                popularity = null;
            } else {
                popularity = in.readDouble();
            }
            posterPath = in.readString();
            originalLanguage = in.readString();
            originalTitle = in.readString();
            backdropPath = in.readString();
            byte tmpAdult = in.readByte();
            adult = tmpAdult == 0 ? null : tmpAdult == 1;
            overview = in.readString();
            releaseDate = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            if (voteCount == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(voteCount);
            }
            if (movieId == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(movieId);
            }
            dest.writeByte((byte) (video == null ? 0 : video ? 1 : 2));
            if (voteAverage == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeDouble(voteAverage);
            }
            dest.writeString(title);
            if (popularity == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeDouble(popularity);
            }
            dest.writeString(posterPath);
            dest.writeString(originalLanguage);
            dest.writeString(originalTitle);
            dest.writeString(backdropPath);
            dest.writeByte((byte) (adult == null ? 0 : adult ? 1 : 2));
            dest.writeString(overview);
            dest.writeString(releaseDate);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<SimilarResults> CREATOR = new Creator<SimilarResults>() {
            @Override
            public SimilarResults createFromParcel(Parcel in) {
                return new SimilarResults(in);
            }

            @Override
            public SimilarResults[] newArray(int size) {
                return new SimilarResults[size];
            }
        };

        public Integer getVoteCount() {
            return voteCount;
        }

        public void setVoteCount(Integer voteCount) {
            this.voteCount = voteCount;
        }

        public Integer getMovieId() {
            return movieId;
        }

        public void setMovieId(Integer id) {
            this.movieId = id;
        }

        public Boolean getVideo() {
            return video;
        }

        public void setVideo(Boolean video) {
            this.video = video;
        }


        public Double getVoteAverage() {
            return voteAverage;
        }

        public void setVoteAverage(Double voteAverage) {
            this.voteAverage = voteAverage;
        }


        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
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


        public List<Integer> getGenreIds() {
            return genreIds;
        }

        public void setGenreIds(List<Integer> genreIds) {
            this.genreIds = genreIds;
        }


        public String getBackdropPath() {
            return backdropPath;
        }

        public void setBackdropPath(String backdropPath) {
            this.backdropPath = backdropPath;
        }


        public Boolean getAdult() {
            return adult;
        }

        public void setAdult(Boolean adult) {
            this.adult = adult;
        }

        public String getOverview() {
            return overview;
        }

        public void setOverview(String overview) {
            this.overview = overview;
        }

        public String getReleaseDate() {
            return releaseDate;
        }

        public void setReleaseDate(String releaseDate) {
            this.releaseDate = releaseDate;
        }

        public Map<String, Object> getAdditionalProperties() {
            return this.additionalProperties;
        }

        public void setAdditionalProperty(String name, Object value) {
            this.additionalProperties.put(name, value);
        }
    }

    public class ReviewsWrapper {
        @SerializedName("results")
        @Expose
        private List<ReviewResults> results = null;

        public List<ReviewResults> getResults() {
            return results;
        }

        public void setResults(List<ReviewResults> results) {
            this.results = results;
        }
    }

    public static class ReviewResults implements Parcelable {
        @SerializedName("author")
        @Expose
        String author;

        @SerializedName("content")
        @Expose
        String content;

        @SerializedName("id")
        @Expose
        String id;

        @SerializedName("url")
        @Expose
        String url;

        protected ReviewResults(Parcel in) {
            author = in.readString();
            content = in.readString();
            id = in.readString();
            url = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(author);
            dest.writeString(content);
            dest.writeString(id);
            dest.writeString(url);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<ReviewResults> CREATOR = new Creator<ReviewResults>() {
            @Override
            public ReviewResults createFromParcel(Parcel in) {
                return new ReviewResults(in);
            }

            @Override
            public ReviewResults[] newArray(int size) {
                return new ReviewResults[size];
            }
        };

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

    public class ReleaseDatesWrapper {

        @SerializedName("results")
        @Expose
        private List<ReleaseDatesResults> results;


        public List<ReleaseDatesResults> getResults () {
            return results;
        }

        public void setResults (List<ReleaseDatesResults> results) {
            this.results = results;
        }
    }

    public class ReleaseDatesResults {

        @SerializedName("release_dates")
        @Expose
        private List<ReleaseDatesResultsContent> releaseDateContents;

        @SerializedName("iso_3166_1")
        @Expose
        private String iso_3166_1;

        public List<ReleaseDatesResultsContent> getReleaseDateContents () {
            return releaseDateContents;
        }

        public void setReleaseDateContents (List<ReleaseDatesResultsContent> release_dates) {
            this.releaseDateContents = release_dates;
        }

        public String getIso_3166_1 () {
            return iso_3166_1;
        }

        public void setIso_3166_1 (String iso_3166_1) {
            this.iso_3166_1 = iso_3166_1;
        }
    }

    public class ReleaseDatesResultsContent {
        @SerializedName("iso_639_1")
        @Expose
        private String iso_639_1;

        @SerializedName("certification")
        @Expose
        private String certification;

        @SerializedName("release_date")
        @Expose
        private String release_date;

        @SerializedName("type")
        @Expose
        private String type;

        @SerializedName("note")
        @Expose
        private String note;


        public String getIso_639_1 () {
            return iso_639_1;
        }

        public void setIso_639_1 (String iso_639_1) {
            this.iso_639_1 = iso_639_1;
        }

        public String getCertification () {
            return certification;
        }

        public void setCertification (String certification) {
            this.certification = certification;
        }

        public String getRelease_date () {
            return release_date;
        }

        public void setRelease_date (String release_date) {
            this.release_date = release_date;
        }

        public String getType () {
            return type;
        }

        public void setType (String type) {
            this.type = type;
        }

        public String getNote () {
            return note;
        }

        public void setNote (String note) {
            this.note = note;
        }
    }

    protected MovieAllDetailsResult(Parcel in) {
        backdrop_path = in.readString();
        budget = in.readByte() == 0x00 ? null : in.readInt();

        homepage = in.readString();
        movieId = in.readByte() == 0x00 ? null : in.readInt();
        imdb_id = in.readString();
        originalLanguage = in.readString();
        originalTitle = in.readString();
        overview = in.readString();
        popularity = in.readByte() == 0x00 ? null : in.readDouble();
        posterPath = in.readString();
        releaseDate = in.readString();
        revenue = in.readByte() == 0x00 ? null : in.readInt();
        runtime = in.readByte() == 0x00 ? null : in.readInt();
        tagline = in.readString();
        voteAverage = in.readByte() == 0x00 ? null : in.readDouble();
        voteCount = in.readByte() == 0x00 ? null : in.readInt();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(backdrop_path);
        if (budget == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(budget);
        }

        dest.writeString(homepage);
        if (movieId == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(movieId);
        }
        dest.writeString(imdb_id);
        dest.writeString(originalLanguage);
        dest.writeString(originalTitle);
        dest.writeString(overview);
        if (popularity == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(popularity);
        }
        dest.writeString(posterPath);
        dest.writeString(releaseDate);
        if (revenue == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(revenue);
        }
        if (runtime == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(runtime);
        }
        dest.writeString(tagline);
        if (voteAverage == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(voteAverage);
        }
        if (voteCount == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(voteCount);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MovieAllDetailsResult> CREATOR = new Parcelable.Creator<MovieAllDetailsResult>() {
        @Override
        public MovieAllDetailsResult createFromParcel(Parcel in) {
            return new MovieAllDetailsResult(in);
        }

        @Override
        public MovieAllDetailsResult[] newArray(int size) {
            return new MovieAllDetailsResult[size];
        }
    };
}
