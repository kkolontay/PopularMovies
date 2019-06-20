package com.kkolontay.popularmovies.ViewModel;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.kkolontay.popularmovies.ConnectionState;
import com.kkolontay.popularmovies.DataManager.DataManager;
import com.kkolontay.popularmovies.DataManager.Room.AppDatabase;
import com.kkolontay.popularmovies.DataManager.Room.SelectedPopularMovie;
import com.kkolontay.popularmovies.DataModel.PopularMovie;
import com.kkolontay.popularmovies.DataModel.ResponseDataObject;
import com.kkolontay.popularmovies.Sessions.NetworkUtility;
import com.kkolontay.popularmovies.Sessions.RequestMovieError;
import com.kkolontay.popularmovies.Sessions.TypeRequest;
import com.kkolontay.popularmovies.Utility.AppExecutors;
import com.kkolontay.popularmovies.Utility.ObjectsDataJSONParser;

import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;



public class MainActivityViewModel {
    private final DataManager dataManager;
    private final WeakReference<MainActivityInterface> delegateMainActivity;
    private final String TAG = MainActivityViewModel.class.getSimpleName();
    private ConnectionState state = ConnectionState.SUCCESS;
    private TypeRequest typeRequest = TypeRequest.POPULAR;
    private AppDatabase appDatabase;

    public void setTypeRequest(TypeRequest typeRequest) {
        this.typeRequest = typeRequest;
    }

    public MainActivityViewModel(MainActivityInterface delegate, Context context) {
        this.delegateMainActivity = new WeakReference<>(delegate);
        dataManager = DataManager.getInstance();
        dataManager.get_response().set_page(0);
        appDatabase = AppDatabase.getInstance(context);
    }

    private void fetchNextPageMovies() {
        int nextPage = dataManager.get_response().get_page();
        nextPage = nextPage + 1;
        if (typeRequest == TypeRequest.SELECTEDMOVIES) {
            if (nextPage <= dataManager.get_response().get_total_pages()) {
                fetchSelectedMovies();
                dataManager.get_response().set_page(nextPage);
            }
        } else {

            URL url;
            if (nextPage <= dataManager.get_response().get_total_pages()) {
                url = NetworkUtility.buildURL(nextPage, typeRequest, 0);
                if (url != null) {
                    new FetchMoviesList().execute(url);
                }
            }
        }
    }

    private void fetchSelectedMovies() {
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<SelectedPopularMovie> selectedPopularMovieList = appDatabase.selectedPopularMovie().loadAllMovies();
                if (selectedPopularMovieList != null) {
                   final ArrayList<PopularMovie> popularMovies = convertMovieData(selectedPopularMovieList);
                    Handler mainHandler = new Handler(Looper.getMainLooper());

                    Runnable myRunnable = new Runnable() {
                        @Override
                        public void run() {
                             DataManager.getInstance().get_response().setResults(popularMovies);
                            delegateMainActivity.get().fetchPopularMovie(popularMovies, ConnectionState.SUCCESS);
                        }
                    };
                    mainHandler.post(myRunnable);

                }
            }
        });
    }

    private ArrayList<PopularMovie> convertMovieData(List<SelectedPopularMovie> selectedPopularMovies) {
        ArrayList<PopularMovie> popularMovies = new ArrayList<>();
        for (SelectedPopularMovie item : selectedPopularMovies) {
            popularMovies.add(item.fetchPopularMovieEntity());
        }
        return popularMovies;
    }

    public void nextPage() {
        fetchNextPageMovies();
    }

    private class FetchMoviesList extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            if (url != null) {
                try {
                    String result = NetworkUtility.getResponseFromHttpUrl(url);
                    state = ConnectionState.SUCCESS;
                    return result;

                } catch (RequestMovieError e) {
                    String error = ObjectsDataJSONParser.getErrorDescription(e.getMessage());
                    state = ConnectionState.ERROR;
                    Log.i(TAG, error);

                    delegateMainActivity.get().errorConnection(error, state);
                    return error;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (state == ConnectionState.SUCCESS) {
                ResponseDataObject responseDataObject = ObjectsDataJSONParser.getResponseDataObject(s);
                if (responseDataObject != null) {
                    dataManager.set_response(responseDataObject);
                    delegateMainActivity.get().fetchPopularMovie(responseDataObject.getResults(), ConnectionState.SUCCESS);
                }

            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

    }
}

