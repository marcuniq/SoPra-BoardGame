package ch.uzh.ifi.seal.soprafs15.group_09_android.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.*;
import ch.uzh.ifi.seal.soprafs15.group_09_android.R;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.*;
import ch.uzh.ifi.seal.soprafs15.group_09_android.service.RestService;
import ch.uzh.ifi.seal.soprafs15.group_09_android.utils.*;
import ch.uzh.ifi.seal.soprafs15.group_09_android.utils.Dice;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.util.ArrayList;
import java.util.List;

public class GameFragment extends Fragment implements View.OnClickListener {

    private ArrayList<RaceTrackField> raceTrack = new ArrayList<>();
    private ArrayList<Integer> legBettingArea = new ArrayList<>();
    private ArrayList<Integer> raceBettingArea = new ArrayList<>();
    private ArrayList<InteractionTile> interactionTiles = new ArrayList<>();
    private ArrayList<Dice> dices = new ArrayList<>();
    private ArrayList<LegBet> legBets = new ArrayList<>();
    private ArrayList<RaceBet> raceBets = new ArrayList<>();

    private Bundle savedInstanceState;
    private ViewGroup container;
    private Long playerId = 0L;
    private Long gameId;
    private GameColors cardColor;
    private PopupWindow popupWindow;
    private Button acceptButton;

    public static GameFragment newInstance() {
        return new GameFragment();
    }

