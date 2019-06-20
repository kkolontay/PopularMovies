package com.kkolontay.popularmovies.View.MovieReviews;

import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kkolontay.popularmovies.DataModel.MovieReview;
import com.kkolontay.popularmovies.DataModel.Reviews;
import com.kkolontay.popularmovies.R;
import com.kkolontay.popularmovies.View.DetailMovie.DetailMovieActivity;
import com.kkolontay.popularmovies.ViewModel.MovieReviewsViewModel;

import java.util.ArrayList;

public class MovieReviews extends AppCompatActivity {
    private int movieId;
    private ArrayList<MovieReview> reviews;
    private int nextPage;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_reviews);
        movieId = getIntent().getIntExtra(DetailMovieActivity.MOVIEID, 0);
        MovieReviewsViewModel viewModel = ViewModelProviders.of(this).get(MovieReviewsViewModel.class);

        final Observer<ArrayList<MovieReview>> reviewsObserver = new Observer<ArrayList<MovieReview>>() {
            @Override
            public void onChanged(@Nullable ArrayList<MovieReview> movieReviews) {
                if (reviews == null) {
                    reviews = movieReviews;
                } else {
                    reviews.addAll(movieReviews);
                }
            }
        };
        viewModel.getReviews().observe(this, reviewsObserver);

        final Observer<Integer> nextPageObserver = new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                nextPage = integer;
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
                    }
                });
            }
        };
        viewModel.getErrorMessage().observe(this, errorMessage);

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
