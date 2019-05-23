package com.kkolontay.popularmovies.View.DetailMovie;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kkolontay.popularmovies.DataModel.PopularMovie;
import com.kkolontay.popularmovies.R;
import com.kkolontay.popularmovies.View.MainActivity;

public class DetailMovieActivity extends AppCompatActivity {

    private PopularMovie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);
        PopularMovie movieDetail = getIntent().getExtras().getParcelable(MainActivity.PUTEXTRAMOVIEDETAIL);
        if (movieDetail != null) {
            movie = movieDetail;
        }
    }
}
