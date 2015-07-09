package com.example.android.spotifystreamer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * RecyclerView adapter used to display the artists search results.
 */
public class ArtistsAdapter extends RecyclerView.Adapter<ArtistsAdapter.ViewHolder> {

    // The dataset to be displayed
    private ArrayList<ParcelableArtist> artistList;
    // Constructor
    public ArtistsAdapter(ArrayList<ParcelableArtist> artistList) {
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
        return new ViewHolder(layoutView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // Get the data for this artist
        final ParcelableArtist artist = artistList.get(position);
        // Set the artist name
        final String name = artist.getName();
        holder.tvArtist.setText(name);
        // Set the artist thumbnail
        int imageSize = artist.getImageSize();
        Picasso.with(holder.ivArtist.getContext())
                .load(artist.getImageUrl())
                .resize(imageSize, imageSize)
                .centerInside()
                .into(holder.ivArtist);
        // Handle clicks on an artist in the results list, which starts the Top 10 activity
        holder.setClickListener(new ViewHolder.ClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(v.getContext(),TopTenActivity.class);
                Bundle extrasBundle = new Bundle();
                extrasBundle.putString("EXTRA_ARTIST_NAME",name);
                extrasBundle.putString("EXTRA_ARTIST_ID",artist.getId());
                intent.putExtras(extrasBundle);
                v.getContext().startActivity(intent);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return artistList.size();
    }

    // Method to clear the adapter / artist search results.
    public void clear() {
        artistList.clear();
    }

    // Inner class to provide references to the views for each item in the layout
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvArtist;
        public ImageView ivArtist;
        private ClickListener clickListener;

        public ViewHolder(View layoutView) {
            super(layoutView);
            tvArtist = (TextView) layoutView.findViewById(R.id.tvArtist);
            ivArtist = (ImageView) layoutView.findViewById(R.id.ivArtist);
            layoutView.setOnClickListener(this);
        }

        public interface ClickListener {
            // Called when view is clicked. v is the view that is clicked, position is the position of
            // the clicked item.
            void onClick(View v, int position);
        }

        // Setter method for the listener
        public void setClickListener(ClickListener clickListener) {
            this.clickListener = clickListener;
        }

        @Override
        public void onClick(View v) {
            clickListener.onClick(v, getPosition());
        }

    }
}
