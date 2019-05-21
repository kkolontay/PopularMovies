package com.kkolontay.popularmovies.DataModel;

import android.os.Parcel;
import android.os.Parcelable;

public class PopularMovie implements Parcelable {
    public final static String VOTECOUNT = "vote_count";
    public final static String ID = "id";
    public final static String VIDEO = "video";
    public final static String VOTE_AVERAGE = "vote_average";
    public final static String TITLE = "title";
    public final static String POPULARITY = "popularity";
    public final static String POSTED_PATH = "poster_path";
    public final static String ORIGINAL_TITLE = "original_title";
    public final static String   GENRE_ID = "genre_ids";
    public final static String BACKGROUND_PATH = "backdrop_path";
    public final static String OVERVIEW = "overview";
    public final static String RELEASE_DATE = "release_date";

    private int _vote_count;
    private int _id;
    private boolean _video;
    private double _vote_average;
    private String _title;
    private double _popularity;
    private String _poster_path;
    private String _original_title;
    private int[] _genre_ids;
    private String _backdrop_path;
    private String _overview;
    private String _release_date;

    public int get_vote_count() {
        return _vote_count;
    }

    public void set_vote_count(int _vote_count) {
        this._vote_count = _vote_count;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public boolean is_video() {
        return _video;
    }

    public void set_video(boolean _video) {
        this._video = _video;
    }

    public double get_vote_average() {
        return _vote_average;
    }

    public void set_vote_average(double _vote_average) {
        this._vote_average = _vote_average;
    }

    public String get_title() {
        return _title;
    }

    public void set_title(String _title) {
        this._title = _title;
    }

    public double get_popularity() {
        return _popularity;
    }

    public void set_popularity(double _popularity) {
        this._popularity = _popularity;
    }

    public String get_poster_path() {
        return _poster_path;
    }

    public void set_poster_path(String _poster_path) {
        this._poster_path = _poster_path;
    }

    public String get_original_title() {
        return _original_title;
    }

    public void set_original_title(String _original_title) {
        this._original_title = _original_title;
    }

    public int[] get_genre_ids() {
        return _genre_ids;
    }

    public void set_genre_ids(int[] _genre_ids) {
        this._genre_ids = _genre_ids;
    }

    public String get_backdrop_path() {
        return _backdrop_path;
    }

    public void set_backdrop_path(String _backdrop_path) {
        this._backdrop_path = _backdrop_path;
    }

    public String get_overview() {
        return _overview;
    }

    public void set_overview(String _overview) {
        this._overview = _overview;
    }

    public String get_release_date() {
        return _release_date;
    }

    public void set_release_date(String _release_date) {
        this._release_date = _release_date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_vote_count);
        dest.writeInt(_id);
        dest.writeInt(_video? 1: 0);
        dest.writeDouble(_vote_average);
        dest.writeString(_title);
        dest.writeDouble(_popularity);
        dest.writeString(_poster_path);
        dest.writeString(_original_title);
        dest.writeInt(_genre_ids.length);
        dest.writeIntArray(_genre_ids);
        dest.writeString(_backdrop_path);
        dest.writeString(_overview);
        dest.writeString(_release_date);
    }
    public PopularMovie() {}
    public static final Parcelable.Creator<PopularMovie> CREATOR
            = new Parcelable.Creator<PopularMovie>() {
        public PopularMovie createFromParcel(Parcel in) {
            return new PopularMovie(in);
        }

        public PopularMovie[] newArray(int size) {
            return new PopularMovie[size];
        }
    };

    private PopularMovie(Parcel in) {
        _vote_count = in.readInt();
         _id = in.readInt();
        _video = (in.readInt() == 1? true: false);
        _vote_average = in.readDouble();
        _title = in.readString();
        _popularity = in.readDouble();
        _poster_path = in.readString();
        _original_title = in.readString();
        int length = in.readInt();
        _genre_ids = new int[length];
         in.readIntArray(_genre_ids);
        _backdrop_path = in.readString();
        _overview = in.readString();
        _release_date = in.readString();
    }
}