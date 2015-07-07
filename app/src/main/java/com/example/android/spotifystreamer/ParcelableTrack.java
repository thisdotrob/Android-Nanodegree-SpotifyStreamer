package com.example.android.spotifystreamer;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.List;

import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Track;

/**
 * Parcelable version of Track
 */
public class ParcelableTrack implements Parcelable {

    private static final String LOG_TAG = ParcelableTrack.class.getSimpleName();

    private String name;
    private String albumName;
    private String imageURL;
    private int imageSize;

    // Constructor with a Spotify API wrapper Track parameter
    public ParcelableTrack(Track track, int imageSize){
        name = track.name;
        albumName = track.album.name;
        imageURL = getBestImageUrl(track.album.images);
        this.imageSize = imageSize;
    }

    // Constructor from a parcel
    public ParcelableTrack(Parcel source) {
        name = source.readString();
        albumName = source.readString();
        imageURL = source.readString();
        imageSize = source.readInt();
    }

    @Override
    public int describeContents() {
        Log.d(LOG_TAG,"describing contents");
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Log.d(LOG_TAG,"flattening to parcel");
        dest.writeString(name);
        dest.writeString(albumName);
        dest.writeString(imageURL);
        dest.writeInt(imageSize);
    }

    // Getters
    public String getName() {
        return name;
    }
    public String getAlbumName() {
        return albumName;
    }
    public String getImageURL() {
        return imageURL;
    }
    public int getImageSize() {
        return imageSize;
    }

    // Returns the url for the image closest in dimension to the desired dimension set
    // by the IMAGE_SIZE constant.
    private String getBestImageUrl(List<Image> images) {
        // Return null if the list is empty
        if(images.size()==0){
            return null;
        }
        // Initially set returned image to first in the list
        Image bestImage = images.get(0);
        // If the list contains more than one image, see if any of them are closer in size
        // to the desired IMAGE_SIZE. Whichever is closest gets set as the bestImage.
        if(images.size() > 1) {
            int varToReqSize = Math.abs(imageSize - bestImage.width);
            for(int i = 1; i < images.size(); i++){
                Image image = images.get(i);
                int newVarToReqSize = Math.abs(imageSize - image.width);
                if (newVarToReqSize < varToReqSize) {
                    varToReqSize = newVarToReqSize;
                    bestImage = image;
                }
            }
        }
        // Return the url String of the best image
        return bestImage.url;
    }

    public static final Parcelable.Creator<ParcelableTrack> CREATOR
            = new Parcelable.Creator<ParcelableTrack>() {

        @Override
        public ParcelableTrack createFromParcel(Parcel source) {
            Log.d(LOG_TAG,"creating from parcel");
            return new ParcelableTrack(source);
        }

        @Override
        public ParcelableTrack[] newArray(int size) {
            return new ParcelableTrack[size];
        }
    };
}
