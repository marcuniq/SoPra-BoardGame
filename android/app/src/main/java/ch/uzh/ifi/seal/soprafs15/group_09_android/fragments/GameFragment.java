package ch.uzh.ifi.seal.soprafs15.group_09_android.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ch.uzh.ifi.seal.soprafs15.group_09_android.R;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.AbstractArea;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.AreaName;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.DiceArea;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.LegBettingArea;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.Move;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.RaceBettingArea;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.RaceTrack;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.beans.DiceAreaBean;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.beans.DieBean;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.events.AbstractPusherEvent;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.events.MoveEvent;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.events.PushEventNameEnum;
import ch.uzh.ifi.seal.soprafs15.group_09_android.service.AreaService;
import ch.uzh.ifi.seal.soprafs15.group_09_android.service.AreaUpdateSubscriber;
import ch.uzh.ifi.seal.soprafs15.group_09_android.service.PusherEventSubscriber;
import ch.uzh.ifi.seal.soprafs15.group_09_android.service.PusherService;
import ch.uzh.ifi.seal.soprafs15.group_09_android.service.RestService;
import ch.uzh.ifi.seal.soprafs15.group_09_android.utils.Dice;
import ch.uzh.ifi.seal.soprafs15.group_09_android.utils.GameColors;
import ch.uzh.ifi.seal.soprafs15.group_09_android.utils.InteractionTile;
import ch.uzh.ifi.seal.soprafs15.group_09_android.utils.LegBet;
import ch.uzh.ifi.seal.soprafs15.group_09_android.utils.Moves;
import ch.uzh.ifi.seal.soprafs15.group_09_android.utils.Popup;
import ch.uzh.ifi.seal.soprafs15.group_09_android.utils.RaceBet;
import ch.uzh.ifi.seal.soprafs15.group_09_android.utils.RaceTrackField;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

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
    private Long playerId = 0L; // TODO: set correct player id
    private Long gameId;
    private String token;
    private GameColors cardColor;
    private PopupWindow popupWindow;
    private Button acceptButton;
    private Boolean fastMode = false;

    private View anchorView;
    private View popupView;
    private ImageView modifiedButton;
    private Drawable lastResource;
    private Popup POPUPTYPE;

    private ArrayList<Integer> playerCharacterCards = new ArrayList<>();

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
        fastMode = b.getBoolean("fastMode");
        token = b.getString("token");

        // Playing rules
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
        initializePlayerCharacterCards();

        subscribeToAreaUpdates();
        subscribeToEvents();

        if (fastMode) {
            playFastMode();
        }
        else {
            play();
        }
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
                displayPopup(v, R.layout.popup_interaction_tile, Popup.PLACE_TILE, 0);
                return;
            }
        }
        for (Integer legBet: legBettingArea) {
            if (legBet == v.getId()) {
                LegBet legBetObject = legBets.get(legBettingArea.indexOf(legBet));
                if (legBetObject.getCurrentLegBet() != R.drawable.empty_image) {
                    displayPopup(v, R.layout.popup_leg_betting, Popup.LEGBET, legBettingArea.indexOf(legBet));
                }
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
     */
    public void displayPopup(View button, int layout, Popup enumPopup, int index) {
        anchorView = button;
        POPUPTYPE = enumPopup;
        popupView = getLayoutInflater(savedInstanceState).inflate(layout, container, false);
        popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(anchorView, Gravity.CENTER_HORIZONTAL, 0, 0);

        switch (POPUPTYPE){
            case ROLL_DICE:
                initPopupRollDice(popupView);
                break;
            case LEGBET:
                initPopupLegBet(popupView, index);
                break;
            case RACEBET:
                initPopupRaceBet(popupView, index);
                break;
            case PLACE_TILE:
                initPopupPlaceTile(popupView);
                break;
            case ROUND:
                // roundFinished()
                break;
            default:
                // do something meaningful
        }

        acceptButton = (Button) popupView.findViewById(R.id.accept);
        Button rejectButton = (Button) popupView.findViewById(R.id.reject);

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (POPUPTYPE){
                    case ROLL_DICE:
                        initiateGameMove(Moves.DICE_ROLLING, null, null, false, 0);
                        break;
                    case LEGBET:
                        break;
                    case RACEBET:
                        break;
                    case PLACE_TILE:
                        break;
                    default:
                        // do something meaningful
                }
                popupWindow.dismiss();
            }
        });
        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (POPUPTYPE) {
                    case ROLL_DICE:
                        break;
                    case LEGBET:
                        LegBet legBet = legBets.get(cardColor.ordinal());
                        int pointer = legBet.getLegBetPointer();
                        legBet.setLegBetPointer(pointer - 1);
                        modifiedButton.setImageDrawable(lastResource);
                        break;
                    case RACEBET:
                        modifiedButton.setImageDrawable(lastResource);
                        break;
                    case PLACE_TILE:
                        if (modifiedButton != null)
                            ((RelativeLayout) modifiedButton.getParent()).removeView(modifiedButton);
                        break;
                    default:
                        // do something meaningful
                }
                popupWindow.dismiss();
            }
        });
    }

    private void initPopupPlaceTile(View popupView){
        ImageButton desertTile = (ImageButton) popupView.findViewById(R.id.desert_tile);
        ImageButton oasisTile = (ImageButton) popupView.findViewById(R.id.oasis_tile);

        desertTile.setImageResource(interactionTiles.get(playerId.intValue()).getDesert());
        oasisTile.setImageResource(interactionTiles.get(playerId.intValue()).getOasis());

        desertTile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout fieldLayout = (RelativeLayout) anchorView;
                acceptButton.setText(R.string.button_text_desert);
                modifiedButton = createDynamicImage(0,
                        interactionTiles.get(playerId.intValue()).getDesert(),
                        RelativeLayout.CENTER_HORIZONTAL,
                        RelativeLayout.CENTER_VERTICAL);
                ViewGroup.LayoutParams params = modifiedButton.getLayoutParams();
                params.width = 75;
                params.height = 75;
                fieldLayout.addView(modifiedButton);
            }
        });
        oasisTile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout fieldLayout = (RelativeLayout) anchorView;
                acceptButton.setText(R.string.button_text_oasis);
                modifiedButton = createDynamicImage(0,
                        interactionTiles.get(playerId.intValue()).getOasis(),
                        RelativeLayout.CENTER_HORIZONTAL,
                        RelativeLayout.CENTER_VERTICAL);
                ViewGroup.LayoutParams params = modifiedButton.getLayoutParams();
                params.width = 75;
                params.height = 75;
                fieldLayout.addView(modifiedButton);
            }
        });
    }

    private void initPopupRaceBet(View popupView, int type) {
        TextView title = (TextView) popupView.findViewById(R.id.popupTitle);
        if (type == 0) title.setText(R.string.title_raceBet_tolle);
        else if (type == 1) title.setText(R.string.title_raceBet_olle);

        modifiedButton = (ImageView) anchorView.findViewById(anchorView.getId());
        lastResource = modifiedButton.getDrawable();

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

        cardColor = null; // because card color is outside function accessible we need to set it to null every time

        cardBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardColor = GameColors.BLUE;
                acceptButton.setText(R.string.button_text_blue);
                modifiedButton.setImageResource(playerCharacterCards.get(playerId.intValue()));
            }
        });
        cardGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardColor = GameColors.GREEN;
                acceptButton.setText(R.string.button_text_green);
                modifiedButton.setImageResource(playerCharacterCards.get(playerId.intValue()));
            }
        });
        cardOrange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardColor = GameColors.ORANGE;
                acceptButton.setText(R.string.button_text_orange);
                modifiedButton.setImageResource(playerCharacterCards.get(playerId.intValue()));
            }
        });
        cardYellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardColor = GameColors.YELLOW;
                acceptButton.setText(R.string.button_text_yellow);
                modifiedButton.setImageResource(playerCharacterCards.get(playerId.intValue()));
            }
        });
        cardWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardColor = GameColors.WHITE;
                acceptButton.setText(R.string.button_text_white);
                modifiedButton.setImageResource(playerCharacterCards.get(playerId.intValue()));
            }
        });
    }

    private void initPopupLegBet(View popupView, int color) {
        cardColor = GameColors.values()[color];
        LegBet legBet = legBets.get(color);

        modifiedButton = (ImageView) anchorView.findViewById(anchorView.getId());
        lastResource = modifiedButton.getDrawable();

        ImageView card = (ImageView) popupView.findViewById(R.id.card);
        card.setImageResource(legBet.getCurrentLegBet());
        int pointer = legBet.getLegBetPointer();
        legBet.setLegBetPointer(pointer + 1);
        modifiedButton.setImageResource(legBet.getCurrentLegBetButton());
    }

    /**
     * Displays a popup for the playing rules.
     *
     * @param layout
     */
    public void instructionsPopup(View v, int layout) {
        View popupView = getLayoutInflater(savedInstanceState).inflate(layout, container, false);
        popupWindow = new PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);

        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(v, Gravity.CENTER_HORIZONTAL, 0, 0);
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
                .setTitle("We have a message for you:");
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) { }
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
     * Loads the player character cards in an array list
     */
    private void initializePlayerCharacterCards() {
        playerCharacterCards.add(R.drawable.c1_button);
        playerCharacterCards.add(R.drawable.c2_button);
        playerCharacterCards.add(R.drawable.c3_button);
        playerCharacterCards.add(R.drawable.c4_button);
        playerCharacterCards.add(R.drawable.c5_button);
        playerCharacterCards.add(R.drawable.c6_button);
        playerCharacterCards.add(R.drawable.c7_button);
        playerCharacterCards.add(R.drawable.c8_button);
        playerCharacterCards.add(R.drawable.empty_image);
    }

    /**
     * This is the main method for the fast mode.
     */
    private void playFastMode(){
        // TODO: implement fast mode
    }

    /**
     * This is the main method. After each player has finished his turn, the whole board is redraw.
     */
    private void play(){
        // TODO: on PUSH from SERVER; get all new information.
        // -> see subscribeToAreaUpdates() and put code there for what to do on an update of areas

//        gameMoves();
//        gameRaceTrack();
//        gameLegBettingArea();
//        gameRaceBettingArea();
//        gameDiceArea();
        addItemsToRaceTrack();

        // TODO: check if player is on his turn; then enable interaction
    }

    private void initiateGameMove(Moves moveType, String legBettingTileColor, String raceBettingOnWinner, Boolean desertTileAsOasis, Integer desertTilePosition) {
        Move move = Move.create(token, moveType, legBettingTileColor, raceBettingOnWinner, desertTileAsOasis, desertTilePosition);

        RestService.getInstance(getActivity()).initiateGameMove(gameId, move, new Callback<Move>() {
            @Override
            public void success(Move move, Response response) {
                gameDiceArea();
            }

            @Override
            public void failure(RetrofitError error) {
                AlertDialog dialog = dummyPopup("failure: " + error.toString());
                dialog.show();
            }
        });
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
     * @api  http://docs.sopra.apiary.io/#reference/games/game-dice-area/retrieve-dice-area
     */
    private void gameDiceArea() {
        RestService.getInstance(getActivity()).getDiceArea(gameId, new Callback<DiceAreaBean>() {

            @Override
            public void success(DiceAreaBean diceArea, Response response) {
                String message = "";

                for (DieBean dice: diceArea.rolledDice()){
                    message += "Dice " + dice.color() + ": " + dice.faceValue() + "\n";
                    dices.get(dice.color().ordinal()).setDicePointer(dice.faceValue());
                }

                AlertDialog dialog = dummyPopup(message);
                dialog.show();
            }

            @Override
            public void failure(RetrofitError error) {
                AlertDialog dialog = dummyPopup("success: " + error.toString());
                dialog.show();
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

    /**
     * Called when creating this fragment
     *
     * Server pushes event to players -> PusherService gets it and notifies all subscribers to that
     * event -> AreaService is per default registered to all events and notifies all AreaUpdateSubscriber
     * (see PusherService.registerAreaServiceAsSubscriber())
     */
    private void subscribeToAreaUpdates(){
        AreaService.getInstance(getActivity()).addSubscriber(AreaName.DICE_AREA, new AreaUpdateSubscriber() {
            @Override
            public void onUpdate(AbstractArea area) {
                DiceArea diceArea1 = (DiceArea) area;

                // TODO update view


            }

            @Override
            public void onError(String errorMessage) {
                // TODO show error message?
            }
        });

        AreaService.getInstance(getActivity()).addSubscriber(AreaName.LEG_BETTING_AREA, new AreaUpdateSubscriber() {
            @Override
            public void onUpdate(AbstractArea area) {
                LegBettingArea legBettingArea1 = (LegBettingArea) area;

                // TODO update view


            }

            @Override
            public void onError(String errorMessage) {
                // TODO show error message?
            }
        });

        AreaService.getInstance(getActivity()).addSubscriber(AreaName.RACE_BETTING_AREA, new AreaUpdateSubscriber() {
            @Override
            public void onUpdate(AbstractArea area) {
                RaceBettingArea raceBettingArea1 = (RaceBettingArea) area;

                // TODO update view


            }

            @Override
            public void onError(String errorMessage) {
                // TODO show error message?
            }
        });

        AreaService.getInstance(getActivity()).addSubscriber(AreaName.RACE_TRACK, new AreaUpdateSubscriber() {
            @Override
            public void onUpdate(AbstractArea area) {
                RaceTrack raceTrack1 = (RaceTrack) area;

                // TODO update view


            }

            @Override
            public void onError(String errorMessage) {
                // TODO show error message?
            }
        });
    }

    private void subscribeToEvents(){
        // for demonstration purposes
        // subscribe to move event and display id
        PusherService.getInstance(getActivity()).addSubscriber(PushEventNameEnum.MOVE_EVENT,
                new PusherEventSubscriber() {
                    @Override
                    public void onNewEvent(final AbstractPusherEvent moveEvent) {
                        System.out.println("got new event");

                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getActivity(), "new move event: " +
                                        ((MoveEvent) moveEvent).getMoveId(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
    }

}
