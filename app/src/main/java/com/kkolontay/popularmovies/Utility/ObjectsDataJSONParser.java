package com.kkolontay.popularmovies.Utility;
import com.kkolontay.popularmovies.DataModel.MovieReview;
import com.kkolontay.popularmovies.DataModel.PopularMovie;
import com.kkolontay.popularmovies.DataModel.ResponseDataObject;
import com.kkolontay.popularmovies.DataModel.Reviews;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public class ObjectsDataJSONParser {
    private static final String STATUSMESSAGE = "status_message";
    private  static final String RESULTS = "results";
    private static final String KEY_TEASER = "key";
    private static final String NAME_TEASER = "name";

    public static ResponseDataObject getResponseDataObject(String json) {
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

    public static Reviews getCustomerMovieReviews(String json) {
        Reviews reviews = new Reviews();
        try {
            JSONObject object = new JSONObject(json);
            int id = object.getInt(Reviews.ID);
            reviews.setId(id);
            int page = object.getInt(Reviews.PAGE);
            reviews.setPage(page);
            int totalPages = object.getInt(Reviews.TOTALPAGES);
            reviews.setTotalPages(totalPages);
            int totalReviews = object.getInt(Reviews.TOTALRESULTS);
            reviews.setTotalResults(totalReviews);
            JSONArray jsonArray = object.getJSONArray(Reviews.RESULTS);
            if (jsonArray != null) {
                reviews.setResults(getItemsMovieReview(jsonArray));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return reviews;
    }

    private static ArrayList<MovieReview> getItemsMovieReview(JSONArray jsonArray) {
        ArrayList<MovieReview> movieReviews = new ArrayList<>();
        for(int i = 0; i <jsonArray.length(); i++) {
            try {
                MovieReview movieReview = new MovieReview();
                JSONObject object = jsonArray.getJSONObject(i);
                String author = object.getString(MovieReview.AUTHOR);
                movieReview.setAuthor(author);
                String content = object.getString(MovieReview.CONTENT);
                movieReview.setContent(content);
                String id = object.getString(MovieReview.ID);
                movieReview.setId(id);
                String url = object.getString(MovieReview.URL);
                movieReview.setUrl(url);
                movieReviews.add(movieReview);

            } catch (Exception e) {
                e.printStackTrace();
                return movieReviews;
            }
        }
        return movieReviews;
    }

    public static String getIdVideoTeaser( String json) {
        try {
            JSONObject object = new JSONObject(json);
            JSONArray results = object.getJSONArray(RESULTS);
            JSONObject objectMovie = results.getJSONObject(0);
            if (objectMovie != null) {
                return  objectMovie.getString(KEY_TEASER);
            }


        } catch (Exception e) {
            e.printStackTrace();
            return  null;
        }
        return  null;
    }

    public static ArrayList<VideoTeaserItem> getVideoTeasers( String json) {
        try {
            JSONObject object = new JSONObject(json);
            JSONArray results = object.getJSONArray(RESULTS);
            ArrayList<VideoTeaserItem> videoTeaserItems = new ArrayList<>();
            for( int i = 0; i < results.length(); i++ ) {
                VideoTeaserItem item = new VideoTeaserItem();
                JSONObject objectMovie = results.getJSONObject(0);
                if (objectMovie != null) {
                    String key = objectMovie.getString(KEY_TEASER);
                    String name = objectMovie.getString(NAME_TEASER);
                    item.setKey(key);
                    item.setName(name);
                    videoTeaserItems.add(item);
                }
            }

            return videoTeaserItems;
        } catch (Exception e) {
            e.printStackTrace();
            return  null;
        }
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
                PopularMovie movie = getPopularMovieObject(object);
                if (movie != null) {
                    moviesList.add(movie);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return moviesList;
    }

    private static PopularMovie getPopularMovieObject(JSONObject object) {
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

