package ch.uzh.ifi.seal.soprafs15.group_09_android.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import ch.uzh.ifi.seal.soprafs15.group_09_android.R;
import ch.uzh.ifi.seal.soprafs15.group_09_android.fragments.GameFragment;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.beans.GameBean;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.beans.UserBean;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.enums.AreaName;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.events.PushEventNameEnum;
import ch.uzh.ifi.seal.soprafs15.group_09_android.service.AreaService;
import ch.uzh.ifi.seal.soprafs15.group_09_android.service.AreaUpdateSubscriber;
import ch.uzh.ifi.seal.soprafs15.group_09_android.service.PusherEventSubscriber;
import ch.uzh.ifi.seal.soprafs15.group_09_android.service.PusherService;
import ch.uzh.ifi.seal.soprafs15.group_09_android.service.RestService;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class GameActivity extends MainActivity implements GameFragment.OnBackPressedListener {

    private Long gameId;
    private Long userId;
    private int playerId;
    private String token;
    private String channelName;

    private HashMap<AreaName, AreaUpdateSubscriber> subscribedAreas = new HashMap<>();
    private HashMap<PushEventNameEnum, PusherEventSubscriber> subscribedPushers = new HashMap<>();

    /**
     * When the activity is created, do the following:
     *      - set window to full screen
     *      - get the parameters passed by GameLobbyFragment (gameId and userId)
     *      - initialize the whole board (RaceTrack, DiceArea and LegBettingArea)
     *      - start playing
     * @param savedInstanceState -
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        if (savedInstanceState == null) {
            Bundle b = getIntent().getExtras();
            gameId = b.getLong("gameId");
            playerId = b.getInt("playerId");
            userId = b.getLong("userId");
            channelName = b.getString("gameChannel");
            Boolean isFastMode = b.getBoolean("isFastMode");

            Fragment fragment = GameFragment.newInstance();
            Bundle bundle = new Bundle();
            bundle.putLong("gameId", gameId);
            bundle.putBoolean("isFastMode", isFastMode);
            bundle.putString("gameChannel", channelName);
            fragment.setArguments(bundle);

            SharedPreferences sharedPref = this.getSharedPreferences("token", Context.MODE_PRIVATE);
            token = sharedPref.getString("token", token);

            setFragment(fragment);
        }
    }

    public void unsubscribeFromEvents(){
        for (Map.Entry<PushEventNameEnum, PusherEventSubscriber> subscribedPusher : subscribedPushers.entrySet()){
            PusherService.getInstance(this).removeSubscriber(subscribedPusher.getKey(), subscribedPusher.getValue());
        }
        subscribedPushers.clear();
        Log.d("GameActivity", "unsubscribeFromLobbyEvents DONE");
    }
    public void unsubscribeFromAreas(){
        for (Map.Entry<AreaName, AreaUpdateSubscriber> subscribedArea : subscribedAreas.entrySet()){
            AreaService.getInstance(this).removeSubscriber(subscribedArea.getKey(), subscribedArea.getValue());
        }
        subscribedAreas.clear();
        Log.d("GameActivity", "unsubscribeFromAreas DONE");
    }
    public void setSubscribedAreas (HashMap<AreaName, AreaUpdateSubscriber> subscribedAreas){
        this.subscribedAreas = subscribedAreas;
        Log.d("GameActivity", "setSubscribedAreas DONE");
    }
    public void setSubscribedPushers (HashMap<PushEventNameEnum, PusherEventSubscriber> subscribedPushers){
        this.subscribedPushers = subscribedPushers;
        Log.d("GameActivity", "setSubscribedLobbyPushers DONE");
    }

    @Override
    public void onBackPressed(){
        AlertDialog dialog = warningPopup();
        dialog.show();
    }

    private AlertDialog warningPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("You have clicked on the back Button")
                .setTitle("Do you want to log out?:");
        builder.setPositiveButton("Stay in GameBean", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // do nothing but close popup
            }
        });
        builder.setNegativeButton("Log out", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //removePlayerFromGame();

                unsubscribeFromAreas();
                unsubscribeFromEvents();

                PusherService.getInstance(getApplicationContext()).unsubscribeFromChannel(channelName);
                PusherService.getInstance(getApplicationContext()).removeAllSubscriber();

                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), MenuActivity.class);
                Bundle b = new Bundle();
                b.putLong("userId", userId);
                b.putString("token", token);
                intent.putExtras(b);
                startActivity(intent);
                finish();
            }
        });
        return builder.create();
    }

    public void removePlayerFromGame() {
        RestService.getInstance(this).removeGamePlayer(gameId, playerId, UserBean.setToken(token), new Callback<UserBean>() {
            @Override
            public void success(UserBean user, Response response) {

            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Toast.makeText(getApplicationContext(), "Remove Player from Game Failed: " + retrofitError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
