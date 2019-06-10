package com.kkolontay.popularmovies.View.DetailMovie;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.kkolontay.popularmovies.ConnectionState;
import com.kkolontay.popularmovies.DataModel.PopularMovie;
import com.kkolontay.popularmovies.R;
import com.kkolontay.popularmovies.Sessions.NetworkUtility;
import com.kkolontay.popularmovies.Sessions.RequestMovieError;
import com.kkolontay.popularmovies.Sessions.SizeImage;
import com.kkolontay.popularmovies.Sessions.TypeRequest;
import com.kkolontay.popularmovies.Utility.ObjectsDataJSONParser;
import com.kkolontay.popularmovies.View.MainActivity;
import com.squareup.picasso.Picasso;

import java.net.URL;


public class DetailMovieActivity extends AppCompatActivity {

    private PopularMovie movie;
    private static final String TAG = DetailMovieActivity.class.getSimpleName();
    private static final String SAVEDINSTANCE = "POPULAR_MOVIE";
    private static final String YOUTUBELINK = "https://www.youtube.com/watch?v=";
    private TextView dateReleaseTextView;
    private TextView plotSynopsisTextView;
    private TextView voteAverageTextView;
    private ImageView moviePosterImageView;
    private ConnectionState state = ConnectionState.SUCCESS;
    private String uriVideoTeaser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);
        dateReleaseTextView = findViewById(R.id.date_release);
        plotSynopsisTextView = findViewById(R.id.plot_movie);
        voteAverageTextView = findViewById(R.id.vote_average);
        moviePosterImageView = findViewById(R.id.movie_poster);
        if (savedInstanceState == null) {
                movie = getIntent().getExtras().getParcelable(MainActivity.PUTEXTRAMOVIEDETAIL);
        } else {
            movie = savedInstanceState.getParcelable(SAVEDINSTANCE);
        }
        if (movie != null) {
            setData(movie);
        }
        URL url = NetworkUtility.buildURL(0, TypeRequest.VIDEO, movie.get_id());
        if (url != null) {
            new FetchYoutubeLinkShortVideo().execute(url);
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

    private class FetchYoutubeLinkShortVideo extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            if (url != null) {
                try {
                    String result = NetworkUtility.getResponseFromHttpUrl(url);
                    state = ConnectionState.SUCCESS;
                    return  result;

                } catch(RequestMovieError e) {
                    String error = ObjectsDataJSONParser.getErrorDescription(e.getMessage());
                    state = ConnectionState.ERROR;
                    Log.i(TAG, error);


                    return error;
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (state == ConnectionState.SUCCESS) {
                String keyTeaser  = ObjectsDataJSONParser.getIdVideoTeaser(s);
                if (keyTeaser != null) {
                    uriVideoTeaser = YOUTUBELINK + keyTeaser;
                }

            }
        }
    }
}
