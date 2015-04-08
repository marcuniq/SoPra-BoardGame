package ch.uzh.ifi.seal.soprafs15.group_09_android.fragments;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import ch.uzh.ifi.seal.soprafs15.group_09_android.R;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.Game;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.User;
import ch.uzh.ifi.seal.soprafs15.group_09_android.service.RestService;
import ch.uzh.ifi.seal.soprafs15.group_09_android.utils.UserArrayAdapter;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.util.ArrayList;
import java.util.List;

public class GameLobbyFragment extends ListFragment {

    private TextView tvLogBox;
    private UserArrayAdapter arrayAdapter; // adapts the ArrayList of Games to the ListView
    private User user;
    private Game game;

    /* empty constructor */
    public GameLobbyFragment() {}

    /**
     * Called after User has successfully logged in.
     * @return A new instance of fragment GamesListFragment.
     */
    public static GameLobbyFragment newInstance(User user, Game game) {
        GameLobbyFragment fragment = new GameLobbyFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("user", user);
        bundle.putParcelable("game", game);
        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = getArguments().getParcelable("user");
        game = getArguments().getParcelable("game");
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
        arrayAdapter = new UserArrayAdapter(getActivity(),
/*                R.layout.fragment_game_lobby,
                R.id.game_lobby_item_label,*/
                new ArrayList<User>());
        setListAdapter(arrayAdapter);

        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        return rootView;
    }

    /**
     * Creates a Callback to get the list of all current available games. Adds all games to the
     * adapter, respectively to the list which is later automatically displayed in the view.
     */
    @Override
    public void onResume(){
        super.onResume();

        Long gameId = this.getArguments().getLong("gameId");

        RestService.getInstance(getActivity()).getPlayers(gameId, new Callback<List<User>>() {
            @Override
            public void success(List<User> players, Response response) {
                for (User player : players) {
                    arrayAdapter.add(player.username());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                tvLogBox.setText("ERROR: " + error.getMessage());
            }
        });
    }

    /**
     * Implements some behaviour when clicking on an item.
     *
     * @param l         The list view.
     * @param v         The current view.
     * @param position  Current position of the item in the view.
     * @param id        Id of the item from the list.
     */
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        /* For now just display what item has been selected */
        String item = (String) getListAdapter().getItem(position);
        Toast.makeText(v.getContext(), "You joined the game \"" + item + "\"", Toast.LENGTH_LONG).show();
    }
}


