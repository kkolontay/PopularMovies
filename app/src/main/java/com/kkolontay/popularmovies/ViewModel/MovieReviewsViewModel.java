package com.kkolontay.popularmovies.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;

import com.kkolontay.popularmovies.ConnectionState;
import com.kkolontay.popularmovies.DataModel.MovieReview;
import com.kkolontay.popularmovies.DataModel.Reviews;
import com.kkolontay.popularmovies.Sessions.NetworkUtility;
import com.kkolontay.popularmovies.Sessions.RequestMovieError;
import com.kkolontay.popularmovies.Sessions.TypeRequest;
import com.kkolontay.popularmovies.Utility.ObjectsDataJSONParser;

import java.net.URL;
import java.util.ArrayList;


public class MovieReviewsViewModel extends AndroidViewModel {

    private ConnectionState state;
    private static final String TAG = ConnectionState.class.getSimpleName();
    private MutableLiveData<String> errorMessage;
    private MutableLiveData<Integer> nextPage;
    private MutableLiveData<ArrayList<MovieReview>> reviewsMovie;
    private Reviews reviews;


    public MovieReviewsViewModel(@NonNull Application application) {
        super(application);
        reviewsMovie = new MutableLiveData<>();
        nextPage = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();
    }

    public MutableLiveData<Integer> getNextPage() {
        return nextPage;
    }

    public LiveData<ArrayList<MovieReview>> getReviews() {
        return reviewsMovie;
    }

    public MutableLiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void fetchNextPage(int pageNumber, int idMovie) {


                URL url = NetworkUtility.buildURL(pageNumber, TypeRequest.VIEWS, idMovie);

                if (url != null) {
                    new FetchMoviesList().execute(url);
                }
    }


    private  class FetchMoviesList extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            if (url != null) {
                try {
                    String result = NetworkUtility.getResponseFromHttpUrl(url);
                    state = ConnectionState.SUCCESS;
                    return result;

                } catch (RequestMovieError e) {
                   final String error = ObjectsDataJSONParser.getErrorDescription(e.getMessage());
                    state = ConnectionState.ERROR;
                    Log.i(TAG, error);
                    Handler handler = new Handler(Looper.getMainLooper());
                    Runnable task = new Runnable() {
                        @Override
                        public void run() {
                            errorMessage.setValue(error);
                        }
                    };
                    handler.post(task);

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
                Reviews responseDataObject = ObjectsDataJSONParser.getCustomerMovieReviews(s);
                if (responseDataObject != null) {
                    fetchReviewMovies(responseDataObject);
                }

            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

    }

    private void fetchReviewMovies(Reviews newRevieMovies) {
        if (reviews == null) {
            reviews = newRevieMovies;
            reviewsMovie.setValue(newRevieMovies.getResults());
            if(newRevieMovies.getPage() < newRevieMovies.getTotalPages()) {
                int next = newRevieMovies.getPage() + 1;
                nextPage.setValue(next);
            } else {
                nextPage.setValue(-1);
            }
        } else {
            reviewsMovie.setValue(newRevieMovies.getResults());
            if(newRevieMovies.getPage() < newRevieMovies.getTotalPages()) {
                int next = newRevieMovies.getPage() + 1;
                nextPage.setValue(next);
                reviews.setPage(newRevieMovies.getPage());
                ArrayList<MovieReview> existList = reviews.getResults();
                existList.addAll(newRevieMovies.getResults());
                reviews.setResults(existList);

            } else {
                nextPage.setValue(-1);
            }
        }
    }
}
