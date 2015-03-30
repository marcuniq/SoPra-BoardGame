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
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.RestUri;
import ch.uzh.ifi.seal.soprafs15.group_09_android.service.RestService;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CreateGameFragment extends Fragment {

    private EditText etName;
    private TextView tvLogBox;
    private Button createGameButton;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LoginFragment.
     */
    public static CreateGameFragment newInstance() {
        return new CreateGameFragment();
    }

    public CreateGameFragment() { }

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
        Game game = Game.create(name);

        RestService.getInstance(getActivity()).createGame(game, new Callback<RestUri>() {
            @Override
            public void success(RestUri restUri, Response response) {

                /* TODO When the server doesn't create the game as suppoed, we get a NULL Object.
                *  This is a problem because we cannot access e.g. the restUri etc. */
                if (restUri == null){
                    Log.v("GameCreate","Creation Failed. NULL Object returned.");
                }
                 /* See all already created games (testing) */
                 ((MenuActivity)getActivity()).setFragment(GameLobbyFragment.newInstance());
            }

            @Override
            public void failure(RetrofitError error) {
                tvLogBox.setText("ERROR: " + error.getMessage());
            }
        });
    }
}
