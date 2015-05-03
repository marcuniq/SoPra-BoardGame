package ch.uzh.ifi.seal.soprafs15.group_09_android.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import ch.uzh.ifi.seal.soprafs15.group_09_android.R;
import ch.uzh.ifi.seal.soprafs15.group_09_android.fragments.GameFragment;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.beans.GameBean;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.beans.UserBean;
import ch.uzh.ifi.seal.soprafs15.group_09_android.service.RestService;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class GameActivity extends MainActivity  {

    private Long gameId;
    private Long userId;
    private int playerId;
    private String token;

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
            Boolean fastMode = b.getBoolean("fastMode");

            Fragment fragment = GameFragment.newInstance();
            Bundle bundle = new Bundle();
            bundle.putLong("gameId", gameId);
            bundle.putBoolean("fastMode", fastMode);
            fragment.setArguments(bundle);

            SharedPreferences sharedPref = this.getSharedPreferences("token", Context.MODE_PRIVATE);
            token = sharedPref.getString("token", token);

            setFragment(fragment);
        }
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
                removePlayerFromGame();

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

    public void removeGame() {
        RestService.getInstance(this).removeGame(gameId, UserBean.setToken(token), new Callback<GameBean>() {
            @Override
            public void success(GameBean game, Response response) {

            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Toast.makeText(getApplicationContext(), "Remove Game Failed: " + retrofitError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
