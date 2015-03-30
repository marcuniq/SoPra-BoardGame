package ch.uzh.ifi.seal.soprafs15.group_09_android.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ch.uzh.ifi.seal.soprafs15.group_09_android.R;
import ch.uzh.ifi.seal.soprafs15.group_09_android.activities.MenuActivity;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.Game;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.RestUri;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.User;
import ch.uzh.ifi.seal.soprafs15.group_09_android.service.RestService;
import ch.uzh.ifi.seal.soprafs15.group_09_android.utils.GameStatus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CreateGameFragment extends Fragment {

    private EditText etName;
    private TextView tvLogBox;
    private Button createGameButton;
    private User user;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LoginFragment.
     */
    public static CreateGameFragment newInstance(User user) {
        CreateGameFragment fragment = new CreateGameFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("user", user);
        fragment.setArguments(bundle);
        return fragment;
    }

    public CreateGameFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = getArguments().getParcelable("user");
    }

    /**
     * User can add a game name and make a POST request to the server and thus create a new game
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_create_game, container, false);

        etName = (EditText) v.findViewById(R.id.etNewGameName);
        tvLogBox = (TextView) v.findViewById(R.id.tvNewGameName);

        createGameButton = (Button) v.findViewById(R.id.createGameButton);
        createGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCreateGameButton(v);
            }
        });

        return v;
    }

    /**
     * Creates a new view with a editText field to insert the gamename and a button to make the
     * POST request on the server
     *
     * @param v the current View
     */
    private void onClickCreateGameButton(View v) {
        String name = etName.getText().toString();
        List<User> players = new ArrayList<User>();
        players.add(user);

        Game game = Game.create( null,                  // game id
                                 name,                  // name of the game
                                 user,                  // host/owner
                                 null,                  // current player
                                 players,               // List of players
                                 null,                  // List of moves
                                 GameStatus.PENDING,    // Game status
                                 null,                  // RaceTrack
                                 null,                  // LegBettingArea
                                 null,                  // RaceBettingArea
                                 null );                // DiceArea

        RestService.getInstance(getActivity()).createGame(game, new Callback<Game>() {
            @Override
            public void success(Game game, Response response) {
                 /* See all already created games (testing) */
                 ((MenuActivity)getActivity()).setFragment(GameLobbyFragment.newInstance(user, game));
            }

            @Override
            public void failure(RetrofitError error) {
                tvLogBox.setText("ERROR: " + error.getMessage());
            }
        });
    }
}
