package com.kkolontay.popularmovies.View.DetailMovie;

import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
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


public class DetailMovieActivity extends YouTubeBaseActivity {

    private PopularMovie movie;
    private static final String TAG = DetailMovieActivity.class.getSimpleName();
    private static final String SAVEDINSTANCE = "POPULAR_MOVIE";
    private static final String YOUTUBELINK = "https://www.youtube.com/watch?v=";
    private static final String MOVIEIDKEY = "moviewIdKey";
    private TextView dateReleaseTextView;
    private TextView plotSynopsisTextView;
    private TextView voteAverageTextView;
    private ImageView moviePosterImageView;
    private ConnectionState state = ConnectionState.SUCCESS;
    private YouTubePlayerView youTubePlayerView;
    private String keyTeaserMovie;
    private YouTubePlayer player;
   // private boolean mAutoRotation = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);
        dateReleaseTextView = findViewById(R.id.date_release);
        plotSynopsisTextView = findViewById(R.id.plot_movie);
        voteAverageTextView = findViewById(R.id.vote_average);
        moviePosterImageView = findViewById(R.id.movie_poster);
        youTubePlayerView = findViewById(R.id.youtube_player);
//        mAutoRotation = Settings.System.getInt(getContentResolver(),
//                Settings.System.ACCELEROMETER_ROTATION, 0) == 1;
        if (savedInstanceState == null) {
                movie = getIntent().getExtras().getParcelable(MainActivity.PUTEXTRAMOVIEDETAIL);
            URL url = NetworkUtility.buildURL(0, TypeRequest.VIDEO, movie.get_id());
            if (url != null) {
                new FetchYoutubeLinkShortVideo().execute(url);
            }
        } else {
            movie = savedInstanceState.getParcelable(SAVEDINSTANCE);
            keyTeaserMovie = savedInstanceState.getString(MOVIEIDKEY);
           // registrationYoutubePlayer();

        }
        if (movie != null) {
            setData(movie);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(SAVEDINSTANCE, movie);
        outState.putString(MOVIEIDKEY, keyTeaserMovie);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player = null;
        youTubePlayerView = null;
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
                    return result;

                } catch (RequestMovieError e) {
                    String error = ObjectsDataJSONParser.getErrorDescription(e.getMessage());
                    state = ConnectionState.ERROR;
                    Log.i(TAG, error);


                    return error;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (state == ConnectionState.SUCCESS) {
                final String keyTeaser = ObjectsDataJSONParser.getIdVideoTeaser(s);
                keyTeaserMovie = keyTeaser;
                registrationYoutubePlayer();

            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            player.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI);
            player.setFullscreen(true);

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            player.setFullscreen(false);
        }
    }

    private void registrationYoutubePlayer() {
            if (keyTeaserMovie != null) {
                youTubePlayerView.initialize(PlayerConfig.YOUTUBE_KEY, new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                        if(!b) {
                            player = youTubePlayer;
                            player.setPlaybackEventListener(new YouTubePlayer.PlaybackEventListener() {
                                @Override
                                public void onPlaying() {
                                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                                        player.setFullscreen(true);
                                    }
                                }

                                @Override
                                public void onPaused() {
                                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                                        player.setFullscreen(false);
                                    }
                                }

                                @Override
                                public void onStopped() {

                                }

                                @Override
                                public void onBuffering(boolean b) {

                                }

                                @Override
                                public void onSeekTo(int i) {

                                }
                            });
                            youTubePlayer.cueVideo(keyTeaserMovie);
                            youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                            youTubePlayer.play();
                        }
                    }



                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                    }
                });
            } else {
                youTubePlayerView.setVisibility(View.GONE);
            }
    }
}
