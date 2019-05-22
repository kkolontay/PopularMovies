package com.kkolontay.popularmovies.View;

import android.content.Context;
import android.graphics.Movie;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.kkolontay.popularmovies.DataModel.PopularMovie;
import com.kkolontay.popularmovies.R;
import com.kkolontay.popularmovies.Sessions.NetworkUtility;
import com.kkolontay.popularmovies.Sessions.SizeImage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainViewRecyclerView extends RecyclerView.Adapter<MainViewRecyclerView.ParentViewHolder> {
    private Context context;
    private ArrayList<PopularMovie> popularMovies;
    final int LOADING = 1;
    final int ITEM = 2;
    private static final String TAG = MainViewRecyclerView.class.getSimpleName();

    public MainViewRecyclerView(Context context, ArrayList<PopularMovie> popularMovies) {
        this.context = context;
        this.popularMovies = popularMovies;
    }
    public void setPopularMovies(ArrayList<PopularMovie> popularMovies) {
        this.popularMovies = popularMovies;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ParentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        if(getItemViewType(i) == ITEM) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_card, viewGroup, false);
            PopularMovieViewHolder viewHolder = new PopularMovieViewHolder(view);
            return viewHolder;
        } else {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.progress_bar, viewGroup, false);
            ProgressBarViewHolder viewHolder = new ProgressBarViewHolder(view);
            return viewHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ParentViewHolder popularMovieViewHolder, int i) {
        Log.i(TAG, String.valueOf(i));
        if (getItemViewType(i) == ITEM) {
            if (popularMovieViewHolder instanceof PopularMovieViewHolder) {
                PopularMovieViewHolder popularHolder = (PopularMovieViewHolder) popularMovieViewHolder;

                if (popularMovies.get(i).get_poster_path() != null) {
                    String imagePath = popularMovies.get(i).get_poster_path();
                    String imageUrl = NetworkUtility.getImageURLString(imagePath, SizeImage.SMAL);
                    Picasso.get().load(imageUrl).placeholder(R.drawable.no_image).error(R.drawable.no_image).into(popularHolder.getmImageView());
                } else {
                    Picasso.get().load("").placeholder(R.drawable.no_image).error(R.drawable.no_image).into(popularHolder.getmImageView());
                }
            }
        }
       // popularMovieViewHolder.bindData(imagePath);
    }

    @Override
    public int getItemCount() {
        if (popularMovies != null) {
            int count = popularMovies.size();
            return count;
        } else {
            return  0;
        }
    }

    public void addNullData() {
        popularMovies.add(null);
        notifyItemInserted(popularMovies.size() + 1);
    }
    public void removeNull() {
        popularMovies.remove(popularMovies.size() - 1);
        notifyItemRemoved(popularMovies.size());
    }

    @Override
    public int getItemViewType(int position) {
        if(popularMovies.get(position) == null) {
            return LOADING;
        } else {
            return  ITEM;
        }
    }


    private void add(PopularMovie mc) {
        popularMovies.add(mc);
        notifyItemInserted(popularMovies.size() - 1);
    }

    public void addAll(ArrayList<PopularMovie> mcList) {
        for (PopularMovie mc : mcList) { add(mc); }
    }
//
//    public void remove(PopularMovie movie) {
//        int position = popularMovies.indexOf(movie);
//        if (position > -1) {
//            popularMovies.remove(position);
//            notifyItemRemoved(position);
//        }
//    }

//    public void clear() {
//        isLoadingAdded = false;
//        while (getItemCount() > 0) { remove(getItem(0)); }
//    }

   // public boolean isEmpty() { return getItemCount() == 0; }

//    public void addLoadingFooter() {
//        isLoadingAdded = true;
//        //add(new Movie());
//    }
//
//    public void removeLoadingFooter() {
//        isLoadingAdded = false;
//
//        int position = movies.size() - 1;
//        Movie item = getItem(position);
//        if (item != null) {
//            movies.remove(position);
//            notifyItemRemoved(position);
//        }
//    }
//
//    public PopularMovie getItem(int position) {
//        return popularMovies.get(position);
//    }



    public class ProgressBarViewHolder extends ParentViewHolder {
        public ProgressBar getProgressBar() {
            return mProgressBar;
        }

        private ProgressBar mProgressBar;


        public ProgressBarViewHolder(@NonNull View itemView) {
            super(itemView);
            mProgressBar = (ProgressBar) itemView.findViewById(R.id.progressbarViewHolder);
            mProgressBar.setVisibility(ProgressBar.VISIBLE);
        }

    }
    public class PopularMovieViewHolder extends ParentViewHolder {
        public ImageView getmImageView() {
            return mImageView;
        }

        private ImageView mImageView;


        public PopularMovieViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.movie_image);
        }

    }

    public class ParentViewHolder extends RecyclerView.ViewHolder {


        public ParentViewHolder(@NonNull View itemView) {
            super(itemView);
        }

    }
}
