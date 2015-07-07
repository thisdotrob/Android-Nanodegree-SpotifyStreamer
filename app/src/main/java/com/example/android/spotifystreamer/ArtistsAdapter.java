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

import java.util.List;

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Image;

/**
 * RecyclerView adapter used to display the artists search results.
 */
public class ArtistsAdapter extends RecyclerView.Adapter<ArtistsAdapter.ViewHolder> {

    // Image width & height in pixels
    private static final int IMAGE_SIZE = 200;

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
    public void onBindViewHolder(final ViewHolder holder, int position) {

        // Get the data for this artist
        final Artist artist = artistList.get(position);

        // Set the artist name
        holder.tvArtist.setText(artist.name);

        // Set the artist thumbnail
        List<Image> imageList = artist.images;
        int numImages = imageList.size();
        if (numImages > 0) {
            Image bestImage = getBestImage(imageList, numImages);
            Picasso.with(holder.ivArtist.getContext())
                    .load(bestImage.url)
                    .resize(200, 200)
                    .centerInside()
                    .into(holder.ivArtist);
        }

        // Handle clicks on an artist in the results list, which starts the Top 10 activity
        holder.setClickListener(new ViewHolder.ClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(v.getContext(),TopTenActivity.class);
                Bundle extrasBundle = new Bundle();
                extrasBundle.putString("EXTRA_ARTIST_NAME",artist.name.toString());
                extrasBundle.putString("EXTRA_ARTIST_ID",artist.id);
                intent.putExtras(extrasBundle);
                v.getContext().startActivity(intent);
            }
        });
    }

    // Method to return the image that most closely matches the desired size (according to
    // IMAGE_SIZE constant.
    private Image getBestImage(List<Image> imageList, int numImages){

        // Initially set returned image to first in the list.
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
        return artistList.size();
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
            public void onClick(View v, int position);
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

    // Method to clear the adapter / artist search results.
    public void clear() {
        artistList.clear();
    }

}
