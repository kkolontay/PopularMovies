package com.kkolontay.popularmovies.DataManager;

import com.kkolontay.popularmovies.DataModel.PopularMovie;
import com.kkolontay.popularmovies.DataModel.ResponseDataObject;

import java.util.ArrayList;

public class DataManager {
    private static volatile DataManager _manager;
    private ResponseDataObject _response;

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