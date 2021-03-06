package com.kkolontay.popularmovies.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;

import com.kkolontay.popularmovies.DataManager.Room.AppDatabase;
import com.kkolontay.popularmovies.Sessions.NetworkUtility;
import com.kkolontay.popularmovies.Sessions.RequestMovieError;
import com.kkolontay.popularmovies.Sessions.TypeRequest;
import com.kkolontay.popularmovies.Utility.AppExecutors;
import com.kkolontay.popularmovies.Utility.ObjectsDataJSONParser;
import com.kkolontay.popularmovies.Utility.VideoTeaserItem;

import java.net.URL;
import java.util.ArrayList;

public class TeasersViewModel extends AndroidViewModel {
    private int idMovie;
    private MutableLiveData<Boolean> isSelected;
    private MutableLiveData<ArrayList<VideoTeaserItem>> videoTeasers;
    private MutableLiveData<String> errorMessage;


    private Context context;
    private static final String TAG = TeasersViewModel.class.getSimpleName();

    public TeasersViewModel(@NonNull Application application, int idMovie, Context context) {
        super(application);
        this.idMovie = idMovie;
        videoTeasers = new MutableLiveData<>();
        errorMessage  = new MutableLiveData<>();
        isSelected = new MutableLiveData<>();
        this.context = context;
        URL url = NetworkUtility.buildURL(0, TypeRequest.VIDEO, idMovie);
        if (url != null) {
            new FetchYoutubeLinkShortVideo().execute(url);
        }
        setItemIsSelected();
    }

    public MutableLiveData<Boolean> getIsSelected() {
        return isSelected;
    }

    public MutableLiveData<ArrayList<VideoTeaserItem>> getVideoTeasers() {
        return videoTeasers;
    }

    public MutableLiveData<String> getErrorMessage() {
        return errorMessage;
    }

    private void setItemIsSelected() {
       final AppDatabase appDatabase = AppDatabase.getInstance(context);

        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                final Boolean isMovieSelected;
                if (appDatabase.selectedPopularMovie().fetchMovie(idMovie) == null) {
                    isMovieSelected = false;
                } else {
                    isMovieSelected = true;
                }
                Handler handler = new Handler(Looper.getMainLooper());
                Runnable task = new Runnable() {
                    @Override
                    public void run() {
                        isSelected.setValue(isMovieSelected);
                    }
                };
                handler.post(task);
            }
        });

    }

    private class FetchYoutubeLinkShortVideo extends AsyncTask<URL, Void, String> {

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
                 ArrayList<VideoTeaserItem> videoTeaserItems = ObjectsDataJSONParser.getVideoTeasers(s);
                 videoTeasers.setValue(videoTeaserItems);

        }
    }

}
