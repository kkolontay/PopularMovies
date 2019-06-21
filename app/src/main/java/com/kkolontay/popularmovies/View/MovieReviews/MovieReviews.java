package com.kkolontay.popularmovies.View.MovieReviews;

import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.ConnectivityManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kkolontay.popularmovies.DataModel.MovieReview;
import com.kkolontay.popularmovies.R;
import com.kkolontay.popularmovies.View.DetailMovie.DetailMovieActivity;
import com.kkolontay.popularmovies.ViewModel.MovieReviewsViewModel;

import java.util.ArrayList;

public class MovieReviews extends AppCompatActivity {
    private static final String TAG = MovieReview.class.getSimpleName();
    private int movieId;
    private static final String PAGE_NUMBER = "pageNumberForRequest";
    private ArrayList<MovieReview> reviews;
    private int nextPage;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private MovieReviewsViewModel viewModel;
    private  MovieReviewRicyclerAdapter adapter;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_reviews);
        progressBar = findViewById(R.id.progressBarReview);
        recyclerView = (RecyclerView) findViewById(R.id.reviewMovies_RecyclerView);
        reviews = new ArrayList();
        initRecyclerView();
        if(savedInstanceState == null) {
            nextPage = 1;
        } else {
            nextPage = savedInstanceState.getInt(PAGE_NUMBER);
        }

        movieId = getIntent().getIntExtra(DetailMovieActivity.MOVIEID, 0);
        viewModel = ViewModelProviders.of(this).get(MovieReviewsViewModel.class);

        final Observer<ArrayList<MovieReview>> reviewsObserver = new Observer<ArrayList<MovieReview>>() {
            @Override
            public void onChanged(@Nullable ArrayList<MovieReview> movieReviews) {
                progressBar.setVisibility(ProgressBar.INVISIBLE);
                if (reviews == null) {
                    reviews = movieReviews;
                } else {
                    reviews.addAll(movieReviews);
                }
                adapter.notifyDataSetChanged();
                if (movieReviews == null || movieReviews.size() == 0) {
                    showWarning();
                }
            }
        };
        viewModel.getReviews().observe(this, reviewsObserver);

        final Observer<Integer> nextPageObserver = new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                nextPage = integer;
                progressBar.setVisibility(ProgressBar.INVISIBLE);
            }
        };
        viewModel.getNextPage().observe(this, nextPageObserver);

        final Observer<String> errorMessage = new Observer<String>() {
            @Override
            public void onChanged(@Nullable String message) {
                final String messageError = message;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showAlertMassage(messageError);
                        progressBar.setVisibility(ProgressBar.INVISIBLE);
                    }
                });
            }
        };
        viewModel.getErrorMessage().observe(this, errorMessage);
        provideNextData();
    }

    private void initRecyclerView() {
        adapter = new MovieReviewRicyclerAdapter(reviews);
        if (adapter != null) {
            recyclerView.setAdapter(adapter);
            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }


                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy)
                {

                    if (nextPage > 0) {
                        if (dy > 0) //check for scroll down
                        {
                           int visibleItemCount = layoutManager.getChildCount();
                           int  totalItemCount = layoutManager.getItemCount();
                           int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

                            if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                                provideNextData();
                            }

                        }
                    }
                }
            });
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(PAGE_NUMBER, nextPage);
        super.onSaveInstanceState(outState);
    }

    private void provideNextData() {
        if (isOnline()) {
            if (nextPage >= 0) {
                viewModel.fetchNextPage(nextPage, movieId);
                progressBar.setVisibility(ProgressBar.VISIBLE);
            }
        }
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    private void showWarning() {
        progressBar.setVisibility(ProgressBar.INVISIBLE);
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.alert_dialog);
        dialog.setTitle(getString(R.string.warning));

        TextView text =  dialog.findViewById(R.id.error_message_tv);
        text.setText(getString(R.string.reviewed_film));

        Button dialogButton =  dialog.findViewById(R.id.alert_dialog_message_bt);
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
        progressBar.setVisibility(ProgressBar.INVISIBLE);
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.alert_dialog);
        dialog.setTitle("Error Message");

        TextView text =  dialog.findViewById(R.id.error_message_tv);
        text.setText(errorMessage);

        Button dialogButton =  dialog.findViewById(R.id.alert_dialog_message_bt);
        dialogButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
