package it.kjaervik.popkorn.util;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import it.kjaervik.popkorn.R;
import it.kjaervik.popkorn.model.Trailer;


public class TrailerAdapter extends ArrayAdapter<Trailer> {

    private final Context context;

    public TrailerAdapter(Activity context, List<Trailer> trailers) {
        super(context, 0, trailers);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Trailer trailer = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.movie_trailer_item, parent, false);
            // Lookup view for data population & populate the data into the template view using the data object
            // Populate the data into the template view using the data object
            viewHolder.thumbnail = (ImageView)convertView.findViewById(R.id.movie_trailer_item_thumbnail);
            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Picasso
                .with(context)
                .load(MovieDataParser.resolveYouTubeThumbnailFromUri(trailer.getKey()))
                .into(viewHolder.thumbnail);

        return convertView;
    }

    // View lookup cache
    private static class ViewHolder {
        ImageView thumbnail;
    }


}
