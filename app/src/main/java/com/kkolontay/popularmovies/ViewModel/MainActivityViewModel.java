package com.kkolontay.popularmovies.ViewModel;

import android.os.AsyncTask;
import android.util.Log;

import com.kkolontay.popularmovies.ConnectionState;
import com.kkolontay.popularmovies.DataManager.DataManager;
import com.kkolontay.popularmovies.DataModel.ResponseDataObject;
import com.kkolontay.popularmovies.Sessions.NetworkUtility;
import com.kkolontay.popularmovies.Sessions.RequestMovieError;
import com.kkolontay.popularmovies.Sessions.TypeRequest;
import com.kkolontay.popularmovies.Utility.ObjectsDataJSONParser;
import java.lang.ref.WeakReference;
import java.net.URL;


public class MainActivityViewModel {
    private DataManager dataManager;
    private WeakReference<MainActivityInterface> delegateMainActivity;
    private final String TAG = MainActivityViewModel.class.getSimpleName();
    private ConnectionState state = ConnectionState.SUCCESS;


    public MainActivityViewModel(MainActivityInterface delegate) {
        this.delegateMainActivity = new WeakReference<>(delegate);
        dataManager = DataManager.getInstance();
        dataManager.get_response().set_page(0);
    }

    private void fetchNextPageMovies() {
        int nextPage = dataManager.get_response().get_page();
        nextPage = nextPage + 1;
        URL url = null;
        if (nextPage <= dataManager.get_response().get_total_pages()) {
            url = NetworkUtility.buildURL(nextPage, TypeRequest.POPULAR);
            if (url != null) {
                new FetchMoviesList().execute(url);
            }
        }
    }

    public void nextPage() {
        fetchNextPageMovies();
    }

    private  class FetchMoviesList extends AsyncTask<URL, Void, String>  {

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            if (url != null) {
                try {
                    String result = NetworkUtility.getResponseFromHttpUrl(url);
                    state = ConnectionState.SUCCESS;
                    return  result;

                } catch(RequestMovieError e) {
                    String error = ObjectsDataJSONParser.getErrorDescription(e.getMessage());
                    state = ConnectionState.ERORR;
                    Log.i(TAG, error);
                    
                    delegateMainActivity.get().errorConnection(error, state);
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
            if (state == ConnectionState.SUCCESS) {
                ResponseDataObject responseDataObject = ObjectsDataJSONParser.getResponseDataObgert(s);
                if (responseDataObject != null) {
                    dataManager.set_response(responseDataObject);
                    delegateMainActivity.get().fetchPopularMoview(dataManager.get_response().getResults(), ConnectionState.SUCCESS);
                }

            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

    }
}

