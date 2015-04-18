package ch.uzh.ifi.seal.soprafs15.group_09_android.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import android.widget.TextView;
import ch.uzh.ifi.seal.soprafs15.group_09_android.R;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.*;
import ch.uzh.ifi.seal.soprafs15.group_09_android.service.RestService;
import ch.uzh.ifi.seal.soprafs15.group_09_android.utils.RaceTrackField;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class GameActivity extends Activity implements View.OnClickListener {

    private ArrayList<RaceTrackField> raceTrack = new ArrayList<>();
    private ArrayList<Integer> legBettingArea = new ArrayList<>();
    private ArrayList<Integer> diceArea = new ArrayList<>();
    private TextView tvLogBox;
    private Long gameId;
    private boolean isOwner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set fullscreen mode
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // get the gameId and current playerId
        Bundle b = getIntent().getExtras();
        gameId = b.getLong("gameId");
        isOwner = b.getBoolean("isOwner");

        setContentView(R.layout.activity_game);

        initializeRaceTrack();
        initializeLegBettingArea();
        initializeDiceArea();

        play();
    }


    public void onClick(View v) {
        String message = "Oops! Not found!";

        for (RaceTrackField field: raceTrack) {
            if (field.getPosition() == v.getId()) {
                message = (String) v.getContentDescription();
                AlertDialog dialog = dummyPopup(message);
                dialog.show();
                return;
            }
        }
        for (Integer legBet: legBettingArea) {
            if (legBet == v.getId()) {
                message = (String) v.getContentDescription();
                AlertDialog dialog = dummyPopup(message);
                dialog.show();
                return;
            }
        }
        if (R.id.dice == v.getId()){
            message = (String) v.getContentDescription();
        }

        AlertDialog dialog = dummyPopup(message);
        dialog.show();
    }

    private AlertDialog dummyPopup(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setTitle("Congratulations");
        // Add the buttons
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        return builder.create();
    }

    /**
     * creates a Camel, Oasis or Desert (or any) image view with defined margins to an ImageView
     * and returns that view
     *
     * @param margin The margin a camel will be shifted from bottom-left:
     *               each camel on the top is shifted by the predefined margin (array)
     * @param raceTrackField The current field on the race track where the camel should be placed
     * @param align1 The alignment in one direction (e.g. ALIGN_PARENT_BOTTOM)
     * @param align2 The alignment in the other direction (e.g. ALIGN_PARENT_LEFT)
     * @return the ImageView with the camel to enable adding that image to a layout
     */
    private ImageView createDynamicImage(int margin, int raceTrackField, int align1, int align2){

        ImageView image = new ImageView(this);
        image.setImageResource(raceTrackField);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT );
        lp.setMargins(margin, 0, 0, margin);
        lp.addRule(align1);
        lp.addRule(align2);
        image.setLayoutParams(lp);

        return image;
    }

    /**
     * Adds all the race track fields to the game's raceTrack
     */
    private void initializeRaceTrack(){
        raceTrack.add(new RaceTrackField(R.id.field1));
        raceTrack.add(new RaceTrackField(R.id.field2));
        raceTrack.add(new RaceTrackField(R.id.field3));
        raceTrack.add(new RaceTrackField(R.id.field4));
        raceTrack.add(new RaceTrackField(R.id.field5));
        raceTrack.add(new RaceTrackField(R.id.field6));
        raceTrack.add(new RaceTrackField(R.id.field7));
        raceTrack.add(new RaceTrackField(R.id.field8));
        raceTrack.add(new RaceTrackField(R.id.field9));
        raceTrack.add(new RaceTrackField(R.id.field10));
        raceTrack.add(new RaceTrackField(R.id.field11));
        raceTrack.add(new RaceTrackField(R.id.field12));
        raceTrack.add(new RaceTrackField(R.id.field13));
        raceTrack.add(new RaceTrackField(R.id.field14));
        raceTrack.add(new RaceTrackField(R.id.field15));
        raceTrack.add(new RaceTrackField(R.id.field16));

        for (RaceTrackField field: raceTrack) {
            (findViewById(field.getPosition())).setOnClickListener(this);
        }
    }

    private void initializeLegBettingArea(){
        legBettingArea.add(R.id.legbetting_blue);
        legBettingArea.add(R.id.legbetting_green);
        legBettingArea.add(R.id.legbetting_orange);
        legBettingArea.add(R.id.legbetting_yellow);
        legBettingArea.add(R.id.legbetting_white);
        legBettingArea.add(R.id.winner_betting);
        legBettingArea.add(R.id.loser_betting);

        for (Integer legbet: legBettingArea) {
            (findViewById(legbet)).setOnClickListener(this);
        }
    }

    private void initializeDiceArea(){
        diceArea.add(R.id.dice1);
        diceArea.add(R.id.dice2);
        diceArea.add(R.id.dice3);
        diceArea.add(R.id.dice4);
        diceArea.add(R.id.dice5);

        (findViewById(R.id.dice)).setOnClickListener(this);
    }

    /**
     * This is the main method. After each player has finished his turn, the whole board is redraw.
     */
    private void play(){
        // TODO: on PUSH from SERVER; get all new information.
        // TODO: create some "round" system
//        gameMoves();
//        gameRaceTrack();
//        gameLegBettingArea();
//        gameRaceBettingArea();
//        gameDiceArea();
        addItemsToRaceTrack();

        // TODO: check if player is on his turn; then enable interaction
    }

    /**
     * @api  http://docs.sopra.apiary.io/#reference/games/game-move/retrieve-a-game-move
     */
    private void gameMoves() {
        RestService.getInstance(this).getGameMoves(gameId, new Callback<List<Move>>() {

            @Override
            public void success(List<Move> moves, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {
                tvLogBox.setText("ERROR: " + error.getMessage());
            }
        });
    }

    /**
     * @api  http://docs.sopra.apiary.io/#reference/games/game-race-track/retrieve-race-track
     */
    private void gameRaceTrack() {
        RestService.getInstance(this).getGameRaceTrack(gameId, new Callback<List<RaceTrack>>() {

            @Override
            public void success(List<RaceTrack> raceTrackField, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {
                tvLogBox.setText("ERROR: " + error.getMessage());
            }
        });
    }

    /**
     * @api  http://docs.sopra.apiary.io/#reference/games/game-leg-betting-area/retrieve-leg-betting-area
     */
    private void gameLegBettingArea() {
        RestService.getInstance(this).getGameLegBettingArea(gameId, new Callback<List<LegBettingArea>>() {

            @Override
            public void success(List<LegBettingArea> legBettingAreas, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {
                tvLogBox.setText("ERROR: " + error.getMessage());
            }
        });
    }

    /**
     * @api  http://docs.sopra.apiary.io/#reference/games/game-race-betting-area/retrieve-race-betting-area
     */
    private void gameRaceBettingArea() {
        RestService.getInstance(this).getGameRaceBettingArea(gameId, new Callback<List<RaceBettingArea>>() {

            @Override
            public void success(List<RaceBettingArea> raceBettingAreas, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {
                tvLogBox.setText("ERROR: " + error.getMessage());
            }
        });
    }

    /**
     * @api  http://docs.sopra.apiary.io/#reference/games/game-dice-area/retrieve-dice-area
     */
    private void gameDiceArea() {
        RestService.getInstance(this).getGameDiceArea(gameId, new Callback<List<DiceArea>>() {

            @Override
            public void success(List<DiceArea> diceAreas, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {
                tvLogBox.setText("ERROR: " + error.getMessage());
            }
        });
    }

    /**
     * Draw the raceTrack according to the game status: all camels, all desert/oasis tiles etc.
     */
    private void addItemsToRaceTrack() {
        int[] margins = {0, 10, 20, 30, 40};
        cleanRaceTrack();

        for (RaceTrackField field: raceTrack) {
            ArrayList<Integer> camels = field.getCamels();
            RelativeLayout fieldLayout = (RelativeLayout) findViewById(field.getPosition());

            if (field.hasDesert()) {
                fieldLayout.addView(createDynamicImage(0,
                        field.getDesert(),
                        RelativeLayout.CENTER_HORIZONTAL,
                        RelativeLayout.CENTER_VERTICAL));
            } else if (field.hasOasis()) {
                fieldLayout.addView(createDynamicImage(0,
                        field.getOasis(),
                        RelativeLayout.CENTER_HORIZONTAL,
                        RelativeLayout.CENTER_VERTICAL));
            } else if (!camels.isEmpty()) {
                for (int pos = 0; pos < camels.size(); pos++){
                    fieldLayout.addView(createDynamicImage(margins[pos],
                            camels.get(pos),
                            RelativeLayout.ALIGN_PARENT_BOTTOM,
                            RelativeLayout.ALIGN_PARENT_LEFT));
                }
            }
        }
    }

    /**
     * Removes all views from the racetrack
     */
    private void cleanRaceTrack() {
        RelativeLayout fieldLayout;
        for (RaceTrackField field: raceTrack) {
            fieldLayout = (RelativeLayout) findViewById(field.getPosition());
            if (!(fieldLayout == null) && fieldLayout.getChildCount() > 0) {
                fieldLayout.removeAllViews();
            }
        }
    }

}
