package com.kkolontay.popularmovies.Utility;

import com.kkolontay.popularmovies.DataModel.PopularMovie;
import com.kkolontay.popularmovies.DataModel.ResponseDataObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ObjectsDataJSONParser {
    private static final String STATUSMESSAGE = "status_message";
    //private static final String STATUSCODE = "status_code";

    public static ResponseDataObject getResponseDataObgert(String json) {
        ResponseDataObject responseDataObject = new ResponseDataObject();
        try {
            JSONObject object = new JSONObject(json);
            int page = object.getInt(ResponseDataObject.PAGE);
            responseDataObject.set_page(page);
            int totalResults = object.getInt(ResponseDataObject.TOTALRESULTS);
            responseDataObject.set_total_resuts(totalResults);
            int totalPages = object.getInt(ResponseDataObject.TOTALPAGES);
            responseDataObject.set_total_pages(totalPages);
            JSONArray arrayMoviesObject = object.getJSONArray(ResponseDataObject.RESULTS);
            ArrayList<PopularMovie> movies = getListOfMovies(arrayMoviesObject);
            if (movies != null ) {
                responseDataObject.setResults(movies);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return responseDataObject;
    }

    public static String getErrorDescription(String json) {
        try {
            JSONObject object = new JSONObject(json);
            String errorMessage = object.getString(STATUSMESSAGE);
            return errorMessage;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static ArrayList<PopularMovie> getListOfMovies(JSONArray objects) {
        ArrayList<PopularMovie> moviesList = new ArrayList<>();
        for (int i = 0; i < objects.length(); i++) {
            try {
                JSONObject object = objects.getJSONObject(i);
                PopularMovie movie = getPopularMoviewObject(object);
                if (movie != null) {
                    moviesList.add(movie);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return moviesList;
    }

    private static PopularMovie getPopularMoviewObject(JSONObject object) {
        PopularMovie movie = new PopularMovie();
        try {
            int voteCount = object.getInt(PopularMovie.VOTECOUNT);
            movie.set_vote_count(voteCount);
            int id = object.getInt(PopularMovie.ID);
            movie.set_id(id);
            boolean video = object.getBoolean(PopularMovie.VIDEO);
            movie.set_video(video);
            double voteAverage = object.getDouble(PopularMovie.VOTE_AVERAGE);
            movie.set_vote_average(voteAverage);
            String title = object.getString(PopularMovie.TITLE);
            movie.set_title(title);
            double populatiry = object.getDouble(PopularMovie.POPULARITY);
            movie.set_popularity(populatiry);
            String posterPath = object.getString(PopularMovie.POSTED_PATH);
            if (posterPath != null) {
                movie.set_poster_path(posterPath);
            }
            String originalTitle = object.getString(PopularMovie.ORIGINAL_TITLE);
            movie.set_original_title(originalTitle);
            JSONArray genders = object.optJSONArray(PopularMovie.GENRE_ID);
            if (genders != null) {
                int[] genderId = new int[genders.length()];
                for(int i = 0; i < genders.length(); i++) {
                    genderId[i] = genders.optInt(i);
                }
                movie.set_genre_ids(genderId);
            }
            String backdropPath = object.getString(PopularMovie.BACKGROUND_PATH);
            movie.set_backdrop_path(backdropPath);
            String overView = object.getString(PopularMovie.OVERVIEW);
            movie.set_overview(overView);
            String releaseData = object.getString(PopularMovie.RELEASE_DATE);
            movie.set_release_date(releaseData);

        } catch (Exception e) {
            e.printStackTrace();
            return  null;
        }
        return movie;
    }
}
