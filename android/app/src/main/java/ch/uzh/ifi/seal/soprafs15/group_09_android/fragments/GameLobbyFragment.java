package ch.uzh.ifi.seal.soprafs15.group_09_android.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import ch.uzh.ifi.seal.soprafs15.group_09_android.R;
import ch.uzh.ifi.seal.soprafs15.group_09_android.activities.GameActivity;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.User;
import ch.uzh.ifi.seal.soprafs15.group_09_android.service.RestService;
import ch.uzh.ifi.seal.soprafs15.group_09_android.utils.PlayerArrayAdapter;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.util.ArrayList;
import java.util.List;

//public class GameLobbyFragment extends ListFragment {
public class GameLobbyFragment extends ListFragment {

    private TextView tvLogBox;
    private Long gameId;
    private Long playerId;
    private Boolean isOwner;
    private PlayerArrayAdapter playerArrayAdapter; // adapts the ArrayList of Games to the ListView
    private ImageView ivPlayerCard;

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
        isOwner = this.getArguments().getBoolean("isOwner");
    }

    /**
     * Creates a new view, instantiates a new ArrayAdapter that 'links' the ArrayList<String>
     * to the ListView which is then displayed.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_lobby, container, false);

        Button startGameButton = (Button) v.findViewById(R.id.startButton);

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
                R.id.player_item_text,
                R.id.player_item_icon,
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
        /*
        Toast.makeText(v.getContext(), "You (your ID = " + playerId + ") started game \"" + gameId + "\"", Toast.LENGTH_LONG).show();
        */
        Intent intent = new Intent();
        intent.setClass(getActivity(), GameActivity.class);
        Bundle b = new Bundle();
        b.putLong("gameId", gameId);
        intent.putExtras(b);
        startActivity(intent);
        getActivity().finish();
    }
}


