package ch.uzh.ifi.seal.soprafs15.group_09_android.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.uzh.ifi.seal.soprafs15.group_09_android.R;
import ch.uzh.ifi.seal.soprafs15.group_09_android.activities.GameActivity;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.beans.UserBean;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.beans.GameBean;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.events.AbstractPusherEvent;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.events.GameStartEvent;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.events.PlayerJoinedEvent;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.events.PushEventNameEnum;
import ch.uzh.ifi.seal.soprafs15.group_09_android.service.PusherAPIService;
import ch.uzh.ifi.seal.soprafs15.group_09_android.service.PusherEventSubscriber;
import ch.uzh.ifi.seal.soprafs15.group_09_android.service.PusherService;
import ch.uzh.ifi.seal.soprafs15.group_09_android.service.RestService;
import ch.uzh.ifi.seal.soprafs15.group_09_android.utils.PlayerArrayAdapter;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

//public class GameLobbyFragment extends ListFragment {
public class GameLobbyFragment extends ListFragment {

    private Long gameId;
    private Long userId;
    private Integer playerId;
    private Boolean isOwner;
    private PlayerArrayAdapter playerArrayAdapter; // adapts the ArrayList of Games to the ListView
    private Boolean isFastMode = false;
    private CheckBox checkBox;
    private String token;
    private String channelName;
    private List<UserBean> players;
    private boolean noLogout = true;

    private HashMap<PushEventNameEnum, PusherEventSubscriber> subscribedPushers = new HashMap<>();

    public GameLobbyFragment() {}

    /**
     * Called after UserBean has successfully logged in.
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
        isOwner = this.getArguments().getBoolean("isOwner");
        channelName = this.getArguments().getString("gameChannel");

        SharedPreferences sharedPref = getActivity().getSharedPreferences("token", Context.MODE_PRIVATE);
        token = sharedPref.getString("token", token);
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
                    isFastMode = checkBox.isChecked();
                    if (isFastMode) startGameInFastMode();
                    else startGame();
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
                new ArrayList<UserBean>(),
                false);
        setListAdapter(playerArrayAdapter);

        return v;
    }

    @Override
    public void onResume(){
        super.onResume();
        getPlayers();

        /* Handle Back Button input */
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
                unsubscribeFromEvents();
                PusherService.getInstance(getActivity()).unsubscribeFromChannel(channelName);
                PusherService.getInstance(getActivity()).unregister(gameId, channelName);
                //removePlayerFromGame();
                getActivity().onBackPressed();
            }
        });
        return builder.create();
    }

    private void onStartGame() {
        unsubscribeFromEvents();
        Intent intent = new Intent();
        intent.setClass(getActivity(), GameActivity.class);
        Bundle b = new Bundle();
        b.putLong("gameId", gameId);
        b.putLong("userId", userId);
        if(playerId != null)
            b.putInt("playerId", playerId);
        b.putBoolean("isFastMode", isFastMode);
        b.putString("gameChannel", channelName);
        intent.putExtras(b);
        startActivity(intent);
        getActivity().finish();
    }

    private void subscribeToEvents(){
        PushEventNameEnum pushEventNameEnum;
        PusherEventSubscriber pusherEventSubscriber;

        Log.i("GameLobbyFragment", "subscribed to GAME_START_EVENT");
        PusherService.getInstance(getActivity()).addSubscriber(
                pushEventNameEnum = PushEventNameEnum.GAME_START_EVENT,
                pusherEventSubscriber = new PusherEventSubscriber() {
                    @Override
                    public void onNewEvent(final AbstractPusherEvent event) {
                        Log.d("GameLobbyFragment", "got new GAME_START_EVENT");

                        GameStartEvent gameStartEvent = (GameStartEvent) event;

                        playerId = gameStartEvent.getUserIdToPlayerIdMap().get(userId);

                        onStartGame();
                    }
                });
        subscribedPushers.put(pushEventNameEnum, pusherEventSubscriber);

        Log.i("GameLobbyFragment", "subscribed to PLAYER_JOINED_EVENT");
        PusherService.getInstance(getActivity()).addSubscriber(
                pushEventNameEnum = PushEventNameEnum.PLAYER_JOINED_EVENT,
                pusherEventSubscriber = new PusherEventSubscriber() {
                    @Override
                    public void onNewEvent(final AbstractPusherEvent event) {
                        Log.d("GameLobbyFragment", "got new PLAYER_JOINED_EVENT");

                        PlayerJoinedEvent playerJoinedEvent = (PlayerJoinedEvent) event;

                        getPlayers();
                    }
                });
        subscribedPushers.put(pushEventNameEnum, pusherEventSubscriber);
    }

    private void startGame(){
        RestService.getInstance(getActivity()).start(gameId, UserBean.setToken(token), new Callback<GameBean>() {
            @Override
            public void success(GameBean game, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), "Start Game Failed: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void startGameInFastMode(){
        RestService.getInstance(getActivity()).startFastMode(gameId, UserBean.setToken(token), new Callback<GameBean>() {
            @Override
            public void success(GameBean game, Response response) {
                PusherService.getInstance(getActivity()).unsubscribeFromChannel(channelName);
                onStartGame();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), "Start Game in Fast Mode Failed: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getPlayers(){
        RestService.getInstance(getActivity()).getPlayers(gameId, new Callback<List<UserBean>>() {
            @Override
            public void success(List<UserBean> newPlayers, Response response) {
                playerArrayAdapter.clear();
                setListAdapter(playerArrayAdapter);
                ImageView playerCard = (ImageView)getActivity().findViewById(R.id.player_card);
                int cardId;
                for (UserBean player : newPlayers) {
                    playerArrayAdapter.add(player);
                    cardId = newPlayers.indexOf(player) + 1;
                    if (userId.equals(player.id())) playerCard.setImageResource(getActivity().getResources().getIdentifier("c" + cardId, "drawable", getActivity().getPackageName()));
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), "Get Players of the game failed: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void removePlayerFromGame() {
        RestService.getInstance(getActivity()).removeGamePlayerAsUser(gameId, playerId, true, UserBean.setToken(token), new Callback<UserBean>() {
            @Override
            public void success(UserBean user, Response response) {
                getActivity().getSupportFragmentManager().popBackStack();
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Toast.makeText(getActivity(), "Remove Player from Game Failed: " + retrofitError.getMessage(), Toast.LENGTH_LONG).show();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

    private void unsubscribeFromEvents(){
        for (Map.Entry<PushEventNameEnum, PusherEventSubscriber> subscribedPusher : subscribedPushers.entrySet()){
            PusherService.getInstance(getActivity()).removeSubscriber(subscribedPusher.getKey(), subscribedPusher.getValue());
        }
        subscribedPushers.clear();
    }
}


