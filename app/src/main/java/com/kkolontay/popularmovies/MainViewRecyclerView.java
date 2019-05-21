package com.kkolontay.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kkolontay.popularmovies.DataModel.PopularMovie;
import com.kkolontay.popularmovies.Sessions.NetworkUtility;
import com.kkolontay.popularmovies.Sessions.SizeImage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainViewRecyclerView extends RecyclerView.Adapter<MainViewRecyclerView.PopularMovieViewHolder> {
    private Context context;
    private ArrayList<PopularMovie> popularMovies;

    public MainViewRecyclerView(Context context, ArrayList<PopularMovie> popularMovies) {
        this.context = context;
        this.popularMovies = popularMovies;
    }

    @NonNull
    @Override
    public PopularMovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_card, viewGroup, false);
        PopularMovieViewHolder viewHolder = new PopularMovieViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PopularMovieViewHolder popularMovieViewHolder, int i) {
        String imagePath = popularMovies.get(i).get_poster_path();
        popularMovieViewHolder.bindData(imagePath);
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

    public class PopularMovieViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;


        public PopularMovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.movie_image);
        }

        void bindData(String urlString) {
            String imageUrl = NetworkUtility.getImageURLString(urlString, SizeImage.SMAL);
            Picasso.get().load(urlString).placeholder(R.drawable.no_image).error(R.drawable.no_image).into(imageView);
        }
    }
}
