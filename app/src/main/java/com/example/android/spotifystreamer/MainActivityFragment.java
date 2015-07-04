package com.example.android.spotifystreamer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;


/**
 * TODO: write doc
 */
public class MainActivityFragment extends Fragment {

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
        final EditText etSearchQuery = (EditText) v.findViewById(R.id.etSearchQuery);

        etSearchQuery.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = etSearchQuery.getText().toString();
                if (query.length() > 2) {
                    // Update the adapter
                    SearchSpotifyTask task = new SearchSpotifyTask();
                    task.execute(query);
                } else {
                    if(mArtistAdapter != null) {
                        mArtistAdapter.clear();
                    }
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {}
        });

        return v;
    }

    public class SearchSpotifyTask extends AsyncTask<String, Void, List<Artist>> {

        @Override
        protected List<Artist> doInBackground(String...query) {
            SpotifyApi api = new SpotifyApi();
            SpotifyService service = api.getService();
            ArtistsPager pager = service.searchArtists(query[0]);
            return pager.artists.items;
        }

        @Override
        protected void onPostExecute(List<Artist> artistList) {

            // initialize the adapter
            mArtistAdapter = new ArtistsAdapter(getActivity(),artistList);

            // Attach the adapter to the ListView
            ListView listView = (ListView) getView().findViewById(R.id.lvSearchResults);
            listView.setAdapter(mArtistAdapter);

        }
    }


}
