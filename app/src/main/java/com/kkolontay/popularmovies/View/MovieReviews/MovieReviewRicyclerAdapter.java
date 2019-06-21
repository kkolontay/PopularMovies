package com.kkolontay.popularmovies.View.MovieReviews;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kkolontay.popularmovies.DataModel.MovieReview;
import com.kkolontay.popularmovies.R;

import java.util.ArrayList;

public class MovieReviewRicyclerAdapter extends  RecyclerView.Adapter<MovieReviewRicyclerAdapter.MovieReviewViewHolder> {
    private static final String TAG = MovieReviewRicyclerAdapter.class.getSimpleName();
    private ArrayList<MovieReview> reviews;

    public MovieReviewRicyclerAdapter(ArrayList<MovieReview> reviews) {
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public MovieReviewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.reviewmovie_item, viewGroup, false);
        MovieReviewViewHolder viewHolder = new MovieReviewViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieReviewViewHolder movieReviewViewHolder, int i) {
        movieReviewViewHolder.bindData(reviews.get(i));
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public class MovieReviewViewHolder extends RecyclerView.ViewHolder {
        private TextView author;
        private TextView content;

        public MovieReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            author = itemView.findViewById(R.id.author_text_view);
            content = itemView.findViewById(R.id.content_text_view);
        }

        public void bindData(MovieReview item) {
            author.setText(item.getAuthor());
            content.setText(item.getContent());
        }

    }
}
