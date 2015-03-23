package ch.uzh.ifi.seal.soprafs15.group_09_android.fragments;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import ch.uzh.ifi.seal.soprafs15.group_09_android.R;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.User;
import ch.uzh.ifi.seal.soprafs15.group_09_android.service.RestService;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class GamesListFragment extends ListFragment {

    private TextView tvLogBox;
    private LayoutInflater theInflater; // later used for callback
    private ArrayList<String> gamesList = new ArrayList<>(); // TODO: later needs to be Game rather than User

    /* empty constructor */
    public GamesListFragment() {
    }

    public static GamesListFragment newInstance() {
        return new GamesListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        theInflater = inflater; // this is some hack to get the inflater accessed inside the callback
        RestService.getInstance(getActivity()).getUsers(new Callback<List<User>>() {
            @Override
            public void success(List<User> games, Response response) {

                // TODO: needs to be Game Object rather than User in later phase
                for ( User game : games ) {
                    gamesList.add(game.username());
                }

                /* Create a list view with the data from above using the fragment_games_list
                 * as template and the gamesList ArrayList to fill it. */
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(theInflater.getContext(),
                        R.layout.fragment_games_list,
                        R.id.games_list_item_label,
                        gamesList);
                setListAdapter(arrayAdapter);
            }
            @Override
            public void failure(RetrofitError error) {
                tvLogBox.setText("ERROR: " + error.getMessage());
            }
        });
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /* TODO: Implement some behaviour when clicking on an item:
    *        in future: should open some detailed view of a game
    *        including: the users that have already joined that game and a "join game" button
    *        where the user can join that specific game if he wants to */
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        /* For now just display what item has been selected */
        String item = (String) getListAdapter().getItem(position);
        Toast.makeText(v.getContext(), item + " selected", Toast.LENGTH_LONG).show();
    }
}


