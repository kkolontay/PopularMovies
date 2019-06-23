package com.kkolontay.popularmovies.ViewModel;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;

public class TeaserViewModelFactory implements ViewModelProvider.Factory{

        private Application mApplication;
        private int idMovie;
        private Context context;


        public TeaserViewModelFactory(Application application, int idMovie, Context context) {
            mApplication = application;
            this.idMovie = idMovie;
            this.context = context;
        }


        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new TeasersViewModel(mApplication, idMovie, context);
        }
}
