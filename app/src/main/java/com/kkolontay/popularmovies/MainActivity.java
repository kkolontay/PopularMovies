package com.kkolontay.popularmovies;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.kkolontay.popularmovies.Sessions.NetworkUtility;
import com.kkolontay.popularmovies.Sessions.RequestMovieError;
import com.kkolontay.popularmovies.Utility.ObjectsDataJSONParser;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    public static String TAG = MainActivity.class.getSimpleName();
    private volatile TextView mMessageTextView;
    private State downloadingState = State.SUCCESS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMessageTextView = (TextView) findViewById(R.id.hello);
        URL url = null;
        url = NetworkUtility.buildURL(1, NetworkUtility.TypeRequest.POPULAR);
        if (url != null) {
           // new FetchMoviesList().execute(url);
        }
    }





}

enum State {
    SUCCESS, ERORR
}