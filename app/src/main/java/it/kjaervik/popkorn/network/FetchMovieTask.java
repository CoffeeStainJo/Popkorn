package it.kjaervik.popkorn.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import it.kjaervik.popkorn.BuildConfig;
import it.kjaervik.popkorn.model.Movie;
import it.kjaervik.popkorn.util.MovieDataParser;
import it.kjaervik.popkorn.util.TheMovieDB;

public class FetchMovieTask extends AsyncTask<String, Void, List<Movie>> {

    private static final String LOG_TAG = FetchMovieTask.class.getSimpleName();
    private ArrayAdapter<Movie> adapter;

    private HttpURLConnection urlConnection;
    private BufferedReader bufferedReader;

    public FetchMovieTask(ArrayAdapter<Movie> adapter) {
        this.adapter = adapter;
    }

    @Override
    protected List<Movie> doInBackground(String... queryParameters) {

        if (queryParameters.length == 0) return null;

        return fetchMovieData(queryParameters[0]);
    }

    private List<Movie> fetchMovieData(String sortOrder) {

        urlConnection  = null;
        bufferedReader = null;

        String moviesJsonString = null;

        try {
            InputStream inputStream = makeHttpRequestToTheMovieDBApi(sortOrder);
            StringBuffer buffer = readResponseFromInputStream(inputStream);

            if (buffer == null) return null;

            moviesJsonString = buffer.toString();

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error: ", e);
            e.printStackTrace();
        } finally {
            cleanup();
        }

        return getMovies(moviesJsonString);
    }

    private InputStream makeHttpRequestToTheMovieDBApi(String sortOrder) throws IOException {

        Uri builtUri = buildUri(sortOrder);
        URL url = new URL(builtUri.toString());
        Log.v(LOG_TAG, "Making network call: " + builtUri.toString());
        urlConnection = (HttpURLConnection)url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.connect();
        return urlConnection.getInputStream();
    }

    private Uri buildUri(String sortOrder) {
        return Uri.parse(TheMovieDB.BASE_URL)
                .buildUpon()
                .appendPath(sortOrder)
                .appendQueryParameter(TheMovieDB.APIKEY_PARAM, BuildConfig.MOVIE_DB_API_KEY)
                .build();
    }

    private StringBuffer readResponseFromInputStream(InputStream inputStream) throws IOException {
        StringBuffer buffer = new StringBuffer();

        if (inputStream == null) return null;

        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        String inputLine;

        while ((inputLine = bufferedReader.readLine()) != null)
            buffer.append(inputLine).append("\n");

        if (buffer.length() == 0) // no point in parsing
            return null;

        return buffer;
    }

    private List<Movie> getMovies(String moviesJsonString) {
        try {
            return MovieDataParser.getMovieDataFromJson(moviesJsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Movie> movies) {
        if (movies != null) {
            adapter.clear();
            adapter.addAll(movies);
            adapter.notifyDataSetChanged();
        }
    }

    private void cleanup() {
        if (urlConnection != null)
            urlConnection.disconnect();
        if (bufferedReader != null)
            closeReader();

    }

    private void closeReader() {
        try {
            bufferedReader.close();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing stream: ", e);
        }
    }

    public boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
