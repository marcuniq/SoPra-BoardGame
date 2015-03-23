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
    private boolean callbackHasFinished = false;
    private ArrayList<String> gamesList = new ArrayList<>(); // later needs to be Game rather than User

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
        RestService.getInstance(getActivity()).getUsers(new Callback<List<User>>() {
            @Override
            public void success(List<User> games, Response response) {
                for ( User game : games ) {
                    gamesList.add(game.username());
                    Log.v("added User: ", game.username());
                }
                callbackHasFinished = true;
            }
            @Override
            public void failure(RetrofitError error) {
                tvLogBox.setText("ERROR: " + error.getMessage());
            }
        });

        /* This does not work since somehow the callback is executed AFTER onCreateView ha finished
         * so, the arrayadapter is never set (but it works perfectly (if the callback would have
         * been executed BEFORE ..*/
        if (callbackHasFinished) {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(inflater.getContext(),
                    R.layout.fragment_games_list,
                    R.id.games_list_item_label,
                    gamesList);
            setListAdapter(arrayAdapter);
        } else {
            Log.v("Justen: ", "we have a problem");
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /* Implement some behaviour when clicking on an item
    * in future: should open some detailed view of a game
    * including: the users that have already joined that game and a "join game" button
    * where the user can join that specific game if he wants to */
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        /* For now just display what item has been selected */
        String item = (String) getListAdapter().getItem(position);
        Toast.makeText(v.getContext(), item + " selected", Toast.LENGTH_LONG).show();
    }
}


