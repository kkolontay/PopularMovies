package com.kkolontay.popularmovies.DataManager.Room;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.kkolontay.popularmovies.DataModel.PopularMovie;

import java.sql.Array;


@Entity(tableName = "movieTable")
public class SelectedPopularMovie {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private long id;
    private int voteCount;
    private int idMovie;
    private boolean video;
    private double voteAverage;
    private String title;
    private double popularity;
    private String posterPath;
    private String originalTitle;
    private String backdropPath;
    private String overview;
    private String releaseDate;

    public SelectedPopularMovie(long id,
                                int voteCount,
                                int idMovie,
                                boolean video,
                                double voteAverage,
                                String title,
                                double popularity,
                                String posterPath,
                                String originalTitle,
                                String backdropPath,
                                String overview,
                                String releaseDate) {
        this.id = id;
        this.voteCount = voteCount;
        this.idMovie = idMovie;
        this.video = video;
        this.voteAverage = voteAverage;
        this.title = title;
        this.popularity = popularity;
        this.posterPath = posterPath;
        this.originalTitle = originalTitle;
        this.backdropPath = backdropPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
    }
    @Ignore
    public SelectedPopularMovie(
                                int voteCount,
                                int idMovie,
                                boolean video,
                                double voteAverage,
                                String title,
                                double popularity,
                                String posterPath,
                                String originalTitle,
                                String backdropPath,
                                String overview,
                                String releaseDate) {

        this.voteCount = voteCount;
        this.idMovie = idMovie;
        this.video = video;
        this.voteAverage = voteAverage;
        this.title = title;
        this.popularity = popularity;
        this.posterPath = posterPath;
        this.originalTitle = originalTitle;
        this.backdropPath = backdropPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public int getIdMovie() {
        return idMovie;
    }

    public void setIdMovie(int idMovie) {
        this.idMovie = idMovie;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
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

    public PopularMovie fetchPopularMovieEntity() {
        PopularMovie popularMovie = new PopularMovie();
        popularMovie.set_id(idMovie);
        popularMovie.set_vote_average(voteAverage);
        popularMovie.set_video(video);
        popularMovie.set_vote_average(voteAverage);
        popularMovie.set_title(title);
        popularMovie.set_popularity(popularity);
        popularMovie.set_poster_path(posterPath);
        popularMovie.set_original_title(originalTitle);
        popularMovie.set_backdrop_path(backdropPath);
        popularMovie.set_overview(overview);
        popularMovie.set_release_date(releaseDate);
        int[] newgenre = new int[]{1,2};
        popularMovie.set_genre_ids(newgenre);
        return  popularMovie;
    }
}
