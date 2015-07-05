package com.example.android.spotifystreamer;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import kaaes.spotify.webapi.android.models.Artist;

public class ArtistsAdapter extends RecyclerView.Adapter<ArtistsAdapter.ViewHolder> {

    private static final String LOG_TAG = ArtistsAdapter.class.getSimpleName();

    // The dataset to be displayed
    private List<Artist> artistList;

    // Constructor
    public ArtistsAdapter(List<Artist> artistList) {
        this.artistList = artistList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ArtistsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        // inflate the view
        View layoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_artist, null);
        // create ViewHolder
        ViewHolder vHolder = new ViewHolder(layoutView);
        return vHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        // Get the data item for this position
        Artist artist = artistList.get(position);
        if (artist.images.size() > 0) {
            Picasso.with(holder.ivArtist.getContext())
                    .load(artist.images.get(0).url)
                    .into(holder.ivArtist);
        }
        holder.tvArtist.setText(artist.name);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return artistList.size();
    }

    // Inner class to provide references to the views for each item in the layout
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvArtist;
        public ImageView ivArtist;
        public ViewHolder(View layoutView) {
            super(layoutView);
            tvArtist = (TextView) layoutView.findViewById(R.id.tvArtist);
            ivArtist = (ImageView) layoutView.findViewById(R.id.ivArtist);
        }
    }






}
