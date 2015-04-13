package ch.uzh.ifi.seal.soprafs15.group_09_android.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import ch.uzh.ifi.seal.soprafs15.group_09_android.R;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.User;

//public class GameLobbyFragment extends ListFragment {
public class GameLobbyFragment extends Fragment {

    private Long gameId;
    private Long playerId;
    private Button startGameButton;

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
    }

    /**
     * Creates a new view, instantiates a new ArrayAdapter that 'links' the ArrayList<String>
     * to the ListView which is then displayed.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_lobby, container, false);

        startGameButton = (Button) v.findViewById(R.id.startButton);
        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickStartGameButton(v);
            }
        });

        return v;
    }

    private void onClickStartGameButton(View v) {
        // TODO: start game
        Toast.makeText(v.getContext(), "You (" + playerId + ") started game \"" + gameId + "\"", Toast.LENGTH_LONG).show();
    }
}


