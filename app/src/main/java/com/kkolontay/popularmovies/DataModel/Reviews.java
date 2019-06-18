package com.kkolontay.popularmovies.DataModel;

import java.util.List;

public class Reviews {
     private int mId;
     private int mPage;
     private List<MovieReview> mResults;
     private int mTotalPages;
     private int mTotalResults;

    public Reviews(int mId, int mPage, List<MovieReview> mResults, int mTotalPages, int mTotalResults) {
        this.mId = mId;
        this.mPage = mPage;
        this.mResults = mResults;
        this.mTotalPages = mTotalPages;
        this.mTotalResults = mTotalResults;
    }

    public int getmId() {
        return mId;
    }

    public int getmPage() {
        return mPage;
    }

    public List<MovieReview> getmResults() {
        return mResults;
    }

    public int getmTotalPages() {
        return mTotalPages;
    }

    public int getmTotalResults() {
        return mTotalResults;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public void setmPage(int mPage) {
        this.mPage = mPage;
    }

    public void setmResults(List<MovieReview> mResults) {
        this.mResults = mResults;
    }

    public void setmTotalPages(int mTotalPages) {
        this.mTotalPages = mTotalPages;
    }

    public void setmTotalResults(int mTotalResults) {
        this.mTotalResults = mTotalResults;
    }
}
