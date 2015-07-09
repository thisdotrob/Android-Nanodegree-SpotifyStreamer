package com.example.android.spotifystreamer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import retrofit.RetrofitError;


/**
 * Main fragment for the app which displays a user editable text field, from which the app
 * takes a search query which it uses to retrieve and display a list of matching artists.
 */
public class MainActivityFragment extends Fragment {

    // Log tag
    private static final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    // Constant for the parcelable Artists ArrayList used in onSaveInstanceState.
    private static final String STATE_ARTISTS = "state artists";
    // Instance variable for the RecyclerView to allow access in both onCreateView method (where
    // the layout manager is set) and setAdapter method (where adapter is set)
    private RecyclerView mRecyclerView;
    // Stores the list of Artists and associated variables
    private ArrayList<ParcelableArtist> artistList;
    // Toast variable (to display message that no search results found)
    private Toast toast;
    // The RecyclerView adapter
    private ArtistsAdapter mArtistAdapter;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        // Set the layout manager for the RecyclerView
        mRecyclerView = (RecyclerView) v.findViewById(R.id.rvSearchResults);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(v.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        // If savedInstanceState is not null, get the parceled ArrayList of artists
        // and use it to set the adapter
        if (savedInstanceState != null){
            artistList = savedInstanceState.getParcelableArrayList(STATE_ARTISTS);
            setAdapter();
        }
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set a text changed listener on the EditText view.
        final EditText etSearchQuery = (EditText) getActivity().findViewById(R.id.etSearchQuery);
        etSearchQuery.addTextChangedListener(new TextWatcher() {
            // If the user has typed 2 or more characters in to the EditText view,
            // retrieve the respective search results and update the adapter.
            // Otherwise, clear the adapter if it has been initialised previously to allow for
            // the case where a user has typed more than 2 characters and then hit backspace.
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = etSearchQuery.getText().toString();
                if (query.length() > 2) {
                    SearchSpotifyTask task = new SearchSpotifyTask();
                    task.execute(query);
                } else {
                    clearAdapter();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STATE_ARTISTS, artistList);
    }

    // Clears the adapter and redraws the view
    private void clearAdapter(){
        if (mArtistAdapter != null) {
            mArtistAdapter.clear();
            mArtistAdapter.notifyDataSetChanged();
        }
    }

    // Set the adapter on the RecyclerView
    private void setAdapter() {
        mArtistAdapter = new ArtistsAdapter(artistList);
        mRecyclerView.setAdapter(mArtistAdapter);
    }

    // AsyncTask, takes in a String parameter (the artist name to search for), uses this to query
    // the Spotify web API and populates the relevant results into the ArrayList<ParcelableArtist>
    // and uses this to set the adapter on the RecyclerView. If no results are found for the query,
    // a toast message is displayed stating this.
    private class SearchSpotifyTask extends AsyncTask<String, Void, List<Artist>> {

        @Override
        protected List<Artist> doInBackground(String...query) {
            try{
                SpotifyApi api = new SpotifyApi();
                SpotifyService service = api.getService();
                ArtistsPager pager = service.searchArtists(query[0]);
                return pager.artists.items;
            } catch (RetrofitError error) {
                SpotifyError spotifyError = SpotifyError.fromRetrofitError(error);
                Log.e(LOG_TAG, spotifyError.getMessage());
                return null;
            }

        }

        @Override
        protected void onPostExecute(List<Artist> artists) {
            if(artists != null){
                // Display no results found toast message if returned results are empty.
                if(artists.size() == 0) {
                    clearAdapter();
                    // Display the toast message if there isn't already one displayed.
                    if(toast==null){
                        String message = "No matching artists found";
                        View v = getView();
                        if(v!=null){
                            toast = Toast.makeText(v.getContext(), message, Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.BOTTOM,0,20);
                            TextView tvToast = (TextView) toast.getView()
                                    .findViewById(android.R.id.message);
                            if( tvToast != null) tvToast.setGravity(Gravity.CENTER);
                            toast.show();
                        }
                    }
                }
                // Else, convert the List<Artist> into a ArrayList<ParcelableArtist>, store in the private
                // instance variable in the outer class and set the adapter on the RecyclerView.
                else {
                    // Cancel the toast on the screen if one exists
                    if(toast!=null) toast.cancel();
                    // Convert the List<Artist>
                    artistList = new ArrayList<>();
                    int imageSize = (int) getResources().getDimension(R.dimen.thumbnail_size);
                    for (Artist artist : artists) {
                        artistList.add(new ParcelableArtist(artist,imageSize));
                    }
                    setAdapter();
                }
            }
        }
    }
}
