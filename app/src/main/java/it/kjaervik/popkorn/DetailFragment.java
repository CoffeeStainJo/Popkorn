package it.kjaervik.popkorn;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import it.kjaervik.popkorn.model.Movie;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment {

    public DetailFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Intent intent = getActivity().getIntent();
        Movie movie = intent.getParcelableExtra("movie");
        Toast.makeText(getActivity(), "The movie is: " + movie.toString(), Toast.LENGTH_SHORT).show();

        return inflater.inflate(R.layout.fragment_detail, container, false);
    }
}
