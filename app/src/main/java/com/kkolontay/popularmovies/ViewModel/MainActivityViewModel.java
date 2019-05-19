package com.kkolontay.popularmovies.ViewModel;

import android.os.AsyncTask;
import android.util.Log;

import com.kkolontay.popularmovies.DataManager.DataManager;
import com.kkolontay.popularmovies.DataModel.PopularMovie;
import com.kkolontay.popularmovies.MainActivity;
import com.kkolontay.popularmovies.Sessions.NetworkUtility;
import com.kkolontay.popularmovies.Sessions.RequestMovieError;
import com.kkolontay.popularmovies.Utility.ObjectsDataJSONParser;

import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;

public class MainActivityViewModel {
    private DataManager dataManager;
    private WeakReference<MainActivityInterface> delegate;


    public MainActivityViewModel(MainActivityInterface delegate) {
        this.delegate = new WeakReference<>(delegate);
    }
    private  class FetchMoviesList extends AsyncTask<URL, Void, String> {
        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            if (url != null) {
                try {
                    String result = NetworkUtility.getResponseFromHttpUrl(url);
                    downloadingState = MainActivity.State.SUCCESS;
                    return  result;

                } catch(RequestMovieError e) {
                    downloadingState = MainActivity.State.ERORR;
                    String error = ObjectsDataJSONParser.getErrorDescription(e.getMessage());
                    Log.i(TAG, error);
                    return error;
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

interface MainActivityInterface {
    void fetchPopularMoview(ArrayList<PopularMovie> movies, State);
}
