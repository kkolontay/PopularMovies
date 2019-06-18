package com.kkolontay.popularmovies.View.DetailMovie;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.kkolontay.popularmovies.ConnectionState;
import com.kkolontay.popularmovies.DataManager.Room.AppDatabase;
import com.kkolontay.popularmovies.DataManager.Room.SelectedPopularMovie;
import com.kkolontay.popularmovies.DataModel.PopularMovie;
import com.kkolontay.popularmovies.R;
import com.kkolontay.popularmovies.Sessions.NetworkUtility;
import com.kkolontay.popularmovies.Sessions.RequestMovieError;
import com.kkolontay.popularmovies.Sessions.SizeImage;
import com.kkolontay.popularmovies.Sessions.TypeRequest;
import com.kkolontay.popularmovies.Utility.AppExecutors;
import com.kkolontay.popularmovies.Utility.ObjectsDataJSONParser;
import com.kkolontay.popularmovies.View.MainActivity;
import com.kkolontay.popularmovies.View.MovieReviews.MovieReviews;
import com.squareup.picasso.Picasso;
import java.net.URL;


public class DetailMovieActivity extends YouTubeBaseActivity {

    private PopularMovie movie;
    private static final String TAG = DetailMovieActivity.class.getSimpleName();
    public static final String MOVIEID = "movie_id";
    private static final String SAVEDINSTANCE = "POPULAR_MOVIE";
    private static final String MOVIEIDKEY = "moviewIdKey";
    private TextView dateReleaseTextView;
    private TextView plotSynopsisTextView;
    private TextView voteAverageTextView;
    private ImageView moviePosterImageView;
    private ConnectionState state = ConnectionState.SUCCESS;
    private YouTubePlayerView youTubePlayerView;
    private String keyTeaserMovie;
    private YouTubePlayer player;
    private Button movieReviewsButton;
    private Button selectedMovieButton;
    private AppDatabase appDatabase;
    private boolean isMovieSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);
        dateReleaseTextView = findViewById(R.id.date_release);
        plotSynopsisTextView = findViewById(R.id.plot_movie);
        voteAverageTextView = findViewById(R.id.vote_average);
        moviePosterImageView = findViewById(R.id.movie_poster);
        youTubePlayerView = findViewById(R.id.youtube_player);
        movieReviewsButton = findViewById(R.id.movie_views_bt);
        selectedMovieButton = findViewById(R.id.selected_movie_button);
        if (savedInstanceState == null) {
                movie = getIntent().getExtras().getParcelable(MainActivity.PUTEXTRAMOVIEDETAIL);
            URL url = NetworkUtility.buildURL(0, TypeRequest.VIDEO, movie.get_id());
            if (url != null) {
                new FetchYoutubeLinkShortVideo().execute(url);
            }
        } else {
            movie = savedInstanceState.getParcelable(SAVEDINSTANCE);
            keyTeaserMovie = savedInstanceState.getString(MOVIEIDKEY);
        }
        if (movie != null) {
            setData(movie);
        }
        movieReviewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reviews = new Intent(DetailMovieActivity.this, MovieReviews.class);
                reviews.putExtra(MOVIEID, movie.get_id());
                startActivity(reviews);
            }
        });
        selectedMovieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        isMovieSelected = !isMovieSelected;
                        if (isMovieSelected) {
                            if ( appDatabase.selectedPopularMovie().fetchMovie(movie.get_id()) == null) {
                                appDatabase.selectedPopularMovie().insertMovie(fetchEntity());
                            }

                        } else {
                            if ( appDatabase.selectedPopularMovie().fetchMovie(movie.get_id()) != null) {
                                appDatabase.selectedPopularMovie().deleteMovie(fetchEntity());
                            }

                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setImageToSelectMovieButton();
                            }
                        });

                    }
                });


            }
        });
        appDatabase = AppDatabase.getInstance(getApplicationContext());

        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                if ( appDatabase.selectedPopularMovie().fetchMovie(movie.get_id()) == null) {
                    isMovieSelected = false;
                } else {
                    isMovieSelected = true;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setImageToSelectMovieButton();
                    }
                });
            }
        });


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

    private SelectedPopularMovie fetchEntity() {
        return new SelectedPopularMovie(movie.get_vote_count(),
                movie.get_id(),
                true,
                movie.get_vote_average(),
                movie.get_title(),
                movie.get_popularity(),
                movie.get_poster_path(),
                movie.get_original_title(),
                movie.get_genre_ids(),
                movie.get_backdrop_path(),
                movie.get_overview(),
                movie.get_release_date());
    }

    private void setImageToSelectMovieButton() {
        if(isMovieSelected) {
            selectedMovieButton.setBackgroundResource(R.drawable.fullstar);
        } else {
            selectedMovieButton.setBackgroundResource(R.drawable.emptystar);
        }
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
