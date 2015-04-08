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
import ch.uzh.ifi.seal.soprafs15.group_09_android.R;
import ch.uzh.ifi.seal.soprafs15.group_09_android.activities.MenuActivity;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.Game;
import ch.uzh.ifi.seal.soprafs15.group_09_android.service.RestService;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class GameCreatorFragment extends Fragment {

    private EditText etName;
    private TextView tvLogBox;
    private Button createGameButton;

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
        String token = "067e6162-3b6f-4ae2-a171-2470b63dff00"; // TODO: get token via http://developer.android.com/training/basics/data-storage/shared-preferences.html

        /* TODO correct naming of null values */
        Game game = Game.create( name,                  // name of the game
                                 token);                // token of current user

        RestService.getInstance(getActivity()).createGame(game, new Callback<Game>() {
            @Override
            public void success(Game game, Response response) {

                /* TODO When the server doesn't create the game as supposed, we get a NULL Object.
                *  This is a problem because we cannot access e.g. the restUri etc. */
                if (game == null){
                    Log.v("GameCreate","Creation Failed. NULL Object returned.");
                }


                Long gameId = game.id();
                if (gameId == null){
                    Log.v("GameCreate","Creation Failed. Game Id is NULL.");
                    gameId = 1L;
                }

                Fragment fragment = GameLobbyFragment.newInstance();

                Bundle bundle = new Bundle();
                bundle.putLong("gameId", gameId);
                fragment.setArguments(bundle);

                 /* See all already created games (testing) */
                ((MenuActivity) getActivity()).setFragment(fragment);
            }

            @Override
            public void failure(RetrofitError error) {
                tvLogBox.setText("ERROR: " + error.getMessage());
            }
        });
    }
}
