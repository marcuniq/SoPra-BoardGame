package ch.uzh.ifi.seal.soprafs15.group_09_android.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ch.uzh.ifi.seal.soprafs15.group_09_android.R;
import ch.uzh.ifi.seal.soprafs15.group_09_android.activities.MenuActivity;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.beans.UserBean;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.beans.GameBean;
import ch.uzh.ifi.seal.soprafs15.group_09_android.service.PusherService;
import ch.uzh.ifi.seal.soprafs15.group_09_android.service.RestService;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class GameCreatorFragment extends Fragment {

    private EditText etName;
    private TextView tvLogBox;
    private Button createGameButton;
    private String token;
    private UserBean player;
    private Long userId;
    private Long joinedGameId;

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

        userId = this.getArguments().getLong("userId");
    }

    /**
     * UserBean can add a game name and make a POST request to the server and thus create a new game
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
        SharedPreferences sharedPref = getActivity().getSharedPreferences("token", Context.MODE_PRIVATE);
        token = sharedPref.getString("token", token);

        RestService.getInstance(getActivity()).createGame(GameBean.create(name, token), new Callback<GameBean>() {
            @Override
            public void success(GameBean game, Response response) {
                Long gameId = game.id();
                joinedGameId = gameId;

                // subscribe to pusher events
                PusherService.getInstance(getActivity()).register(joinedGameId, game.channelName());

                Fragment fragment = GameLobbyFragment.newInstance();
                Bundle bundle = new Bundle();
                bundle.putLong("gameId", joinedGameId);
                bundle.putLong("userId", userId);
                bundle.putBoolean("isOwner", true);
                bundle.putString("gameChannel", game.channelName());
                fragment.setArguments(bundle);

                ((MenuActivity) getActivity()).pushFragment(fragment);
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), "Crate Game Failed: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
