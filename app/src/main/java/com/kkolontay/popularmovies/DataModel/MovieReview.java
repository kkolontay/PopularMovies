package com.kkolontay.popularmovies.DataModel;


public class MovieReview {
     private String mAuthor;
     private String mContent;
     private  String mId;
     private String mURL;

    public MovieReview(String mAuthor, String mContent, String mId, String mURL) {
        this.mAuthor = mAuthor;
        this.mContent = mContent;
        this.mId = mId;
        this.mURL = mURL;
    }

    public String getmAuthor() {
        return mAuthor;
    }

    public void setmAuthor(String mAuthor) {
        this.mAuthor = mAuthor;
    }

    public String getmContent() {
        return mContent;
    }

    public void setmContent(String mContent) {
        this.mContent = mContent;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmURL() {
        return mURL;
    }

    public void setmURL(String mURL) {
        this.mURL = mURL;
    }
}

