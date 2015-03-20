package ch.uzh.ifi.seal.soprafs15.group_09_android.fragments;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v4.app.LoaderManager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import ch.uzh.ifi.seal.soprafs15.group_09_android.R;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.RestUri;
import ch.uzh.ifi.seal.soprafs15.group_09_android.service.RestService;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public abstract class GamesListFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>,
        AdapterView.OnItemClickListener {

    private Button btnJoin;
    private TextView tvLogBox;

    private LoginFragment.OnFragmentInteractionListener mListener;

    /* Defines an array that contains resource ids for the layout views
     * that get the Cursor column contents. The id is pre-defined in
     * the Android framework, so it is prefaced with "android.R.id" */
    private final static int[] TO_IDS = {
            android.R.id.text1
    };

    // Define global mutable variables
    // Define a ListView object
    ListView mGamesList;

    // Define variables for the game the user selects
    long mGameId;       // The game's _ID value
    String mGameName;   // The game's name
    Uri mGameUri;       // A content URI for the selected game

    // An adapter that binds the result Cursor to the ListView
    private SimpleCursorAdapter mCursorAdapter;

    // Empty public constructor, required by the system
    public GamesListFragment() {}

    // A UI Fragment must inflate its View
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the fragment layout
        return inflater.inflate(R.layout.fragment_games_list, container, false);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Gets the ListView from the View list of the parent activity
        mGamesList = (ListView) getActivity().findViewById(R.layout.fragment_games_list);
        // Gets a CursorAdapter
        mCursorAdapter = new SimpleCursorAdapter(
                                            getActivity(),
                                            R.layout.games_list_item,
                                            null,
                                            null, TO_IDS,
                                            0);
        // Sets the adapter for the ListView
        mGamesList.setAdapter(mCursorAdapter);
    }

    private void onClickJoinBtn(View v) {
        RestService.getInstance(getActivity()).joinGame(user, new Callback<RestUri>() {

            @Override
            public void success(RestUri restUri, Response response) {
                tvLogBox.setText("SUCCESS: User joined game: " + restUri.uri());
            }

            @Override
            public void failure(RetrofitError error) {
                tvLogBox.setText("ERROR: " + error.getMessage());
            }
        });

    }
}
