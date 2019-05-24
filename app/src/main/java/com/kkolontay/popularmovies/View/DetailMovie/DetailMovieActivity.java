package com.kkolontay.popularmovies.View.DetailMovie;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.kkolontay.popularmovies.DataModel.PopularMovie;
import com.kkolontay.popularmovies.R;
import com.kkolontay.popularmovies.Sessions.NetworkUtility;
import com.kkolontay.popularmovies.Sessions.SizeImage;
import com.kkolontay.popularmovies.View.MainActivity;
import com.squareup.picasso.Picasso;


public class DetailMovieActivity extends AppCompatActivity {

    private PopularMovie movie;
    private static final String SAVEDINSTANCE = "POPULAR_MOVIE";
    private TextView dateReleaseTextView;
    private TextView plotSynopsisTextView;
    private TextView voteAverageTextView;
    private ImageView moviePosterImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);
        dateReleaseTextView = (TextView) findViewById(R.id.date_release);
        plotSynopsisTextView = (TextView) findViewById(R.id.plot_movie);
        voteAverageTextView = (TextView) findViewById(R.id.vote_average);
        moviePosterImageView = (ImageView) findViewById(R.id.movie_poster);
        if (savedInstanceState == null) {
                movie = getIntent().getExtras().getParcelable(MainActivity.PUTEXTRAMOVIEDETAIL);
        } else {
            movie = savedInstanceState.getParcelable(SAVEDINSTANCE);
        }
        if (movie != null) {
            setData(movie);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(SAVEDINSTANCE, movie);
        super.onSaveInstanceState(outState);
    }

    private void setData(PopularMovie movie) {
        setTitle(movie.get_title());
        dateReleaseTextView.setText(movie.get_release_date());
        plotSynopsisTextView.setText(movie.get_overview());
        voteAverageTextView.setText(String.format("%.1f", movie.get_vote_average()));
        if (movie.get_poster_path() != null) {
            String imagePath = movie.get_poster_path();
            String imageUrl = NetworkUtility.getImageURLString(imagePath, SizeImage.BIG);
            Picasso.get().load(imageUrl).placeholder(R.drawable.no_image).error(R.drawable.no_image).into(moviePosterImageView);
        } else {
            Picasso.get().load("").placeholder(R.drawable.no_image).error(R.drawable.no_image).into(moviePosterImageView);
        }
    }
}
