package com.kkolontay.popularmovies;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.kkolontay.popularmovies.Sessions.NetworkUtility;
import com.kkolontay.popularmovies.Sessions.RequestMovieError;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    public static String TAG = MainActivity.class.getSimpleName();
    private TextView mMessageTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMessageTextView = (TextView) findViewById(R.id.hello);
        URL url = null;
        url = NetworkUtility.buildURL(1, NetworkUtility.TypeRequest.POPULAR);
        if (url != null) {
            new FetchMoviesList().execute(url);
        }
    }

    public class FetchMoviesList extends AsyncTask<URL, Void, String> {
        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            if (url != null) {
                try {
                    String result = NetworkUtility.getResponseFromHttpUrl(url);
                    return  result;

                } catch(RequestMovieError e) {
                    mMessageTextView.setText(e.getMessage());
                    Log.i(TAG, e.getMessage());
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mMessageTextView.setText(s);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }

}
