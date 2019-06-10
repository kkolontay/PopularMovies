package com.kkolontay.popularmovies.View;

import android.content.Context;
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

public class MainViewRecyclerView extends RecyclerView.Adapter<MainViewRecyclerView.ParentViewHolder> {

    private ArrayList<PopularMovie> popularMovies;
    private final int LOADING = 1;
    private final int ITEM = 2;
    private static final String TAG = MainViewRecyclerView.class.getSimpleName();
    private MovieAdapterOnClickHandler clickHandler;

    public MainViewRecyclerView(Context context, ArrayList<PopularMovie> popularMovies) {
        this.popularMovies = popularMovies;
        if(context instanceof MainActivity) {
            clickHandler = (MainActivity) context;
        }
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
                popularHolder.setPosition(i);
                if (popularMovies.get(i).get_poster_path() != null) {
                    String imagePath = popularMovies.get(i).get_poster_path();
                    String imageUrl = NetworkUtility.getImageURLString(imagePath, SizeImage.SMALL);
                    Picasso.get().load(imageUrl).placeholder(R.drawable.no_image).error(R.drawable.no_image).into(popularHolder.getImageView());
                } else {
                    Picasso.get().load("").placeholder(R.drawable.no_image).error(R.drawable.no_image).into(popularHolder.getImageView());
                }
            }
        }
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

public interface MovieAdapterOnClickHandler {
         void movieOnClickHandler(PopularMovie movie);
}


    public class ProgressBarViewHolder extends ParentViewHolder {

        private final ProgressBar mProgressBar;

        private ProgressBarViewHolder(@NonNull View itemView) {
            super(itemView);
            mProgressBar = (ProgressBar) itemView.findViewById(R.id.progressbarViewHolder);
            mProgressBar.setVisibility(ProgressBar.VISIBLE);
        }

    }
    private class PopularMovieViewHolder extends ParentViewHolder implements View.OnClickListener {
        public ImageView getImageView() {
            return mImageView;
        }

        private ImageView mImageView;

        private void setPosition(int position) {
            this.position = position;
        }

        private int position;


        public PopularMovieViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.movie_image);
            mImageView.setOnClickListener(this);
        }



        @Override
        public void onClick(View v) {
            clickHandler.movieOnClickHandler(popularMovies.get(position));
        }
    }

    public class ParentViewHolder extends RecyclerView.ViewHolder {


        public ParentViewHolder(@NonNull View itemView) {
            super(itemView);
        }

    }
}
