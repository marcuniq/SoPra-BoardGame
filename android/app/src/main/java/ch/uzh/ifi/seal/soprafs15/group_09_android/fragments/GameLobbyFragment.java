package ch.uzh.ifi.seal.soprafs15.group_09_android.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;

import ch.uzh.ifi.seal.soprafs15.group_09_android.R;
import ch.uzh.ifi.seal.soprafs15.group_09_android.activities.GameActivity;
import ch.uzh.ifi.seal.soprafs15.group_09_android.activities.MenuActivity;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.Game;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.User;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.events.AbstractPusherEvent;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.events.GameStartEvent;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.events.MoveEvent;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.events.PlayerJoinedEvent;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.events.PushEventNameEnum;
import ch.uzh.ifi.seal.soprafs15.group_09_android.service.PusherEventSubscriber;
import ch.uzh.ifi.seal.soprafs15.group_09_android.service.PusherService;
import ch.uzh.ifi.seal.soprafs15.group_09_android.service.RestService;
import ch.uzh.ifi.seal.soprafs15.group_09_android.utils.PlayerArrayAdapter;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

//public class GameLobbyFragment extends ListFragment {
public class GameLobbyFragment extends ListFragment {

    private TextView tvLogBox;
    private Long gameId;
    private Long userId;
    private Integer playerId;
    private Boolean isOwner;
    private PlayerArrayAdapter playerArrayAdapter; // adapts the ArrayList of Games to the ListView
    private ImageView ivPlayerCard;
    private Boolean fastMode = false;
    private CheckBox checkBox;
    private String token;
    private List<User> players;
    private boolean noLogout = true;

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
        userId = this.getArguments().getLong("userId");

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        token = sharedPref.getString("token", token);

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
        checkBox = (CheckBox) v.findViewById(R.id.checkBox);

        subscribeToEvents();

        // Hide button and fast mode if user is not the owner
        if (isOwner) {
            startGameButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fastMode = checkBox.isChecked();

                    if (fastMode) {
                        startGameInFastMode();
                    }
                    else {
                        startGame();
                    }
                }
            });
        } else {
            checkBox.setVisibility(View.INVISIBLE);
            startGameButton.setVisibility(View.INVISIBLE);
        }


        playerArrayAdapter = new PlayerArrayAdapter(
                getActivity(),
                R.layout.player_item,
                R.id.player_item_text,
                R.id.player_item_description,
                R.id.player_item_icon,
                new ArrayList<User>());
        setListAdapter(playerArrayAdapter);

        return v;
    }

    @Override
    public void onResume(){
        super.onResume();
        getPlayers();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    AlertDialog dialog = warningPopup();
                    dialog.show();
                    return true;
                }
                return false;
            }
        });
    }

    private AlertDialog warningPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("You have clicked on the back Button")
                .setTitle("Do you want to log out from the lobby?:");
        builder.setPositiveButton("Stay in Game", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // do nothing but close popup
            }
        });
        builder.setNegativeButton("Log out", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // TODO: logout User
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        return builder.create();
    }

    private void onStartGame() {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        token = sharedPref.getString("token", token);

        Intent intent = new Intent();
        intent.setClass(getActivity(), GameActivity.class);
        Bundle b = new Bundle();
        b.putLong("gameId", gameId);
        b.putLong("userId", userId);
        b.putInt("playerId", playerId);
        b.putBoolean("fastMode", fastMode);
        b.putString("token", token);
        intent.putExtras(b);
        startActivity(intent);
        getActivity().finish();
    }

    private void subscribeToEvents(){
        System.out.println("subscribe to game start");
        PusherService.getInstance(getActivity()).addSubscriber(PushEventNameEnum.GAME_START_EVENT,
            new PusherEventSubscriber() {
                @Override
                public void onNewEvent(final AbstractPusherEvent event) {
                    System.out.println("got game start event");

                    GameStartEvent gameStartEvent = (GameStartEvent) event;

                    playerId = gameStartEvent.getUserIdToPlayerIdMap().get(userId);

                    onStartGame();
                }
            });

        System.out.println("subscribe to player joined events");
        PusherService.getInstance(getActivity()).addSubscriber(PushEventNameEnum.PLAYER_JOINED_EVENT,
                new PusherEventSubscriber() {
                    @Override
                    public void onNewEvent(final AbstractPusherEvent event) {
                        System.out.println("got player joined event");

                        PlayerJoinedEvent playerJoinedEvent = (PlayerJoinedEvent) event;

                        getPlayers();
                    }
                });
    }

    private void startGame(){
        User user = User.setToken(token);
        RestService.getInstance(getActivity()).start(gameId, user, new Callback<Game>() {
            @Override
            public void success(Game game, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    private void startGameInFastMode(){
        User user = User.setToken(token);
        RestService.getInstance(getActivity()).startFastMode(gameId, user, new Callback<Game>() {
            @Override
            public void success(Game game, Response response) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("success: " + response.toString()).setTitle("We have a message for you:");
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                builder.create().show();
            }

            @Override
            public void failure(RetrofitError error) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("failure: " + error.toString());
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                builder.create().show();
            }
        });
    }

    private void getPlayers(){
        RestService.getInstance(getActivity()).getPlayers(gameId, new Callback<List<User>>() {
            @Override
            public void success(List<User> newPlayers, Response response) {
                players = newPlayers;
                playerArrayAdapter.clear();
                setListAdapter(playerArrayAdapter);
                ImageView playerCard = (ImageView)getActivity().findViewById(R.id.player_card);
                int cardId;
                for (User player : newPlayers) {
                    playerArrayAdapter.add(player);
                    cardId = newPlayers.indexOf(player) + 1;
                    if (userId.equals(player.id())) playerCard.setImageResource(getActivity().getResources().getIdentifier("c" + cardId, "drawable", getActivity().getPackageName()));
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), "ERROR: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}


