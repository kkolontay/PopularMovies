package com.kkolontay.popularmovies.DataModel;

import java.util.ArrayList;

public class ResponseDataObject {
    public final static String PAGE = "page";
    public final static String TOTALRESULTS = "total_results";
    public final static String TOTALPAGES = "total_pages";
    public final static String RESULTS = "results";
    private int _page;
    private int _total_resuts;
    private int _total_pages;
    private ArrayList<PopularMovie> results;

    public int get_page() {
        return _page;
    }

    public void set_page(int page) {
        _page = page;
    }

    public int get_total_resuts() {
        return _total_resuts;
    }

    public void set_total_resuts(int total_resuts) {
        _total_resuts = _total_resuts;
    }

    public int get_total_pages() {
        return _total_pages;
    }

    public void set_total_pages(int total_pages) {
        _total_pages = total_pages;
    }

    public ArrayList<PopularMovie> getResults() {
        return results;
    }

    public void setResults(ArrayList<PopularMovie> results) {
        this.results.addAll(results);
    }
}
