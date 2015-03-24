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
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.Game;
import ch.uzh.ifi.seal.soprafs15.group_09_android.service.RestService;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class GamesListFragment extends ListFragment {

    private TextView tvLogBox;
    private ArrayAdapter<String> arrayAdapter; // adapts the ArrayList of Games to the ListView

    /* empty constructor */
    public GamesListFragment() {}

    /**
     * Called after User has successfully logged in.
     * @return A new instance of fragment GamesListFragment.
     */
    public static GamesListFragment newInstance() {
        return new GamesListFragment();
    }

    /**
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Creates a new view, instantiates a new ArrayAdapter that 'links' the ArrayList<String>
     * to the ListView which is then displayed.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        arrayAdapter = new ArrayAdapter<>(inflater.getContext(),
                R.layout.fragment_games_list,
                R.id.games_list_item_label,
                new ArrayList<String>());
        setListAdapter(arrayAdapter);

        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        onResume();

        return rootView;
    }

    /**
     * Creates a Callback to get the list of all current available games. Adds all games to the
     * adapter, respectively to the list which is later automatically displayed in the view.
     */
    @Override
    public void onResume(){
        super.onResume();
        RestService.getInstance(getActivity()).getGames(new Callback<List<Game>>() {
            @Override
            public void success(List<Game> games, Response response) {
                for (Game game : games) {
                    arrayAdapter.add(game.game());
                }
            }
            @Override
            public void failure(RetrofitError error) {
                tvLogBox.setText("ERROR: " + error.getMessage());
            }
        });
    }

    /**
     * Stop  Fragment
     */
    @Override
    public void onStop() {
        Log.w("TAG", "App stopped");

        super.onStop();
    }

    /**
     * Destroy Fragment
     */
    @Override
    public void onDestroy() {
        Log.w("TAG", "App destoryed");

        super.onDestroy();
    }

    /**
     * Implements some behaviour when clicking on an item.
     *
     * @param l         The list view.
     * @param v         The current view.
     * @param position  Current position of the item in the view.
     * @param id        Id of the item from the list.
     */
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


