package it.kjaervik.popkorn;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import it.kjaervik.popkorn.model.Movie;
import it.kjaervik.popkorn.util.MovieDataParser;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment {

    private TextView title, releaseDate, userRating, synopsis;
    private ImageView thumbnail;

    public DetailFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Movie movie = getActivity()
                .getIntent()
                .getParcelableExtra("movie");

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        getUiElements(rootView);
        populateUiElementsFromMovie(movie);

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
        title = (TextView)rootView.findViewById(R.id.movie_title);
        releaseDate = (TextView)rootView.findViewById(R.id.release_date);
        userRating = (TextView)rootView.findViewById(R.id.user_rating);
        synopsis = (TextView)rootView.findViewById(R.id.synopsis);
        thumbnail = (ImageView)rootView.findViewById(R.id.poster_thumbnail);
    }
}
