package it.kjaervik.popkorn.util;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import it.kjaervik.popkorn.model.Movie;

public class MovieDataParser {

    private static final String LOG_TAG = MovieDataParser.class.getSimpleName();

    public static List<Movie> getMovieDataFromJson(String moviesJsonString) throws JSONException {

        JSONObject apiResponse = new JSONObject(moviesJsonString);
        JSONArray movies = apiResponse.getJSONArray(TheMovieDB.API_RESULTS);

        return Movie.getMoviesFromJsonArray(movies);
    }

    public static String resolvePosterPathFromStringPath(String path) {

        return Uri.parse(TheMovieDB.IMAGE_BASE_URL).buildUpon()
                .appendPath(TheMovieDB.IMAGE_SIZE)
                .appendPath(path)
                .build().toString();
    }

    public static String resolveYouTubeThumbnailFromUri(String uri) {

        return Uri.parse("http://img.youtube.com").buildUpon()
                .appendPath("vi")
                .appendPath(uri)
                .appendPath("0.jpg")
                .build().toString();
    }

    public static Movie getMovieDetailsFromJson(String moviesJsonString, String requestType) throws JSONException {
        JSONObject apiResponse = new JSONObject(moviesJsonString);
        JSONArray movies = apiResponse.getJSONArray(TheMovieDB.API_RESULTS);

        Movie movie = new Movie();

        for (int i = 0; i < movies.length(); i++) {

            if (requestType.equals(TheMovieDB.VIDEO_URL_PATH))
                movie.getVideos().add(movies.getJSONObject(i).getString("key"));
            else
                movie.getReviews().add(movies.getJSONObject(i).getString("url"));
        }

        Log.d(LOG_TAG, movie.toString());

        return movie;
    }
}