    public GameFragment() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_game, container, false);
        this.savedInstanceState = savedInstanceState;
        this.container = container;
        Bundle b = getActivity().getIntent().getExtras();
        gameId = b.getLong("gameId");

        Toast.makeText(v.getContext(), "GameId = " + gameId, Toast.LENGTH_LONG).show();

        ImageButton ivHelpButton = (ImageButton) v.findViewById(R.id.help);
        ivHelpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                instructionsPopup(v, R.layout.popup_instructions);
            }
        });

        return v;
    }

    @Override
    public void onResume(){
        super.onResume();
        initializeRaceTrack();
        initializeLegBettingArea();
        initializeDiceArea();
        initializeRaceBettingArea();
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
                displayPopup(v, R.layout.popup_leg_betting, Popup.LEGBET, legBettingArea.indexOf(legBet));
                return;
            }
        }
        for (Integer raceBet : raceBettingArea) {
            if (raceBet == v.getId()) {
                displayPopup(v, R.layout.popup_race_betting, Popup.RACEBET, raceBettingArea.indexOf(raceBet));
                return;
            }
        }
        if (R.id.dice == v.getId()){
            displayPopup(v, R.layout.popup_roll_dice, Popup.ROLL_DICE, 0);
            return;
        }

        AlertDialog dialog = dummyPopup(message);
        dialog.show();
    }

    /**
     * Displays a Popup with the given layout and draws all the dices
     * Two Options:
     *  - Accept: Execute a Move and close the Popup
     *  - Reject: abort, close Popup
     * @param anchorView the current view (Bean)
     */
    public void displayPopup(View anchorView, int layout, Popup POPUPTYPE, int index) {
        View popupView = getLayoutInflater(savedInstanceState).inflate(layout, container, false);
        popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(anchorView, Gravity.CENTER_HORIZONTAL,0,0);

        switch (POPUPTYPE){
            case ROLL_DICE:
                initPopupRollDice(popupView);
                break;
            case LEGBET:
                initPopupLegBet(popupView, anchorView, index);
                break;
            case RACEBET:
                initPopupRaceBet(popupView, index);
                break;
            case PLACE_TILE:
                initPopupPlaceTile(popupView);
                break;
            case ROUND_EVALUATION:
                // initPopupRoundEvaluation()
                break;
            default:
                // do something meaningful
        }

        acceptButton = (Button) popupView.findViewById(R.id.accept);
        Button rejectButton = (Button) popupView.findViewById(R.id.reject);

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: send Game Move
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

    private void initPopupPlaceTile(View popupView){
        ImageButton desertButton = (ImageButton) popupView.findViewById(R.id.desert_tile);
        ImageButton oasisButton = (ImageButton) popupView.findViewById(R.id.oasis_tile);

        desertButton.setImageResource(interactionTiles.get(playerId.intValue()).getDesert());
        oasisButton.setImageResource(interactionTiles.get(playerId.intValue()).getOasis());

        desertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptButton.setText(R.string.button_text_desert);
            }
        });
        oasisButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardColor = GameColors.GREEN;
                acceptButton.setText(R.string.button_text_oasis);
            }
        });
    }

    private void initPopupRaceBet(View popupView, int type) {
        ImageButton cardBlue = (ImageButton) popupView.findViewById(R.id.card_blue);
        ImageButton cardGreen = (ImageButton) popupView.findViewById(R.id.card_green);
        ImageButton cardOrange = (ImageButton) popupView.findViewById(R.id.card_orange);
        ImageButton cardYellow = (ImageButton) popupView.findViewById(R.id.card_yellow);
        ImageButton cardWhite = (ImageButton) popupView.findViewById(R.id.card_white);

        ArrayList<Integer> playersCards = raceBets.get(playerId.intValue()).getAllRaceBetCards();

        cardBlue.setImageResource(playersCards.get(GameColors.BLUE.ordinal()));
        cardGreen.setImageResource(playersCards.get(GameColors.GREEN.ordinal()));
        cardOrange.setImageResource(playersCards.get(GameColors.ORANGE.ordinal()));
        cardYellow.setImageResource(playersCards.get(GameColors.YELLOW.ordinal()));
        cardWhite.setImageResource(playersCards.get(GameColors.WHITE.ordinal()));

        cardColor = null;

        cardBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardColor = GameColors.BLUE;
                acceptButton.setText(R.string.button_text_blue);
            }
        });
        cardGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardColor = GameColors.GREEN;
                acceptButton.setText(R.string.button_text_green);
            }
        });
        cardOrange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardColor = GameColors.ORANGE;
                acceptButton.setText(R.string.button_text_orange);
            }
        });
        cardYellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardColor = GameColors.YELLOW;
                acceptButton.setText(R.string.button_text_yellow);
            }
        });
        cardWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardColor = GameColors.WHITE;
                acceptButton.setText(R.string.button_text_white);
            }
        });

        TextView title = (TextView) popupView.findViewById(R.id.popupTitle);
        if (type == 0) title.setText(R.string.title_raceBet_tolle);
        else if (type == 1) title.setText(R.string.title_raceBet_olle);
    }

    private void initPopupLegBet(View popupView, View legBettingCard, int color) {
        LegBet legBet = legBets.get(color);
        int pointer = 0;

        ImageView card = (ImageView) popupView.findViewById(R.id.card);
        card.setImageResource(legBet.getCurrentLegBet());
        pointer = legBet.getLegBetPointer();
        legBet.setLegBetPointer(pointer + 1);
        legBettingCard.setBackgroundResource(legBet.getCurrentLegBetButton());
    }

    public void instructionsPopup(View anchorView, int layout) {
        View popupView = getLayoutInflater(savedInstanceState).inflate(layout, container, false);
        popupWindow = new PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);

        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(anchorView, Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private void initPopupRollDice(View popupView){
        ImageView blueDice = (ImageView) popupView.findViewById(R.id.dice_blue);
        ImageView greenDice = (ImageView) popupView.findViewById(R.id.dice_green);
        ImageView orangeDice = (ImageView) popupView.findViewById(R.id.dice_orange);
        ImageView yellowDice = (ImageView) popupView.findViewById(R.id.dice_yellow);
        ImageView whiteDice = (ImageView) popupView.findViewById(R.id.dice_white);

        blueDice.setImageResource(dices.get(GameColors.BLUE.ordinal()).getCurrentDice());
        greenDice.setImageResource(dices.get(GameColors.GREEN.ordinal()).getCurrentDice());
        orangeDice.setImageResource(dices.get(GameColors.ORANGE.ordinal()).getCurrentDice());
        yellowDice.setImageResource(dices.get(GameColors.YELLOW.ordinal()).getCurrentDice());
        whiteDice.setImageResource(dices.get(GameColors.WHITE.ordinal()).getCurrentDice());
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
        InteractionTile c1Tiles = new InteractionTile();
        InteractionTile c2Tiles = new InteractionTile();
        InteractionTile c3Tiles = new InteractionTile();
        InteractionTile c4Tiles = new InteractionTile();
        InteractionTile c5Tiles = new InteractionTile();
        InteractionTile c6Tiles = new InteractionTile();
        InteractionTile c7Tiles = new InteractionTile();
        InteractionTile c8Tiles = new InteractionTile();

        c1Tiles.setDesert(R.drawable.c1_desert);
        c1Tiles.setOasis(R.drawable.c1_oasis);

        c2Tiles.setDesert(R.drawable.c2_desert);
        c2Tiles.setOasis(R.drawable.c2_oasis);

        c3Tiles.setDesert(R.drawable.c3_desert);
        c3Tiles.setOasis(R.drawable.c3_oasis);

        c4Tiles.setDesert(R.drawable.c4_desert);
        c4Tiles.setOasis(R.drawable.c4_oasis);

        c5Tiles.setDesert(R.drawable.c5_desert);
        c5Tiles.setOasis(R.drawable.c5_oasis);

        c6Tiles.setDesert(R.drawable.c6_desert);
        c6Tiles.setOasis(R.drawable.c6_oasis);

        c7Tiles.setDesert(R.drawable.c7_desert);
        c7Tiles.setOasis(R.drawable.c7_oasis);

        c8Tiles.setDesert(R.drawable.c8_desert);
        c8Tiles.setOasis(R.drawable.c8_oasis);

        interactionTiles.add(c1Tiles);
        interactionTiles.add(c2Tiles);
        interactionTiles.add(c3Tiles);
        interactionTiles.add(c4Tiles);
        interactionTiles.add(c5Tiles);
        interactionTiles.add(c6Tiles);
        interactionTiles.add(c7Tiles);
        interactionTiles.add(c8Tiles);

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
        LegBet blueLegBets = new LegBet();
        LegBet greenLegBets = new LegBet();
        LegBet orangeLegBets = new LegBet();
        LegBet yellowLegBets = new LegBet();
        LegBet whiteLegBets = new LegBet();

        blueLegBets.add(R.drawable.legbettingtile_blue_5, R.drawable.legbettingtile_blue_5_button);
        blueLegBets.add(R.drawable.legbettingtile_blue_3, R.drawable.legbettingtile_blue_3_button);
        blueLegBets.add(R.drawable.legbettingtile_blue_2, R.drawable.legbettingtile_blue_2_button);
        blueLegBets.add(R.drawable.empty_image, R.drawable.empty_image);

        greenLegBets.add(R.drawable.legbettingtile_green_5, R.drawable.legbettingtile_green_5_button);
        greenLegBets.add(R.drawable.legbettingtile_green_3, R.drawable.legbettingtile_green_3_button);
        greenLegBets.add(R.drawable.legbettingtile_green_2, R.drawable.legbettingtile_green_2_button);
        greenLegBets.add(R.drawable.empty_image, R.drawable.empty_image);

        orangeLegBets.add(R.drawable.legbettingtile_orange_5, R.drawable.legbettingtile_orange_5_button);
        orangeLegBets.add(R.drawable.legbettingtile_orange_3, R.drawable.legbettingtile_orange_3_button);
        orangeLegBets.add(R.drawable.legbettingtile_orange_2, R.drawable.legbettingtile_orange_2_button);
        orangeLegBets.add(R.drawable.empty_image, R.drawable.empty_image);

        yellowLegBets.add(R.drawable.legbettingtile_yellow_5, R.drawable.legbettingtile_yellow_5_button);
        yellowLegBets.add(R.drawable.legbettingtile_yellow_3, R.drawable.legbettingtile_yellow_3_button);
        yellowLegBets.add(R.drawable.legbettingtile_yellow_2, R.drawable.legbettingtile_yellow_2_button);
        yellowLegBets.add(R.drawable.empty_image, R.drawable.empty_image);

        whiteLegBets.add(R.drawable.legbettingtile_white_5, R.drawable.legbettingtile_white_5_button);
        whiteLegBets.add(R.drawable.legbettingtile_white_3, R.drawable.legbettingtile_white_3_button);
        whiteLegBets.add(R.drawable.legbettingtile_white_2, R.drawable.legbettingtile_white_2_button);
        whiteLegBets.add(R.drawable.empty_image, R.drawable.empty_image);

        legBets.add(blueLegBets);
        legBets.add(greenLegBets);
        legBets.add(orangeLegBets);
        legBets.add(yellowLegBets);
        legBets.add(whiteLegBets);

        legBettingArea.add(R.id.legbetting_blue);
        legBettingArea.add(R.id.legbetting_green);
        legBettingArea.add(R.id.legbetting_orange);
        legBettingArea.add(R.id.legbetting_yellow);
        legBettingArea.add(R.id.legbetting_white);

        for (Integer raceBet: legBettingArea) {
            (getActivity().findViewById(raceBet)).setOnClickListener(this);
        }
    }

    private void initializeRaceBettingArea(){
        RaceBet c1RaceBet = new RaceBet(R.drawable.c1, R.drawable.c1_button);
        RaceBet c2RaceBet = new RaceBet(R.drawable.c2, R.drawable.c2_button);
        RaceBet c3RaceBet = new RaceBet(R.drawable.c3, R.drawable.c3_button);
        RaceBet c4RaceBet = new RaceBet(R.drawable.c4, R.drawable.c4_button);
        RaceBet c5RaceBet = new RaceBet(R.drawable.c5, R.drawable.c5_button);
        RaceBet c6RaceBet = new RaceBet(R.drawable.c6, R.drawable.c6_button);
        RaceBet c7RaceBet = new RaceBet(R.drawable.c7, R.drawable.c7_button);
        RaceBet c8RaceBet = new RaceBet(R.drawable.c8, R.drawable.c8_button);

        c1RaceBet.add(R.drawable.c1_racebettingcard_blue);
        c1RaceBet.add(R.drawable.c1_racebettingcard_green);
        c1RaceBet.add(R.drawable.c1_racebettingcard_orange);
        c1RaceBet.add(R.drawable.c1_racebettingcard_yellow);
        c1RaceBet.add(R.drawable.c1_racebettingcard_white);

        c2RaceBet.add(R.drawable.c2_racebettingcard_blue);
        c2RaceBet.add(R.drawable.c2_racebettingcard_green);
        c2RaceBet.add(R.drawable.c2_racebettingcard_orange);
        c2RaceBet.add(R.drawable.c2_racebettingcard_yellow);
        c2RaceBet.add(R.drawable.c2_racebettingcard_white);

        c3RaceBet.add(R.drawable.c3_racebettingcard_blue);
        c3RaceBet.add(R.drawable.c3_racebettingcard_green);
        c3RaceBet.add(R.drawable.c3_racebettingcard_orange);
        c3RaceBet.add(R.drawable.c3_racebettingcard_yellow);
        c3RaceBet.add(R.drawable.c3_racebettingcard_white);

        c4RaceBet.add(R.drawable.c4_racebettingcard_blue);
        c4RaceBet.add(R.drawable.c4_racebettingcard_green);
        c4RaceBet.add(R.drawable.c4_racebettingcard_orange);
        c4RaceBet.add(R.drawable.c4_racebettingcard_yellow);
        c4RaceBet.add(R.drawable.c4_racebettingcard_white);

        c5RaceBet.add(R.drawable.c5_racebettingcard_blue);
        c5RaceBet.add(R.drawable.c5_racebettingcard_green);
        c5RaceBet.add(R.drawable.c5_racebettingcard_orange);
        c5RaceBet.add(R.drawable.c5_racebettingcard_yellow);
        c5RaceBet.add(R.drawable.c5_racebettingcard_white);

        c6RaceBet.add(R.drawable.c6_racebettingcard_blue);
        c6RaceBet.add(R.drawable.c6_racebettingcard_green);
        c6RaceBet.add(R.drawable.c6_racebettingcard_orange);
        c6RaceBet.add(R.drawable.c6_racebettingcard_yellow);
        c6RaceBet.add(R.drawable.c6_racebettingcard_white);

        c7RaceBet.add(R.drawable.c7_racebettingcard_blue);
        c7RaceBet.add(R.drawable.c7_racebettingcard_green);
        c7RaceBet.add(R.drawable.c7_racebettingcard_orange);
        c7RaceBet.add(R.drawable.c7_racebettingcard_yellow);
        c7RaceBet.add(R.drawable.c7_racebettingcard_white);

        c8RaceBet.add(R.drawable.c8_racebettingcard_blue);
        c8RaceBet.add(R.drawable.c8_racebettingcard_green);
        c8RaceBet.add(R.drawable.c8_racebettingcard_orange);
        c8RaceBet.add(R.drawable.c8_racebettingcard_yellow);
        c8RaceBet.add(R.drawable.c8_racebettingcard_white);

        raceBets.add(c1RaceBet);
        raceBets.add(c2RaceBet);
        raceBets.add(c3RaceBet);
        raceBets.add(c4RaceBet);
        raceBets.add(c5RaceBet);
        raceBets.add(c6RaceBet);
        raceBets.add(c7RaceBet);

        raceBettingArea.add(R.id.winner_betting);
        raceBettingArea.add(R.id.loser_betting);

        for (Integer raceBet : raceBettingArea) {
            (getActivity().findViewById(raceBet)).setOnClickListener(this);
        }
    }

    /**
     * Adds the dice button (pyramid) to the OnClickListener
     * Adds all dices to the ArrayList dices
     */
    private void initializeDiceArea(){
        Dice blueDices = new Dice();
        Dice greenDices = new Dice();
        Dice orangeDices = new Dice();
        Dice yellowDices = new Dice();
        Dice whiteDices = new Dice();

        blueDices.add(R.drawable.roll_dice_0_blue);
        blueDices.add(R.drawable.roll_dice_1_blue);
        blueDices.add(R.drawable.roll_dice_2_blue);
        blueDices.add(R.drawable.roll_dice_3_blue);

        greenDices.add(R.drawable.roll_dice_0_green);
        greenDices.add(R.drawable.roll_dice_1_green);
        greenDices.add(R.drawable.roll_dice_2_green);
        greenDices.add(R.drawable.roll_dice_3_green);

        orangeDices.add(R.drawable.roll_dice_0_orange);
        orangeDices.add(R.drawable.roll_dice_1_orange);
        orangeDices.add(R.drawable.roll_dice_2_orange);
        orangeDices.add(R.drawable.roll_dice_3_orange);

        yellowDices.add(R.drawable.roll_dice_0_yellow);
        yellowDices.add(R.drawable.roll_dice_1_yellow);
        yellowDices.add(R.drawable.roll_dice_2_yellow);
        yellowDices.add(R.drawable.roll_dice_3_yellow);

        whiteDices.add(R.drawable.roll_dice_0_white);
        whiteDices.add(R.drawable.roll_dice_1_white);
        whiteDices.add(R.drawable.roll_dice_2_white);
        whiteDices.add(R.drawable.roll_dice_3_white);

        dices.add(blueDices);
        dices.add(greenDices);
        dices.add(orangeDices);
        dices.add(yellowDices);
        dices.add(whiteDices);

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
                // TODO: set the dices ArrayList accoring to the values
                /* dices.get(COLOR_ID).setDicePointer(position);
                * where:
                 * - COLOR_ID = {0 .. 4} (blue - white)
                *  - position = {0 .. 3} (?, 1, 2, 3)
                *
                *  e.g.: dices.get(Colors.BLUE.ordinal()).setDicePointer(2);
                * */
            }

            @Override
            public void failure(RetrofitError error) {
                // TODO: handle failure
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
