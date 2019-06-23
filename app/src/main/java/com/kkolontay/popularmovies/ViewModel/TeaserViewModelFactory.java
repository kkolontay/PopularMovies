package com.kkolontay.popularmovies.ViewModel;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

public class TeaserViewModelFactory implements ViewModelProvider.Factory{

        private Application mApplication;
        private int idMovie;


        public TeaserViewModelFactory(Application application, int idMovie) {
            mApplication = application;
            this.idMovie = idMovie;
        }


        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new TeasersViewModel(mApplication, idMovie);
        }
}
