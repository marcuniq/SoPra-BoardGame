package ch.uzh.ifi.seal.soprafs15.group_09_android.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ch.uzh.ifi.seal.soprafs15.group_09_android.R;
import ch.uzh.ifi.seal.soprafs15.group_09_android.activities.MenuActivity;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.Game;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.User;
import ch.uzh.ifi.seal.soprafs15.group_09_android.service.RestService;
import ch.uzh.ifi.seal.soprafs15.group_09_android.utils.GameArrayAdapter;
import ch.uzh.ifi.seal.soprafs15.group_09_android.utils.PlayerArrayAdapter;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

//public class GameLobbyFragment extends ListFragment {
public class GameLobbyFragment extends ListFragment {

    private TextView tvLogBox;
    private Long gameId;
    private Long playerId;
    private Button startGameButton;
    private Boolean isOwner;
    private ArrayAdapter playerArrayAdapter; // adapts the ArrayList of Games to the ListView
    private View viewContainer;

    /* empty constructor */
    public GameLobbyFragment() {}

    /**
     * Called after User has successfully logged in.
     * @return A new instance of fragment GamesListFragment.
     */
    public static GameLobbyFragment newInstance() {
        return new GameLobbyFragment();
    }

    /**
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameId = this.getArguments().getLong("gameId");
        playerId = this.getArguments().getLong("playerId");
        isOwner = this.getArguments().getBoolean("isOwner");
    }

    /**
     * Creates a new view, instantiates a new ArrayAdapter that 'links' the ArrayList<String>
     * to the ListView which is then displayed.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_lobby, container, false);

        startGameButton = (Button) v.findViewById(R.id.startButton);

        // Hide button if user is not the owner
        if (!isOwner) {
            startGameButton.setVisibility(View.INVISIBLE);
        }
        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickStartGameButton(v);
            }
        });

        playerArrayAdapter = new ArrayAdapter<>(
                getActivity(),
                R.layout.player_item,
                R.id.player_list_item,
                new ArrayList<String>());
        setListAdapter(playerArrayAdapter);

        return v;
    }

    @Override
    public void onResume(){
        super.onResume();
        RestService.getInstance(getActivity()).getPlayers(gameId, new Callback<List<User>>() {
            @Override
            public void success(List<User> players, Response response) {
                for (User player : players) {
                    playerArrayAdapter.add(player.username());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                tvLogBox.setText("ERROR: " + error.getMessage());
            }
        });
    }

    private void onClickStartGameButton(View v) {
        // TODO: start game
        Toast.makeText(v.getContext(), "You (" + playerId + ") started game \"" + gameId + "\"", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        //User selectedPlayer = (User) getListAdapter().getItem(position);
        //Toast.makeText(v.getContext(), "You joined the game \"" + selectedPlayer.username() + "\" with the id (" + selectedPlayer.id() + ")", Toast.LENGTH_LONG).show();
    }
}


