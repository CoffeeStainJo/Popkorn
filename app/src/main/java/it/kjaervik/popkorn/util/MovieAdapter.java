package it.kjaervik.popkorn.util;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import it.kjaervik.popkorn.R;
import it.kjaervik.popkorn.model.Movie;

public class MovieAdapter extends ArrayAdapter<Movie> {

    public MovieAdapter(Activity context, List<Movie> movies) {
        super(context, 0, movies);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Movie movie = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.grid_item_movie, parent, false);
            // Lookup view for data population & populate the data into the template view using the data object
            // Populate the data into the template view using the data object
            viewHolder.poster = (ImageView)convertView.findViewById(R.id.grid_item_poster);
            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Picasso
                .with(getContext())
                .load(MovieDataParser.resolvePosterPathFromStringPath(movie.getPosterImageUri()))
                .into(viewHolder.poster);

        return convertView;
    }

    // View lookup cache
    private static class ViewHolder {
        ImageView poster;
    }
}
