package com.example.android.spotifystreamer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import kaaes.spotify.webapi.android.models.Artist;

public class ArtistsAdapter extends ArrayAdapter<Artist> {

    private static final String LOG_TAG = ArtistsAdapter.class.getSimpleName();

    public ArtistsAdapter(Context context, List<Artist> artistList) {
        super(context, 0, artistList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        Artist artist = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater
                    .from(getContext()).inflate(R.layout.item_artist, parent, false);
        }

        // Lookup view for data population
        TextView tvArtist = (TextView) convertView.findViewById(R.id.tvArtist);
        ImageView ivArtist = (ImageView) convertView.findViewById(R.id.ivArtist);

        // Populate the image into the template view using the data object
        if (artist.images.size() > 0) {
            Picasso.with(getContext())
                    .load(artist.images.get(0).url)
                    .into(ivArtist);
        }

        // Populate the artist name into the template view using the data object
        tvArtist.setText(artist.name);



        // Return the completed view
        return convertView;
    }

}
