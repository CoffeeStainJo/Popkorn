package it.kjaervik.popkorn;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import it.kjaervik.popkorn.util.ImageAdapter;

/**
 * A placeholder fragment containing a simple view.
 */
public class DiscoveryFragment extends Fragment {

    private static final String LOG_TAG = DiscoveryFragment.class.getSimpleName();

    public DiscoveryFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        Log.d(LOG_TAG, "onCreateView()");
        GridView gridview = (GridView) rootView.findViewById(R.id.movie_grid);
        gridview.setAdapter(new ImageAdapter(getActivity()));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "" + position, Toast.LENGTH_LONG).show();
            }
        });
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
