package com.kkolontay.popularmovies.Sessions;

import android.net.Uri;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


public final class NetworkUtility {

    //TODO: place for your api key
    public static final String KEYAPI = "";

    private static final String HOST = "https://api.themoviedb.org/3/";
    private static final String PATH_POPULAR_MOVIE = "movie/popular";
    private static final String PATH_TOP_RATED_MOVIE = "movie/top_rated";
    private static final String PAGE_REQUEST_KEY = "page";
    private static final String API_REQUEST_KEY = "api_key";
    private static final String TAG = NetworkUtility.class.getSimpleName();
    private static final String IMAGE_HOST = "https://image.tmdb.org/t/p/";
    private static final String BIG_SIZE_IMAGE = "w780";
    private static final String SMALL_SIZE_IMAGE = "w185";
    private static final String MIDDLE_SIZE_IMAGE = "w500";
    private static final String MOVIE_VIDEO_PATH = "movie/%1$d/videos";
    private static final String MOVIE_REVIEWS_PATH = "movie/%1$d/reviews";

    public static String getResponseFromHttpUrl(URL url) throws Exception {

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        if (urlConnection.getResponseCode() > 299) {
            String message = urlConnection.getResponseMessage();
            Log.v(TAG, message);
        }
        InputStream in = urlConnection.getErrorStream();
        if (in == null) {
            in = urlConnection.getInputStream();
        }

        try {

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                if (urlConnection.getResponseCode() > 299) {
                    throw new RequestMovieError(scanner.next());
                } else {
                    return scanner.next();
                }
            } else {
                return null;
            }
        } catch (IOException ex) {
            Log.e(TAG, Log.getStackTraceString(ex));
        } finally {
            urlConnection.disconnect();
        }
        return null;
    }

    public static URL buildURL(int numberPage, TypeRequest request, int idMovie) {
        String urlString = null;
        switch (request) {
            case RATED:
                urlString = HOST + PATH_TOP_RATED_MOVIE;
                break;
            case POPULAR:
                urlString = HOST + PATH_POPULAR_MOVIE;
                break;
            case VIDEO:
                urlString = HOST + String.format(MOVIE_VIDEO_PATH, idMovie);
                break;
            case VIEWS:
                urlString = HOST + String.format(MOVIE_REVIEWS_PATH, idMovie);
                break;

        }

        Uri buildUri = null;
        if (urlString != null) {
            if (request != TypeRequest.VIDEO) {
                buildUri = Uri.parse(urlString).buildUpon()
                        .appendQueryParameter(API_REQUEST_KEY, KEYAPI)
                        .appendQueryParameter(PAGE_REQUEST_KEY, Integer.toString(numberPage)).build();
            } else {
                buildUri = Uri.parse(urlString).buildUpon()
                        .appendQueryParameter(API_REQUEST_KEY, KEYAPI).build();
            }
        }
        URL url = null;
        if (buildUri != null) {

            try {
                url = new URL(buildUri.toString());

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

        }
        Log.v(TAG, url.toString());
        return url;
    }

    public static String getImageURLString(String imagePath, SizeImage size) {
        String sizeImage;
        switch (size) {
            case BIG:
                sizeImage = NetworkUtility.BIG_SIZE_IMAGE;
                break;
            case MIDDLE:
                sizeImage = NetworkUtility.MIDDLE_SIZE_IMAGE;
                break;
            default:
                sizeImage = NetworkUtility.SMALL_SIZE_IMAGE;
        }
        return NetworkUtility.IMAGE_HOST + sizeImage + imagePath;
    }


}

