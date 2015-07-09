package com.example.android.spotifystreamer;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Image;

/**
 * Parcelable version of Artist
 */
public class ParcelableArtist implements Parcelable {

    private String name;
    private String imageUrl;
    private int imageSize;
    private String id;

    // Constructor with a Spotify API wrapper Artist object for parameter
    public ParcelableArtist(Artist artist, int imageSize){
        this.name = artist.name;
        this.imageSize = imageSize;
        imageUrl = getBestImageUrl(artist.images);
        this.id = artist.id;
    }

    // Constructor from a parcel
    public ParcelableArtist(Parcel source) {
        name = source.readString();
        imageUrl = source.readString();
        imageSize = source.readInt();
        id = source.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(imageUrl);
        dest.writeInt(imageSize);
        dest.writeString(id);
    }

    // Getters
    public String getName() {
        return name;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public int getImageSize() {
        return imageSize;
    }
    public String getId() {
        return id;
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

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<ParcelableArtist> CREATOR
            = new Parcelable.Creator<ParcelableArtist>(){

        @Override
        public ParcelableArtist createFromParcel(Parcel source) {
            return new ParcelableArtist(source);
        }

        @Override
        public ParcelableArtist[] newArray(int size) {
            return new ParcelableArtist[size];
        }
    };
}
