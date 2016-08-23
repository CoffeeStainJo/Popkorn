package it.kjaervik.popkorn;

import android.app.ActionBar;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import it.kjaervik.popkorn.model.Movie;
import it.kjaervik.popkorn.model.Trailer;
import it.kjaervik.popkorn.network.NetworkUtils;
import it.kjaervik.popkorn.util.MovieDataParser;
import it.kjaervik.popkorn.util.TheMovieDB;
import it.kjaervik.popkorn.util.TrailerAdapter;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment {

    private TextView title, releaseDate, userRating, synopsis;
    private ImageView thumbnail;
    private Movie movie;
    private GridView movieTrailers;
    private TrailerAdapter trailerDapter;
    private LinearLayout reviews;

    public DetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        movie = getActivity()
                .getIntent()
                .getParcelableExtra("movie");

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        getUiElements(rootView);
        populateUiElementsFromMovie(movie);

        trailerDapter = new TrailerAdapter(getActivity(), new ArrayList<Trailer>());
        movieTrailers.setAdapter(trailerDapter);

        movieTrailers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String uri = ((Trailer) parent.getItemAtPosition(position)).getKey();
                Toast.makeText(getActivity(), uri, Toast.LENGTH_SHORT).show();

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com").buildUpon()
                        .appendPath("watch")
                        .appendQueryParameter("v", uri)
                        .build()));
            }
        });

        return rootView;
    }

    private void populateUiElementsFromMovie(Movie movie) {
        title.setText(movie.getTitle());
        releaseDate.setText(movie.getReleaseDate());
        userRating.setText(Double.toString(movie.getUserRating()) + "/10");
        synopsis.setText(movie.getSynopsis());
        Picasso
                .with(getActivity())
                .load(MovieDataParser.resolvePosterPathFromStringPath(movie.getPosterImageUri()))
                .into(thumbnail);
    }

    private void getUiElements(View rootView) {
        title = (TextView) rootView.findViewById(R.id.movie_title);
        releaseDate = (TextView) rootView.findViewById(R.id.release_date);
        userRating = (TextView) rootView.findViewById(R.id.user_rating);
        synopsis = (TextView) rootView.findViewById(R.id.synopsis);
        thumbnail = (ImageView) rootView.findViewById(R.id.poster_thumbnail);
        movieTrailers = (GridView) rootView.findViewById(R.id.movie_trailers);
        reviews = (LinearLayout) rootView.findViewById(R.id.reviews_container);
    }

    @Override
    public void onStart() {
        super.onStart();
        String[] queryParams = {movie.getId(), TheMovieDB.VIDEO_URL_PATH};
        new FetchMovieDetailsTask().execute(queryParams);
        queryParams = new String[]{movie.getId(), TheMovieDB.REVIEW_URL_PATH};
        new FetchMovieDetailsTask().execute(queryParams);

    }

    public class FetchMovieDetailsTask extends AsyncTask<String, Void, Movie> {

        private final String LOG_TAG = FetchMovieDetailsTask.class.getSimpleName();

        private HttpURLConnection urlConnection;
        private BufferedReader bufferedReader;

        public FetchMovieDetailsTask() {
        }

        @Override
        protected Movie doInBackground(String... params) {
            return (params.length == 0) ? null : fetchMovieData(params);
        }

        private Movie fetchMovieData(String... params) {

            urlConnection = null;
            bufferedReader = null;

            String moviesJsonString = null;

            try {
                InputStream inputStream = makeHttpRequestToTheMovieDBApi(params);
                StringBuffer buffer = NetworkUtils.readResponseFromInputStream(inputStream, bufferedReader);

                if (buffer == null) return null;

                moviesJsonString = buffer.toString();


            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                cleanup();
            }
            return getMovieDetails(moviesJsonString, params[1]);
        }

        private Movie getMovieDetails(String moviesJsonString, String type) {

            try {
                return MovieDataParser.getMovieDetailsFromJson(moviesJsonString, type);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        private InputStream makeHttpRequestToTheMovieDBApi(String... params) throws IOException {

            Uri builtUri = buildUri(params);
            URL url = new URL(builtUri.toString());
            Log.v(LOG_TAG, "Making network call: " + builtUri.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            return urlConnection.getInputStream();

        }

        private Uri buildUri(String[] params) {

            return Uri.parse(TheMovieDB.BASE_URL)
                    .buildUpon()
                    .appendPath(params[0])
                    .appendPath(params[1])
                    .appendQueryParameter(TheMovieDB.APIKEY_PARAM, BuildConfig.MOVIE_DB_API_KEY)
                    .build();
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

        @Override
        protected void onPostExecute(Movie movie) {
            super.onPostExecute(movie);
            if (movie.getVideos().size() > 0) {
                Log.d(LOG_TAG, movie.getVideos().toString());
                trailerDapter.clear();
                trailerDapter.addAll(movie.getVideos());
                trailerDapter.notifyDataSetChanged();
            }
            if (movie.getReviews().size() > 0) {
                Log.d(LOG_TAG, movie.getReviews().toString());
                //reviews_container
                TextView review = new TextView(getActivity());
                review.setText(movie.getReviews().get(0).getUrl());
                review.setLayoutParams(
                        new ActionBar.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                );

                reviews.addView(review);
            }
        }
    }
}
