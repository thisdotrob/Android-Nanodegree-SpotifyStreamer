package com.example.android.spotifystreamer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;


/**
 * Detailed fragment which displays a user selected artist's top ten tracks in a list.
 */
public class TopTenActivityFragment extends Fragment {

    // Instance variables for the RecyclerView and its adapter.
    // Required to allow access in both onCreateView method, and in SearchSpotifyTask inner class.
    private RecyclerView mRecyclerView;
    private TracksAdapter mTracksAdapter;

    // Constant for the country code used when requesting the top 10 tracks from the Spotify
    // web API.
    private final String SEARCH_COUNTRY = "GB";

    // Private variable for the artist's ID number, to allow its use in both the onCreate and
    // onCreateView methods.
    private String artistId;

    public TopTenActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve the artist's name from the intent's bundle, and use it to set the
        // action bar subtitle.
        Bundle bundle = getActivity().getIntent().getExtras();
        artistId = bundle.getString("EXTRA_ARTIST_ID");
        String artistName = bundle.getString("EXTRA_ARTIST_NAME");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle(artistName);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_top_ten, container, false);

        // Set the layout manager for the RecyclerView.
        mRecyclerView = (RecyclerView) v.findViewById(R.id.rvTopTenResults);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(v.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Retrieve the top 10 tracks for the artist and update the adapter.
        SearchSpotifyTask task = new SearchSpotifyTask();
        task.execute(artistId);

        return v;
    }

    // AsyncTask, takes in a String parameter (the artist name to search for), uses this to query
    // the Spotify web API and populates the artist's top 10 tracks in the RecyclerView adapter.
    private class SearchSpotifyTask extends AsyncTask<String, Void, List<Track>> {

        @Override
        protected List<Track> doInBackground(String...query) {
            // Get top 10 tracks from Spotify web api
            SpotifyApi api = new SpotifyApi();
            SpotifyService service = api.getService();
            Map<String, Object> options = new HashMap<>();
            options.put("country", SEARCH_COUNTRY);
            return service.getArtistTopTrack(query[0], options).tracks;
        }

        @Override
        protected void onPostExecute(List<Track> tracks) {
            View v = getView();
            // Set the adapter on the RecyclerView with the top 10 tracks data
            mTracksAdapter = new TracksAdapter(tracks);
            mRecyclerView.setAdapter(mTracksAdapter);

        }
    }
}
