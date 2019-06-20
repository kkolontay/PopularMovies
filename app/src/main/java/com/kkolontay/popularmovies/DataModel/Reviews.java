package com.kkolontay.popularmovies.DataModel;

import java.util.ArrayList;


public class Reviews {
    public static final String ID = "id";
    public static final String PAGE = "page";
    public static final String RESULTS = "results";
    public static final String TOTALPAGES = "total_pages";
    public static final String TOTALRESULTS = "total_results";
     private int id;
     private int page;
     private ArrayList<MovieReview> results;
     private int totalPages;
     private int totalResults;

    public Reviews() {
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public ArrayList<MovieReview> getResults() {
        return results;
    }

    public void setResults(ArrayList<MovieReview> results) {
        this.results = results;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public Reviews(int id, int page, ArrayList<MovieReview> results, int totalPages, int totalResults) {
        this.id = id;
        this.page = page;
        this.results = results;
        this.totalPages = totalPages;
        this.totalResults = totalResults;
    }

}
