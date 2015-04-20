package ch.uzh.ifi.seal.soprafs15.group_09_android.fragments;

import android.content.Context;
import android.content.Intent;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pusher.client.channel.SubscriptionEventListener;

import java.util.ArrayList;
import java.util.List;

import ch.uzh.ifi.seal.soprafs15.group_09_android.R;
import ch.uzh.ifi.seal.soprafs15.group_09_android.activities.GameActivity;
import ch.uzh.ifi.seal.soprafs15.group_09_android.activities.MenuActivity;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.Game;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.User;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.events.MoveEvent;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.gson.AutoValueAdapterFactory;
import ch.uzh.ifi.seal.soprafs15.group_09_android.service.PusherService;
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
    private PlayerArrayAdapter playerArrayAdapter; // adapts the ArrayList of Games to the ListView

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

        PusherService.getInstance().bind("MOVE_EVENT", new SubscriptionEventListener() {
            @Override
            public void onEvent(String channel, String event, String data) {
                System.out.println("Received event with data: " + data);
                Gson gson = new GsonBuilder().registerTypeAdapterFactory(new AutoValueAdapterFactory()).create();
                MoveEvent e = gson.fromJson(data, MoveEvent.class);
            }
        });

        // Hide button if user is not the owner
        if (isOwner) {
            startGameButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickStartGameButton(v);
                }
            });
        } else {
            startGameButton.setVisibility(View.INVISIBLE);
        }

        playerArrayAdapter = new PlayerArrayAdapter(
                getActivity(),
                R.layout.player_item,
                R.id.player_list_item,
                new ArrayList<User>());
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
                    playerArrayAdapter.add(player);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                tvLogBox.setText("ERROR: " + error.getMessage());
            }
        });
    }

    private void onClickStartGameButton(View v) {
        Toast.makeText(v.getContext(), "You (your ID = " + playerId + ") started game \"" + gameId + "\"", Toast.LENGTH_LONG).show();
        Intent intent = new Intent();
        intent.setClass(getActivity(), GameActivity.class);
        startActivity(intent);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        User selectedPlayer = (User) getListAdapter().getItem(position);
        Toast.makeText(v.getContext(), "You selected User \"" + selectedPlayer.username() + "\" with his id (" + selectedPlayer.id() + ")", Toast.LENGTH_LONG).show();
    }
}


