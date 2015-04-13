package ch.uzh.ifi.seal.soprafs15.group_09_android.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import ch.uzh.ifi.seal.soprafs15.group_09_android.R;
import ch.uzh.ifi.seal.soprafs15.group_09_android.activities.MenuActivity;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.Game;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.Token;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.User;
import ch.uzh.ifi.seal.soprafs15.group_09_android.service.RestService;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class GameCreatorFragment extends Fragment {

    private EditText etName;
    private TextView tvLogBox;
    private Button createGameButton;
    private String token;
    private User player;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LoginFragment.
     */
    public static GameCreatorFragment newInstance() {
        return new GameCreatorFragment();
    }

    public GameCreatorFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        View v = inflater.inflate(R.layout.fragment_game_creator, container, false);

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
     * Creates a new view with a editText field to insert the game name and a button to make the
     * POST request on the server
     *
     * @param v the current View
     */
    private void onClickCreateGameButton(View v) {
        String name = etName.getText().toString();

        // @see http://developer.android.com/training/basics/data-storage/shared-preferences.html
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        token = sharedPref.getString("token", token);

        Game game = Game.create( name,                  // name of the game
                                 token);                // token of current user

        RestService.getInstance(getActivity()).createGame(game, new Callback<Game>() {

            @Override
            public void success(Game game, Response response) {
                try {
                    Long gameId = game.id();

                    joinGame(gameId);

                        Fragment fragment = GameLobbyFragment.newInstance();

                        Bundle bundle = new Bundle();
                        bundle.putLong("gameId", gameId);
                        bundle.putLong("playerId",player.id());
                        fragment.setArguments(bundle);

                        ((MenuActivity) getActivity()).setFragment(fragment);
                } catch ( NullPointerException e) {
                    Log.e("GameCreate", "null pointer exception");
                }
            }

            @Override
            public void failure(RetrofitError error) {
                tvLogBox.setText("ERROR: " + error.getMessage());
            }
        });
    }

    private void joinGame(Long gameId) {
        Token theToken = Token.create(token);

        RestService.getInstance(getActivity()).joinGame(gameId, theToken, new Callback<User>() {

            @Override
            public void success(User myPlayer, Response response) {
                try {
                    player = myPlayer;
                } catch (NullPointerException e) {
                    Log.e("GameCreate", "null pointer exception");
                }
            }

            @Override
            public void failure(RetrofitError error) {
                tvLogBox.setText("ERROR: " + error.getMessage());
            }
        });
    }
}
