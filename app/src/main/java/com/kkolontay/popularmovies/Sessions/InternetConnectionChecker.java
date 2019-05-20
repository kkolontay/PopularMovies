package com.kkolontay.popularmovies.Sessions;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class InternetConnectionChecker extends AsyncTask<Void, Void, Boolean> {

    private Customer mCustomer;

    public InternetConnectionChecker( Customer customer) {

        mCustomer = customer;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress("8.8.8.8", 53), 1500);
            socket.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return  false;
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        mCustomer.accept(aBoolean);
    }

    public interface Customer {
        void accept(Boolean internet);
    }
}
