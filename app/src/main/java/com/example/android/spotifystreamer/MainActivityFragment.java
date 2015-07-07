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

import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;


/**
 * Main fragment for the app which displays a user editable text field, from which the app
 * takes a search query which it uses to retrieve and display a list of matching artists.
 */
public class MainActivityFragment extends Fragment {

    private static final String LOG_TAG = MainActivityFragment.class.getSimpleName();

    // Instance variables for the RecyclerView and its adapter.
    // Required to allow access in both onCreateView method, and in SearchSpotifyTask inner class.
    private RecyclerView mRecyclerView;
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

        final EditText etSearchQuery = (EditText) v.findViewById(R.id.etSearchQuery);

        // Set a text changed listener on the EditText view.
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
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {}
        });

        return v;
    }

    // Clears the adapter and redraws the view
    private void clearAdapter(){
        if (mArtistAdapter != null) {
            mArtistAdapter.clear();
            mArtistAdapter.notifyDataSetChanged();
        }
    }

    // AsyncTask, takes in a String parameter (the artist name to search for), uses this to query
    // the Spotify web API and populates the results in the RecyclerView adapter.
    // If no results are found for the query, a toast message is displayed stating this.
    private class SearchSpotifyTask extends AsyncTask<String, Void, List<Artist>> {

        Toast toast;

        @Override
        protected List<Artist> doInBackground(String...query) {
            Log.d(LOG_TAG, "getting data from spotify");
            SpotifyApi api = new SpotifyApi();
            SpotifyService service = api.getService();
            ArtistsPager pager = service.searchArtists(query[0]);
            return pager.artists.items;
        }

        @Override
        protected void onPostExecute(List<Artist> artistList) {
            View v = getView();

            //this will cancel the toast on the screen if one exists
            if(toast!=null)   {
                toast.cancel();
            }

            if(artistList.size() == 0) {
                // Display no results found toast message
                clearAdapter();
                String message = "No matching artists found";
                if(toast==null){
                    toast = Toast.makeText(v.getContext(), message, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM,0,20);
                    TextView tvToast = (TextView) toast.getView().findViewById(android.R.id.message);
                    if( tvToast != null) tvToast.setGravity(Gravity.CENTER);
                }
                toast.show();
            } else {
                // Set the adapter on the RecyclerView
                mArtistAdapter = new ArtistsAdapter(artistList);
                mRecyclerView.setAdapter(mArtistAdapter);
            }
        }

    }

}
