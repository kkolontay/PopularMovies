package com.kkolontay.popularmovies;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kkolontay.popularmovies.DataModel.PopularMovie;
import com.kkolontay.popularmovies.ViewModel.MainActivityInterface;
import com.kkolontay.popularmovies.ViewModel.MainActivityViewModel;
import java.util.ArrayList;

public final class MainActivity extends AppCompatActivity implements MainActivityInterface {

    public static String TAG = MainActivity.class.getSimpleName();
    private Dialog dialog;
    private ConnectionState downloadingState = ConnectionState.SUCCESS;
    private ArrayList<PopularMovie> popularMovies;
    private MainActivityViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       if (savedInstanceState == null) {
           viewModel = new MainActivityViewModel(this);
           if (isOnline() == true) {
               viewModel.nextPage();
           } else {
               showAlertMassage("No connection with Internet.");
           }
       }

    }
    void showAlertMassage(String errorMessage) {
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

