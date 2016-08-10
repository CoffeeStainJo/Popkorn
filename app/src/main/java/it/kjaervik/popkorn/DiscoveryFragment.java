package it.kjaervik.popkorn;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import it.kjaervik.popkorn.model.Movie;
import it.kjaervik.popkorn.network.FetchMovieTask;
import it.kjaervik.popkorn.util.MovieAdapter;

/**
 * A placeholder fragment containing a simple view.
 */
public class DiscoveryFragment extends Fragment {

    private static final String LOG_TAG = DiscoveryFragment.class.getSimpleName();
    private MovieAdapter movieAdapter;
    private List<Movie> movies = new ArrayList<>();

    public DiscoveryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Construct the data source
        movieAdapter = new MovieAdapter(getActivity(), new ArrayList<Movie>());

        GridView gridview = (GridView) rootView.findViewById(R.id.movie_grid);

        gridview.setAdapter(movieAdapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // selected item
                Movie movie = movieAdapter.getItem(position);

                Intent intent = new Intent(getActivity().getApplicationContext(), DetailActivity.class);
                intent.putExtra("movie", movie);
                startActivity(intent);
            }
        });
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.containsKey("movies"))
            movies = savedInstanceState.getParcelableArrayList("movies");
    }

    @Override
    public void onStart() {
        super.onStart();

        String sortOrderPreference = getSortOrderPreference();

        if (movies.isEmpty()) {
            FetchMovieTask task = new FetchMovieTask(movieAdapter);
            if (task.isOnline(getActivity()))
                task.execute(sortOrderPreference);
            else
                Log.d(LOG_TAG, "No Internet Connectivity");
        } else {
            movieAdapter.clear();
            movieAdapter.addAll(movies);
            movieAdapter.notifyDataSetChanged();
        }
    }

    private String getSortOrderPreference() {
        return PreferenceManager.getDefaultSharedPreferences(getActivity())
                .getString(
                        getString(R.string.pref_sort_order),
                        getString(R.string.pref_sort_order_default)
                );
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("movies", (ArrayList<? extends Parcelable>) movieAdapter.getMovies());
        super.onSaveInstanceState(outState);
    }
}