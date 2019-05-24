package com.kkolontay.popularmovies.View;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kkolontay.popularmovies.ConnectionState;
import com.kkolontay.popularmovies.DataManager.DataManager;
import com.kkolontay.popularmovies.DataModel.PopularMovie;
import com.kkolontay.popularmovies.R;
import com.kkolontay.popularmovies.Sessions.TypeRequest;
import com.kkolontay.popularmovies.View.DetailMovie.DetailMovieActivity;
import com.kkolontay.popularmovies.View.Pagination.PaginationScrollListener;
import com.kkolontay.popularmovies.ViewModel.MainActivityInterface;
import com.kkolontay.popularmovies.ViewModel.MainActivityViewModel;

import java.util.ArrayList;
import java.util.Arrays;

public final class MainActivity extends AppCompatActivity implements MainActivityInterface, PaginationScrollListener.OnLoadMoreListener, MainViewRecyclerView.MovieAdapterOnClickHandler {
    private static final String SAVEINSTANCE = "POPULAR_MOVIES";
    public static final String PUTEXTRAMOVIEDETAIL = "putExtraMovieDetail";
    private ConnectionState downloadingState = ConnectionState.SUCCESS;
    private ArrayList<PopularMovie> popularMovies;
    private MainActivityViewModel viewModel;
    private final int COUNT_COLUMN = 3;
    private MainViewRecyclerView mAdapter;
    private ProgressBar progressBar;
    private PaginationScrollListener paginationScrollListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.main_recicler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        GridLayoutManager manager = new GridLayoutManager(getBaseContext(), COUNT_COLUMN);
        paginationScrollListener = new PaginationScrollListener(manager, this);
        paginationScrollListener.setLoaded();
        recyclerView.addOnScrollListener(paginationScrollListener);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        mAdapter = new MainViewRecyclerView(this, new ArrayList<PopularMovie>());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(manager);
       if (savedInstanceState == null) {
           viewModel = new MainActivityViewModel(this);
           provideNextData();
       } else {
            PopularMovie[] savedMovie = (PopularMovie[]) savedInstanceState.getParcelableArray(SAVEINSTANCE);
            if (savedMovie != null) {
                popularMovies = new ArrayList<PopularMovie>(Arrays.asList(savedMovie));
                mAdapter.setPopularMovies(popularMovies);
            }
       }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        PopularMovie[] savedMovie = new PopularMovie[popularMovies.size()];
        savedMovie = popularMovies.toArray(savedMovie);
        outState.putParcelableArray(SAVEINSTANCE, savedMovie );
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.type_movie_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.popular_movie:
                changeTypeMovieRequest(TypeRequest.POPULAR);
                return true;
            case R.id.top_rated_movie:
                changeTypeMovieRequest(TypeRequest.RATED);
               return  true;
                default:
                    return super.onOptionsItemSelected(item);

        }
    }

   private void provideNextData() {
        if (isOnline()) {
            viewModel.nextPage();
            progressBar.setVisibility(ProgressBar.VISIBLE);
        } else {
            showAlertMassage("No connection with Internet.");
        }
    }

    private void changeTypeMovieRequest(TypeRequest type) {
        viewModel.setTypeRequest(type);
        mAdapter.setPopularMovies(new ArrayList<PopularMovie>());
        popularMovies = new ArrayList<>();
        DataManager.getInstance().get_response().set_page(0);
        DataManager.getInstance().get_response().setResults(new ArrayList<PopularMovie>());
        provideNextData();
    }

    private void showAlertMassage(String errorMessage) {
        progressBar.setVisibility(ProgressBar.INVISIBLE);
              final  Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.alert_dialog);
                dialog.setTitle("Error Message");

                TextView text = (TextView) dialog.findViewById(R.id.error_message_tv);
                text.setText(errorMessage);

                Button dialogButton = (Button) dialog.findViewById(R.id.alert_dialog_message_bt);
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

    @Override
    public void fetchPopularMovie(ArrayList<PopularMovie> movies, ConnectionState state) {
        downloadingState = state;
        if (popularMovies != null && popularMovies.size() > 0) {
            mAdapter.removeNull();
        }
        this.popularMovies = DataManager.getInstance().get_response().getResults();
        progressBar.setVisibility(ProgressBar.INVISIBLE);
        mAdapter.addAll(movies);
        paginationScrollListener.setLoaded();
    }

    @Override
    public void errorConnection(String error, ConnectionState state) {
        downloadingState = state;
        final String messageError = error;
      this.runOnUiThread(new Runnable() {
          @Override
          public void run() {
              showAlertMassage(messageError);
          }
      });


    }


    @Override
    public void onLoadMore() {
        mAdapter.addNullData();
        if (DataManager.getInstance().get_response().get_total_pages() <= DataManager.getInstance().get_response().get_page()){
            paginationScrollListener.addEndOfRequests();
        } else {
            provideNextData();
        }

    }

    @Override
    public void movieOnClickHandler(PopularMovie movie) {
        Intent detailIntent = new Intent(this, DetailMovieActivity.class);
        detailIntent.putExtra(PUTEXTRAMOVIEDETAIL, movie);
        startActivity(detailIntent);

    }
}

