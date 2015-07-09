package com.example.android.spotifystreamer;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * RecyclerView adapter used to display the top 10 tracks data.
 */
public class TracksAdapter extends RecyclerView.Adapter<TracksAdapter.ViewHolder> {

    // The dataset to be displayed
    private ArrayList<ParcelableTrack> trackList;

    // Constructor
    public TracksAdapter(ArrayList<ParcelableTrack> trackList){
        this.trackList = trackList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TracksAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        // inflate the view
        View layoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_track, null);
        // create ViewHolder
        ViewHolder vHolder = new ViewHolder(layoutView);
        return vHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // Get data for this track
        ParcelableTrack track = trackList.get(position);
        // Set the track name
        holder.tvTrack.setText(track.getName());
        // Set the album name
        holder.tvAlbum.setText(track.getAlbumName());
        // Set the track thumbnail
        int imageSize = track.getImageSize();
        Picasso.with(holder.ivTrack.getContext())
                .load(track.getImageUrl())
                .resize(imageSize, imageSize)
                .centerInside()
                .into(holder.ivTrack);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return trackList.size();
    }

    // Inner class to provide references to the views for each item in the layout
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvTrack;
        public TextView tvAlbum;
        public ImageView ivTrack;

        public ViewHolder(View layoutView) {
            super(layoutView);
            tvTrack = (TextView) layoutView.findViewById(R.id.tvTrack);
            tvAlbum = (TextView) layoutView.findViewById(R.id.tvAlbum);
            ivTrack = (ImageView) layoutView.findViewById(R.id.ivTrack);
        }

    }

}
