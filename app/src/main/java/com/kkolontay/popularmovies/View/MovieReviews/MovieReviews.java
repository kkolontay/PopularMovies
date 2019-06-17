package com.kkolontay.popularmovies.View.MovieReviews;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kkolontay.popularmovies.R;
import com.kkolontay.popularmovies.View.DetailMovie.DetailMovieActivity;

public class MovieReviews extends AppCompatActivity {
    private int movieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_reviews);
        movieId = getIntent().getIntExtra(DetailMovieActivity.MOVIEID, 0);
    }
}
