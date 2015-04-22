package ch.uzh.ifi.seal.soprafs15.group_09_android.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ch.uzh.ifi.seal.soprafs15.group_09_android.R;
import ch.uzh.ifi.seal.soprafs15.group_09_android.activities.GameActivity;
import ch.uzh.ifi.seal.soprafs15.group_09_android.activities.MenuActivity;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.DiceArea;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.LegBettingArea;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.Move;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.RaceBettingArea;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.RaceTrack;
import ch.uzh.ifi.seal.soprafs15.group_09_android.service.RestService;
import ch.uzh.ifi.seal.soprafs15.group_09_android.utils.RaceTrackField;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class GameFragment extends Fragment implements View.OnClickListener {

    private ArrayList<RaceTrackField> raceTrack = new ArrayList<>();
    private ArrayList<Integer> legBettingArea = new ArrayList<>();

    private TextView tvPlayerName;
    private ImageButton ivPlayerIcon;
    private TextView tvCurrentPlayerName;
    private ImageView  ivCurrentPlayerIcon;
    private TextView tvMoney;

    private Bundle savedInstanceState;
    private Long playerId;
    private Long gameId;

    private PopupWindow popupWindow;

    public static GameFragment newInstance() {
        return new GameFragment();
    }

    public GameFragment() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_game, container, false);
        this.savedInstanceState = savedInstanceState;
        Bundle b = getActivity().getIntent().getExtras();
        gameId = b.getLong("gameId");

        Toast.makeText(v.getContext(), "GameId = " + gameId, Toast.LENGTH_LONG).show();

        return v;
    }

    @Override
    public void onResume(){
        super.onResume();
        initializeRaceTrack();
        initializeLegBettingArea();
        initializeDiceArea();

        play();
    }


    /**
     * Adds possible interaction with the game.
     * Searches through all lists (raceTrack, legBettingArea and the dice) and checks if the
     * id of the parameter matches any id in the lists.
     *
     * When an id is found, do something with it (e.g. display an alert message)
     *
     * @param v the view that is clicked on
     */
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
//            message = (String) v.getContentDescription();
//            ((GameActivity)getActivity()).pushFragment(RollDiceFragment.newInstance());
                rollDice(v);
            return;
        }

        AlertDialog dialog = dummyPopup(message);
        dialog.show();
    }

    public void rollDice(View anchorView) {
        View popupView = getLayoutInflater(savedInstanceState).inflate(R.layout.fragment_roll_dice, null);
        popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(anchorView, Gravity.CENTER_HORIZONTAL,0,0);
        Button acceptButton = (Button) popupView.findViewById(R.id.accept);
        Button rejectButton = (Button) popupView.findViewById(R.id.reject);

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }


    /**
     * This is only for testing purposes, an alert popup that displays dynamic messages.
     *
     * @param message the message to be shown in the popup body
     * @return the AlertDialog object with its (new) parameters
     */
    private AlertDialog dummyPopup(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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

        ImageView image = new ImageView(getActivity());
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
     * Adds all field to the OnClickListener
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
            (getActivity().findViewById(field.getPosition())).setOnClickListener(this);
        }
    }

    /**
     * Adds all legBettingAreas to an ArrayList
     * Adds all legBettingAreas to the OnClickListener
     */
    private void initializeLegBettingArea(){
        legBettingArea.add(R.id.legbetting_blue);
        legBettingArea.add(R.id.legbetting_green);
        legBettingArea.add(R.id.legbetting_orange);
        legBettingArea.add(R.id.legbetting_yellow);
        legBettingArea.add(R.id.legbetting_white);
        legBettingArea.add(R.id.winner_betting);
        legBettingArea.add(R.id.loser_betting);

        for (Integer legbet: legBettingArea) {
            (getActivity().findViewById(legbet)).setOnClickListener(this);
        }
    }

    /**
     * Adds the dice button (pyramid) to the OnClickListener
     */
    private void initializeDiceArea(){
        (getActivity().findViewById(R.id.dice)).setOnClickListener(this);
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
        RestService.getInstance(getActivity()).getGameMoves(gameId, new Callback<List<Move>>() {

            @Override
            public void success(List<Move> moves, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    /**
     * @api  http://docs.sopra.apiary.io/#reference/games/game-race-track/retrieve-race-track
     */
    private void gameRaceTrack() {
        RestService.getInstance(getActivity()).getGameRaceTrack(gameId, new Callback<List<RaceTrack>>() {

            @Override
            public void success(List<RaceTrack> raceTrackField, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    /**
     * @api  http://docs.sopra.apiary.io/#reference/games/game-leg-betting-area/retrieve-leg-betting-area
     */
    private void gameLegBettingArea() {
        RestService.getInstance(getActivity()).getGameLegBettingArea(gameId, new Callback<List<LegBettingArea>>() {

            @Override
            public void success(List<LegBettingArea> legBettingAreas, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    /**
     * @api  http://docs.sopra.apiary.io/#reference/games/game-race-betting-area/retrieve-race-betting-area
     */
    private void gameRaceBettingArea() {
        RestService.getInstance(getActivity()).getGameRaceBettingArea(gameId, new Callback<List<RaceBettingArea>>() {

            @Override
            public void success(List<RaceBettingArea> raceBettingAreas, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    /**
     * @api  http://docs.sopra.apiary.io/#reference/games/game-dice-area/retrieve-dice-area
     */
    private void gameDiceArea() {
        RestService.getInstance(getActivity()).getGameDiceArea(gameId, new Callback<List<DiceArea>>() {

            @Override
            public void success(List<DiceArea> diceAreas, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {

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
            RelativeLayout fieldLayout = (RelativeLayout) getActivity().findViewById(field.getPosition());

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
            fieldLayout = (RelativeLayout) getActivity().findViewById(field.getPosition());
            if (!(fieldLayout == null) && fieldLayout.getChildCount() > 0) {
                fieldLayout.removeAllViews();
            }
        }
    }

}
