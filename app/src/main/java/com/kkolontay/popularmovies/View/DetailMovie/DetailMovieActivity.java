package com.kkolontay.popularmovies.View.DetailMovie;

import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.youtube.player.YouTubePlayer;
import com.kkolontay.popularmovies.DataManager.Room.AppDatabase;
import com.kkolontay.popularmovies.DataManager.Room.SelectedPopularMovie;
import com.kkolontay.popularmovies.DataModel.PopularMovie;
import com.kkolontay.popularmovies.R;
import com.kkolontay.popularmovies.Sessions.NetworkUtility;
import com.kkolontay.popularmovies.Sessions.SizeImage;
import com.kkolontay.popularmovies.Utility.AppExecutors;
import com.kkolontay.popularmovies.Utility.VideoTeaserItem;
import com.kkolontay.popularmovies.View.MainActivity;
import com.kkolontay.popularmovies.View.MovieReviews.MovieReviews;
import com.kkolontay.popularmovies.ViewModel.TeaserViewModelFactory;
import com.kkolontay.popularmovies.ViewModel.TeasersViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class DetailMovieActivity extends AppCompatActivity {

    private PopularMovie movie;
    private static final String TAG = DetailMovieActivity.class.getSimpleName();
    public static final String MOVIEID = "movie_id";
    private TextView dateReleaseTextView;
    private TextView plotSynopsisTextView;
    private TextView voteAverageTextView;
    private ImageView moviePosterImageView;
    private String keyTeaserMovie;
    private Button movieReviewsButton;
    private Button selectedMovieButton;
    private AppDatabase appDatabase;
    private boolean isMovieSelected;
    private Button playPrevTeaserButton;
    private Button playNextTeaserButton;
    private ArrayList<VideoTeaserItem> teaserItems;
    private TeasersViewModel viewModel;
    private int teaserSelectedId;
    private VideoFragment videoFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);
        dateReleaseTextView = findViewById(R.id.date_release);
        plotSynopsisTextView = findViewById(R.id.plot_movie);
        voteAverageTextView = findViewById(R.id.vote_average);
        moviePosterImageView = findViewById(R.id.movie_poster);
        movieReviewsButton = findViewById(R.id.movie_views_bt);
        selectedMovieButton = findViewById(R.id.selected_movie_button);
        playNextTeaserButton = findViewById(R.id.play_next_button);
        playPrevTeaserButton = findViewById(R.id.play_prev_button);
        appDatabase = AppDatabase.getInstance(getApplicationContext());
        if (savedInstanceState == null) {
            movie = getIntent().getExtras().getParcelable(MainActivity.PUTEXTRAMOVIEDETAIL);
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
                            if (appDatabase.selectedPopularMovie().fetchMovie(movie.get_id()) == null) {
                                SelectedPopularMovie selectedPopularMovie = fetchEntity();
                                appDatabase.selectedPopularMovie().insertMovie(selectedPopularMovie);

                            }

                        } else {
                            SelectedPopularMovie selectedPopularMovie = appDatabase.selectedPopularMovie().fetchMovie(movie.get_id());
                            if (selectedPopularMovie != null) {
                                appDatabase.selectedPopularMovie().deleteMovie(selectedPopularMovie);
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
        initViewModel();

    }

    private void initViewModel() {
        if (isOnline()) {
            viewModel = ViewModelProviders.of(this, new TeaserViewModelFactory(getApplication(), movie.get_id(), getApplicationContext())).get(TeasersViewModel.class);
            final Observer<String> errorMessage = new Observer<String>() {
                @Override
                public void onChanged(@Nullable String s) {
                    showAlertMassage(s);
                }
            };
            viewModel.getErrorMessage().observe(this, errorMessage);

            final Observer<ArrayList<VideoTeaserItem>> moviesObserver = new Observer<ArrayList<VideoTeaserItem>>() {
                @Override
                public void onChanged(@Nullable ArrayList<VideoTeaserItem> teasers) {
                    if (teasers.size() > 0) {
                        teaserItems = teasers;
                        teaserSelectedId = 0;
                        playTeaserButtonSelected();
                    } else {
                        showWarning();
                    }
                }
            };
            viewModel.getVideoTeasers().observe(this, moviesObserver);

            final Observer<Boolean> isSelected = new Observer<Boolean>() {
                @Override
                public void onChanged(@Nullable Boolean selected) {
                    isMovieSelected  = selected;
                    setImageToSelectMovieButton();
                }
            };
            viewModel.getIsSelected().observe(this, isSelected);
        }
    }

    private void showWarning() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.alert_dialog);
        dialog.setTitle(getString(R.string.warning));

        TextView text = dialog.findViewById(R.id.error_message_tv);
        text.setText(getString(R.string.teasers_film));

        Button dialogButton = dialog.findViewById(R.id.alert_dialog_message_bt);
        dialogButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });

        dialog.show();
    }

    private void showAlertMassage(String errorMessage) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.alert_dialog);
        dialog.setTitle("Error Message");

        TextView text = dialog.findViewById(R.id.error_message_tv);
        text.setText(errorMessage);

        Button dialogButton = dialog.findViewById(R.id.alert_dialog_message_bt);
        dialogButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }


    private SelectedPopularMovie fetchEntity() {

        if (movie != null) {
            return new SelectedPopularMovie(movie.get_vote_count(),
                    movie.get_id(),
                    true,
                    movie.get_vote_average(),
                    movie.get_title(),
                    movie.get_popularity(),
                    movie.get_poster_path(),
                    movie.get_original_title(),
                    movie.get_backdrop_path(),
                    movie.get_overview(),
                    movie.get_release_date());
        } else {
            return null;
        }
    }

    private void setImageToSelectMovieButton() {
        if (isMovieSelected) {
            selectedMovieButton.setBackgroundResource(R.drawable.fillstarnewcolor);
        } else {
            selectedMovieButton.setBackgroundResource(R.drawable.emptystarnewcolor);
        }
    }

    private void playTeaserButtonSelected() {
        makePlayButtonsVisible();
        keyTeaserMovie = teaserItems.get(teaserSelectedId).getKey();
        registrationYoutubePlayer();
    }

    private void makePlayButtonsVisible() {
        if (teaserItems.size() > 1) {
            playNextTeaserButton.setVisibility(View.VISIBLE);
            playPrevTeaserButton.setVisibility(View.VISIBLE);
            if (teaserSelectedId == 0) {
                playPrevTeaserButton.setEnabled(false);
            } else if (teaserSelectedId == teaserItems.size() - 1) {
                playNextTeaserButton.setEnabled(false);
            } else {
                playPrevTeaserButton.setEnabled(true);
                playNextTeaserButton.setEnabled(true);
            }

        } else {
            playNextTeaserButton.setVisibility(View.GONE);
            playPrevTeaserButton.setVisibility(View.GONE);
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
        playNextTeaserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (teaserSelectedId + 1 < teaserItems.size()) {
                    teaserSelectedId += 1;
                    playTeaserButtonSelected();
                }
            }
        });
        playPrevTeaserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (teaserSelectedId - 1 >= 0) {
                    teaserSelectedId -= 1;
                    playTeaserButtonSelected();
                }
            }
        });
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        YouTubePlayer player = videoFragment.getPlayer();
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            player.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI);
            player.setFullscreen(true);

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            player.setFullscreen(false);
        }
    }

    private void registrationYoutubePlayer() {
        if (keyTeaserMovie != null) {
            Log.v(TAG, keyTeaserMovie);
            videoFragment = VideoFragment.newInstance(keyTeaserMovie);
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.youtube_fragment, videoFragment).commit();
            final YouTubePlayer player = videoFragment.getPlayer();
            if (player != null) {


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
            }

        }

    }
}
