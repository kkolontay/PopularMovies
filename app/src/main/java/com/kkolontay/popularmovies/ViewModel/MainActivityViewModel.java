package com.kkolontay.popularmovies.ViewModel;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;
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
import java.net.URL;
import java.util.ArrayList;
import java.util.List;



public class MainActivityViewModel extends AndroidViewModel {
    private final DataManager dataManager;
    private final String TAG = MainActivityViewModel.class.getSimpleName();
    private TypeRequest typeRequest = TypeRequest.POPULAR;

    public MutableLiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public MutableLiveData<ArrayList<PopularMovie>> getMovies() {
        return movies;
    }

    private MutableLiveData<String> errorMessage;
    private MutableLiveData<ArrayList<PopularMovie>> movies;
    private AppDatabase appDatabase;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        dataManager = DataManager.getInstance();
        dataManager.get_response().set_page(0);
        appDatabase = AppDatabase.getInstance(getApplication().getApplicationContext());
       errorMessage = new MutableLiveData<>();
       movies = new MutableLiveData<>();
       nextPage();
    }

    public void setTypeRequest(TypeRequest typeRequest) {
        this.typeRequest = typeRequest;
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
                            movies.setValue(popularMovies);
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
                    return result;

                } catch (RequestMovieError e) {
                    final String error = ObjectsDataJSONParser.getErrorDescription(e.getMessage());
                    Log.i(TAG, error);

                   Handler handler = new Handler(Looper.getMainLooper());
                   handler.post(new Runnable() {
                       @Override
                       public void run() {
                           errorMessage.setValue(error);
                       }
                   });
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
                ResponseDataObject responseDataObject = ObjectsDataJSONParser.getResponseDataObject(s);
                if (responseDataObject != null) {
                    dataManager.set_response(responseDataObject);
                    movies.setValue(responseDataObject.getResults());
                }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

    }
}

