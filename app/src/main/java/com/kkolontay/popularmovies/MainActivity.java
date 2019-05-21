package com.kkolontay.popularmovies;

import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kkolontay.popularmovies.DataModel.PopularMovie;
import com.kkolontay.popularmovies.ViewModel.MainActivityInterface;
import com.kkolontay.popularmovies.ViewModel.MainActivityViewModel;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public final class MainActivity extends AppCompatActivity implements MainActivityInterface {
    final String SAVEINSTANCE = "POPULAR_MOVIES";
    public static String TAG = MainActivity.class.getSimpleName();
    private Dialog dialog;
    private ConnectionState downloadingState = ConnectionState.SUCCESS;
    private ArrayList<PopularMovie> popularMovies;
    private MainActivityViewModel viewModel;
    private RecyclerView recyclerView;
    private final int COUNT_COLUMN = 3;
    private MainViewRecyclerView mainRecyclerView;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.main_recicler_view);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        GridLayoutManager manager = new GridLayoutManager(getBaseContext(), COUNT_COLUMN);
        mainRecyclerView = new MainViewRecyclerView(this, popularMovies);
        recyclerView.setAdapter(mainRecyclerView);
        recyclerView.setLayoutManager(manager);
       if (savedInstanceState == null) {
           viewModel = new MainActivityViewModel(this);
           if (isOnline() == true) {
               viewModel.nextPage();
               progressBar.setVisibility(ProgressBar.VISIBLE);
           } else {
               showAlertMassage("No connection with Internet.");
           }
       } else {
            PopularMovie[] savedMoview = (PopularMovie[]) savedInstanceState.getParcelableArray(SAVEINSTANCE);
            popularMovies = new ArrayList<PopularMovie>(Arrays.asList(savedMoview));
            mainRecyclerView.notifyDataSetChanged();
       }



    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        PopularMovie[] savedMovie = new PopularMovie[popularMovies.size()];
        savedMovie = popularMovies.toArray(savedMovie);
        outState.putParcelableArray(SAVEINSTANCE, savedMovie );
        super.onSaveInstanceState(outState, outPersistentState);
    }

    void showAlertMassage(String errorMessage) {
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

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    @Override
    public void fetchPopularMoview(ArrayList<PopularMovie> movies, ConnectionState state) {
        downloadingState = state;
        this.popularMovies = movies;
        progressBar.setVisibility(ProgressBar.INVISIBLE);
        mainRecyclerView.notifyDataSetChanged();
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


}

