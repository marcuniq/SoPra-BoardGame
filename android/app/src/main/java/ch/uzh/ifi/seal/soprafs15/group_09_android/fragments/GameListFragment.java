package ch.uzh.ifi.seal.soprafs15.group_09_android.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ch.uzh.ifi.seal.soprafs15.group_09_android.R;
import ch.uzh.ifi.seal.soprafs15.group_09_android.activities.MenuActivity;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.beans.UserBean;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.beans.GameBean;
import ch.uzh.ifi.seal.soprafs15.group_09_android.service.PusherService;
import ch.uzh.ifi.seal.soprafs15.group_09_android.service.RestService;
import ch.uzh.ifi.seal.soprafs15.group_09_android.utils.GameArrayAdapter;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class GameListFragment extends ListFragment {

    private TextView tvLogBox;
    private GameArrayAdapter gameArrayAdapter; // adapts the ArrayList of Games to the ListView
    private String token;
    private Long joinedGameId;
    private Long userId;

    /* empty constructor */
    public GameListFragment() {}

    /**
     * Called after UserBean has successfully logged in.
     * @return A new instance of fragment GamesListFragment.
     */
    public static GameListFragment newInstance() {
        return new GameListFragment();
    }

    /**
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userId = this.getArguments().getLong("userId");

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        token = sharedPref.getString("token", token);
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
        View v = inflater.inflate(R.layout.fragment_game_list, container, false);

        gameArrayAdapter = new GameArrayAdapter(
                getActivity(),
                R.layout.game_item,
                R.id.game_item_text,
                R.id.game_item_icon,
                new ArrayList<GameBean>());
        setListAdapter(gameArrayAdapter);

        return v;
    }

    /**
     * Creates a Callback to get the list of all current available games. Adds all games to the
     * adapter, respectively to the list which is later automatically displayed in the view.
     */
    @Override
    public void onResume(){
        super.onResume();
        RestService.getInstance(getActivity()).getGames(new Callback<List<GameBean>>() {
            @Override
            public void success(List<GameBean> games, Response response) {
                for (GameBean game : games) {
                    gameArrayAdapter.add(game);
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
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        token = sharedPref.getString("token", token);

        GameBean selectedGame = (GameBean) getListAdapter().getItem(position);
/*
        Toast.makeText(v.getContext(), "You joined the game \"" + selectedGame.name() + "\" with the id (" + selectedGame.id() + ")", Toast.LENGTH_LONG).show();

*/
        final Long gameId = selectedGame.id();
        joinedGameId = gameId;
        UserBean player = UserBean.setToken(token);

        RestService.getInstance(getActivity()).joinGame(gameId, player, new Callback<UserBean>() {

            @Override
            public void success(UserBean user, Response response) {

                PusherService.getInstance(getActivity()).register(gameId, user.channelName());

                Fragment gameLobbyFragment = GameLobbyFragment.newInstance();
                Bundle bundle = new Bundle();
                bundle.putLong("gameId", joinedGameId);
                bundle.putLong("userId", userId);
                bundle.putBoolean("isOwner", false);
                gameLobbyFragment.setArguments(bundle);

                /* See all already created games (testing) */
                ((MenuActivity) getActivity()).pushFragment(gameLobbyFragment);
            }

            @Override
            public void failure(RetrofitError error) {
                tvLogBox.setText("ERROR: " + error.getMessage());
            }
        });
    }

}


