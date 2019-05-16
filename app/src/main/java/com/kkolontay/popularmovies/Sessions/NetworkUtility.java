package com.kkolontay.popularmovies.Sessions;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.kkolontay.popularmovies.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtility {

    //TODO: Past API KEY

    private static final  String API_KEY = "";
    private static final String HOST = "https://api.themoviedb.org/3/";
    private static final String PATH_POPULAR_MOVIE = "movie/popular";
    private static final String PATH_TOP_RATED_MOVIE = "movie/top_rated";
    private static final String PAGE_REQUEST_KEY = "page";
    private static final String API_REQUEST_KEY = "api_key";
    private static final String TAG = NetworkUtility.class.getSimpleName();

    public static String getResponseFromHttpUrl(URL url) throws Exception { // IOExeption

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        InputStream in = urlConnection.getErrorStream();
        if (in == null) {
            in = urlConnection.getInputStream();
        }

        try {

           // InputStream in = urlConnection.getInputStream();

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
        }
        catch (IOException ex) {
            Log.e(TAG,Log.getStackTraceString(ex));
        }
        finally {
            urlConnection.disconnect();
        }
        return  null;
    }

    public static URL buildURL(int numberPage, TypeRequest request) {
        String urlString = null;
        if (request == TypeRequest.POPULAR) {
            urlString = HOST + PATH_POPULAR_MOVIE;
        } else {
            urlString = HOST + PATH_TOP_RATED_MOVIE;
        }
        Uri buildUri = null;
        if (urlString != null) {
            buildUri = Uri.parse(urlString).buildUpon()
                    .appendQueryParameter(API_REQUEST_KEY, API_KEY)
                    .appendQueryParameter(PAGE_REQUEST_KEY, Integer.toString(numberPage)).build();
        }
        URL url = null;
        if (buildUri != null) {

            try {
                url = new URL(buildUri.toString());

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

        }
        return url;
    }


    public enum TypeRequest {
        POPULAR, RATED
    }
}





//public class FetchWeatherTask extends AsyncTask<String, Void, String[]> {
//
//    // COMPLETED (6) Override the doInBackground method to perform your network requests
//    @Override
//    protected String[] doInBackground(String... params) {
//
//        /* If there's no zip code, there's nothing to look up. */
//        if (params.length == 0) {
//            return null;
//        }
//
//        String location = params[0];
//        URL weatherRequestUrl = NetworkUtils.buildUrl(location);
//
//        try {
//            String jsonWeatherResponse = NetworkUtils
//                    .getResponseFromHttpUrl(weatherRequestUrl);
//
//            String[] simpleJsonWeatherData = OpenWeatherJsonUtils
//                    .getSimpleWeatherStringsFromJson(MainActivity.this, jsonWeatherResponse);
//
//            return simpleJsonWeatherData;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//

//
//    // COMPLETED (7) Override the onPostExecute method to display the results of the network request
//    @Override
//    protected void onPostExecute(String[] weatherData) {
//        if (weatherData != null) {
//            /*
//             * Iterate through the array and append the Strings to the TextView. The reason why we add
//             * the "\n\n\n" after the String is to give visual separation between each String in the
//             * TextView. Later, we'll learn about a better way to display lists of data.
//             */
//            for (String weatherString : weatherData) {
//                mWeatherTextView.append((weatherString) + "\n\n\n");
//            }
//        }
//    }
//}

//public static String[] getSimpleWeatherStringsFromJson(Context context, String forecastJsonStr)
//        throws JSONException {
//
///* Weather information. Each day's forecast info is an element of the "list" array */
//final String OWM_LIST = "list";
//
///* All temperatures are children of the "temp" object */
//final String OWM_TEMPERATURE = "temp";
//
///* Max temperature for the day */
//final String OWM_MAX = "max";
//final String OWM_MIN = "min";
//
//final String OWM_WEATHER = "weather";
//final String OWM_DESCRIPTION = "main";
//
//final String OWM_MESSAGE_CODE = "cod";
//
//        /* String array to hold each day's weather String */
//        String[] parsedWeatherData = null;
//
//        JSONObject forecastJson = new JSONObject(forecastJsonStr);
//
//        /* Is there an error? */
//        if (forecastJson.has(OWM_MESSAGE_CODE)) {
//        int errorCode = forecastJson.getInt(OWM_MESSAGE_CODE);
//
//        switch (errorCode) {
//        case HttpURLConnection.HTTP_OK:
//        break;
//        case HttpURLConnection.HTTP_NOT_FOUND:
//        /* Location invalid */
//        return null;
//default:
//        /* Server probably down */
//        return null;
//        }
//        }
//
//        JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);
//
//        parsedWeatherData = new String[weatherArray.length()];
//
//        long localDate = System.currentTimeMillis();
//        long utcDate = SunshineDateUtils.getUTCDateFromLocal(localDate);
//        long startDay = SunshineDateUtils.normalizeDate(utcDate);
//
//        for (int i = 0; i < weatherArray.length(); i++) {
//        String date;
//        String highAndLow;
//
//        /* These are the values that will be collected */
//        long dateTimeMillis;
//        double high;
//        double low;
//        String description;
//
//        /* Get the JSON object representing the day */
//        JSONObject dayForecast = weatherArray.getJSONObject(i);
//
//        /*
//         * We ignore all the datetime values embedded in the JSON and assume that
//         * the values are returned in-order by day (which is not guaranteed to be correct).
//         */
//        dateTimeMillis = startDay + SunshineDateUtils.DAY_IN_MILLIS * i;
//        date = SunshineDateUtils.getFriendlyDateString(context, dateTimeMillis, false);
//
//        /*
//         * Description is in a child array called "weather", which is 1 element long.
//         * That element also contains a weather code.
//         */
//        JSONObject weatherObject =
//        dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
//        description = weatherObject.getString(OWM_DESCRIPTION);
//
//        /*
//         * Temperatures are sent by Open Weather Map in a child object called "temp".
//         *
//         * Editor's Note: Try not to name variables "temp" when working with temperature.
//         * It confuses everybody. Temp could easily mean any number of things, including
//         * temperature, temporary and is just a bad variable name.
//         */
//        JSONObject temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE);
//        high = temperatureObject.getDouble(OWM_MAX);
//        low = temperatureObject.getDouble(OWM_MIN);
//        highAndLow = SunshineWeatherUtils.formatHighLows(context, high, low);
//
//        parsedWeatherData[i] = date + " - " + description + " - " + highAndLow;
//        }
//
//        return parsedWeatherData;
//        }

