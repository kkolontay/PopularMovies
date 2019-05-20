package com.kkolontay.popularmovies.DataManager;

import com.kkolontay.popularmovies.DataModel.PopularMovie;
import com.kkolontay.popularmovies.DataModel.ResponseDataObject;

import java.util.ArrayList;

public class DataManager {
    private static volatile DataManager _manager;
    private ResponseDataObject _response;

    public ResponseDataObject get_response() {
        return _response;
    }

    public void set_response(ResponseDataObject response) {
        this._response.set_page(response.get_page());
        this._response.set_total_pages(response.get_total_pages());
        this._response.set_total_resuts(response.get_total_resuts());
        this._response.setResults(response.getResults());
    }

    private DataManager() {
        _response = new ResponseDataObject();
        _response.setResults(new ArrayList<PopularMovie>());
    }

    public static DataManager getInstance() {
        if (_manager == null) {
            synchronized (DataManager.class) {
                if (_manager == null) _manager = new DataManager();
            }
        }
        return _manager;
    }

}