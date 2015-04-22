package ch.uzh.ifi.seal.soprafs15.group_09_android.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Window;
import android.view.WindowManager;
import ch.uzh.ifi.seal.soprafs15.group_09_android.R;
import ch.uzh.ifi.seal.soprafs15.group_09_android.fragments.GameFragment;

public class GameActivity extends MainActivity  {

    /**
     * When the activity is created, do the following:
     *      - set window to full screen
     *      - get the parameters passed by GameLobbyFragment (gameId and playerId)
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
            Long gameId = b.getLong("gameId");

            Fragment fragment = GameFragment.newInstance();
            Bundle bundle = new Bundle();
            bundle.putLong("gameId", gameId);
            fragment.setArguments(bundle);

            setFragment(fragment);
        }
    }

}
