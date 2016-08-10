package it.kjaervik.popkorn.util;

import android.net.Uri;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import it.kjaervik.popkorn.model.Movie;

public class MovieDataParser {

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
}
