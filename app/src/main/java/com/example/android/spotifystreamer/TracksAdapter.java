package com.example.android.spotifystreamer;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;
import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Track;


/**
 * RecyclerView adapter used to display the top 10 tracks data.
 */
public class TracksAdapter extends RecyclerView.Adapter<TracksAdapter.ViewHolder> {

    // Image width & height in pixels
    private static final int IMAGE_SIZE = 200;

    // The dataset to be displayed
    private List<Track> tracks;

    // Constructor
    public TracksAdapter(List<Track> tracks){
        this.tracks = tracks;
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
        Track track = tracks.get(position);

        // Set the track name
        holder.tvTrack.setText(track.name);

        // Set the album name
        holder.tvAlbum.setText(track.album.name);

        // Set the track thumbnail
        List<Image> imageList = track.album.images;
        int numImages = imageList.size();
        if (numImages > 0) {
            Image bestImage = getBestImage(imageList, numImages);
            Picasso.with(holder.ivTrack.getContext())
                    .load(bestImage.url)
                    .resize(200, 200)
                    .centerInside()
                    .into(holder.ivTrack);
        }
    }

    // Method to return the image that most closely matches the desired size (according to
    // IMAGE_SIZE constant.
    private Image getBestImage(List<Image> imageList, int numImages){

        // Initially set returned image to first in the list
        Image bestImage = imageList.get(0);

        // If the list contains more than one image, see if any of them are closer in size
        // to the desired IMAGE_SIZE. Whichever is closest gets set as the returned image.
        if(numImages > 1) {
            int varToReqSize = Math.abs(IMAGE_SIZE - bestImage.width);
            for(int i = 1; i < numImages; i++){
                Image image = imageList.get(i);
                int newVarToReqSize = Math.abs(IMAGE_SIZE - image.width);
                if (newVarToReqSize < varToReqSize) {
                    varToReqSize = newVarToReqSize;
                    bestImage = image;
                }
            }
        }

        return bestImage;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return tracks.size();
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
