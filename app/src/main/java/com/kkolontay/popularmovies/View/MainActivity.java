package com.kkolontay.popularmovies.View;

import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.annotation.Nullable;
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
import com.kkolontay.popularmovies.DataManager.DataManager;
import com.kkolontay.popularmovies.DataModel.PopularMovie;
import com.kkolontay.popularmovies.DataModel.ResponseDataObject;
import com.kkolontay.popularmovies.R;
import com.kkolontay.popularmovies.Sessions.TypeRequest;
import com.kkolontay.popularmovies.View.DetailMovie.DetailMovieActivity;
import com.kkolontay.popularmovies.View.Pagination.PaginationScrollListener;
import com.kkolontay.popularmovies.ViewModel.MainActivityViewModel;
import java.util.ArrayList;


public final class MainActivity extends AppCompatActivity implements PaginationScrollListener.OnLoadMoreListener, MainViewRecyclerView.MovieAdapterOnClickHandler {
    public static final String PUTEXTRAMOVIEDETAIL = "putExtraMovieDetail";
    private ArrayList<PopularMovie> popularMovies;
    private MainActivityViewModel viewModel;
    private final int COUNT_COLUMN = 3;
    private MainViewRecyclerView mAdapter;
    private ProgressBar progressBar;
    private PaginationScrollListener paginationScrollListener;
    private TypeRequest typeRequest = TypeRequest.POPULAR;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView =  findViewById(R.id.main_recicler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        GridLayoutManager manager = new GridLayoutManager(getBaseContext(), COUNT_COLUMN);
        paginationScrollListener = new PaginationScrollListener(manager, this);
        paginationScrollListener.setLoaded();
        recyclerView.addOnScrollListener(paginationScrollListener);
        progressBar =  findViewById(R.id.progressBar);

        mAdapter = new MainViewRecyclerView(this, new ArrayList<PopularMovie>());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(manager);
        if (isOnline()) {
            initViewModel();
        }
    }

    private void initViewModel() {
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        final Observer<String> errorMessage = new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                showAlertMassage(s);
            }
        };
        viewModel.getErrorMessage().observe(this, errorMessage);

        final Observer<ArrayList<PopularMovie>> moviesObserver = new Observer<ArrayList<PopularMovie>>() {
            @Override
            public void onChanged(@Nullable ArrayList<PopularMovie> popularMovies) {
                fetchPopularMovie(popularMovies);
            }
        };
        viewModel.getMovies().observe(this, moviesObserver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.type_movie_menu, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (typeRequest == TypeRequest.SELECTEDMOVIES) {
            changeTypeMovieRequest(typeRequest);
        }
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
            case R.id.selected_list_movies:
                changeTypeMovieRequest(TypeRequest.SELECTEDMOVIES);
                return  true;
                default:
                    return super.onOptionsItemSelected(item);

        }
    }

   private void provideNextData() {
        if (isOnline()) {
            if (viewModel == null) {
                initViewModel();
            } else {
                viewModel.nextPage();
                progressBar.setVisibility(ProgressBar.VISIBLE);
            }
        } else {
            showAlertMassage("No connection with Internet.");
        }
    }

    private void changeTypeMovieRequest(TypeRequest type) {
        typeRequest = type;
        viewModel.setTypeRequest(type);
        mAdapter.setPopularMovies(new ArrayList<PopularMovie>());
        popularMovies = new ArrayList<>();
        DataManager.getInstance().resetResponse(new ResponseDataObject());
        provideNextData();
    }

    private void showAlertMassage(String errorMessage) {
        progressBar.setVisibility(ProgressBar.INVISIBLE);
              final  Dialog dialog = new Dialog(this);
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

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    public void fetchPopularMovie(ArrayList<PopularMovie> movies) {
        if (popularMovies != null && popularMovies.size() > 0) {
            mAdapter.removeNull();
        }
        this.popularMovies = DataManager.getInstance().get_response().getResults();
        progressBar.setVisibility(ProgressBar.INVISIBLE);
        mAdapter.addAll(movies);
        paginationScrollListener.setLoaded();
    }


    @Override
    public void onLoadMore() {

        if (DataManager.getInstance().get_response().get_total_pages() <= DataManager.getInstance().get_response().get_page()){
            paginationScrollListener.addEndOfRequests();
        } else {
            mAdapter.addNullData();
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

