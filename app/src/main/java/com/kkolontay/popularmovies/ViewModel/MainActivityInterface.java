package com.kkolontay.popularmovies.ViewModel;

import com.kkolontay.popularmovies.ConnectionState;
import com.kkolontay.popularmovies.DataModel.PopularMovie;

import java.util.ArrayList;

public interface MainActivityInterface {
    void fetchPopularMoview(ArrayList<PopularMovie> movies, ConnectionState state);
    void errorConnection(final String error, ConnectionState state);
}
