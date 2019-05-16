package com.kkolontay.popularmovies.Sessions;

public class RequestMovieError extends Exception {

    public RequestMovieError(String errorMessage) {
        super(errorMessage);
    }
}
