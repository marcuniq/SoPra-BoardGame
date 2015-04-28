package ch.uzh.ifi.seal.soprafs15.group_09_android.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.beans.RaceTrackBean;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.beans.RaceTrackObjectBean;
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

    private ArrayList<Integer> raceTrack = new ArrayList<>();
    private ArrayList<Integer> legBettingArea = new ArrayList<>();
    private ArrayList<Integer> raceBettingArea = new ArrayList<>();
    private ArrayList<Integer> camels = new ArrayList<>();
    private ArrayList<InteractionTile> interactionTiles = new ArrayList<>();
    private ArrayList<Dice> dices = new ArrayList<>();
    private ArrayList<LegBet> legBets = new ArrayList<>();
    private ArrayList<RaceBet> raceBets = new ArrayList<>();

    private Bundle savedInstanceState;
    private ViewGroup container;
    private Long playerId = 1L; // TODO: set correct player id
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
    private Boolean isRaceBettingOnWinner = null;
    private Boolean isDesertTileAsOasis = null;

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

        subscribeToAreaUpdates();
        subscribeToEvents();

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
                        initiateGameMove(Moves.DICE_ROLLING, null, null, null, null);
                        break;
                    case LEGBET:
                        if (cardColor != null ) {
                            initiateGameMove(Moves.LEG_BETTING, cardColor, null, null, null);
                        }
                        break;
                    case RACEBET:
                        if (cardColor != null ){
                            initiateGameMove(Moves.RACE_BETTING, cardColor, isRaceBettingOnWinner, null, null);
                        }
                        break;
                    case PLACE_TILE:
                        if (isDesertTileAsOasis != null) {
                            for (RaceTrackField field : raceTrack) {
                                if (anchorView.getId() == field.getPosition()) {
                                    initiateGameMove(Moves.DESERT_TILE_PLACING, null, null, isDesertTileAsOasis, raceTrack.indexOf(field));
                                }
                            }
                        }
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
                        if (modifiedButton != null) ((RelativeLayout) modifiedButton.getParent()).removeView(modifiedButton);
                        break;
                    default:
                        // do something meaningful
                }
                popupWindow.dismiss();
            }
        });
    }

    private void initPopupPlaceTile(View popupView){
        modifiedButton = null;
        ImageButton desertTile = (ImageButton) popupView.findViewById(R.id.desert_tile);
        ImageButton oasisTile = (ImageButton) popupView.findViewById(R.id.oasis_tile);

        String desertImageName = "c" + playerId.toString() + "_desert";
        Toast.makeText(popupView.getContext(), "the image is: " + desertImageName, Toast.LENGTH_LONG).show();

        int desertRID = getActivity().getResources().getIdentifier(desertImageName, "drawable", getActivity().getPackageName());
        desertTile.setImageResource(desertRID);
        //desertTile.setImageResource(interactionTiles.get(playerId.intValue()).getDesert());
        oasisTile.setImageResource(interactionTiles.get(playerId.intValue()).getOasis());

        isDesertTileAsOasis = null;

        desertTile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (modifiedButton != null) ((RelativeLayout) modifiedButton.getParent()).removeView(modifiedButton);
                isDesertTileAsOasis = true;
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
                if (modifiedButton != null) ((RelativeLayout) modifiedButton.getParent()).removeView(modifiedButton);
                isDesertTileAsOasis = false;
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
        isRaceBettingOnWinner = (type != 0); // cast the type {0 or 1} to boolean

        if (isRaceBettingOnWinner) title.setText(R.string.title_raceBet_tolle);
        else title.setText(R.string.title_raceBet_olle);

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
        int fieldRID;
        for (int i = 1; i<=16; i++){
            fieldRID = getActivity().getResources().getIdentifier("field" + i, "id", getActivity().getPackageName());
            (getActivity().findViewById(fieldRID)).setOnClickListener(this);
        }
    }

    /**
     * Adds all legBettingAreas to an ArrayList
     * Adds all legBettingAreas to the OnClickListener
     */
    private void initializeLegBettingArea(){
        int fieldRID;
        for (GameColors color: GameColors.values()) {
            fieldRID = getActivity().getResources().getIdentifier("legbetting_" + color.name().toLowerCase(), "id", getActivity().getPackageName());
            (getActivity().findViewById(fieldRID)).setOnClickListener(this);
        }
    }

    private void initializeRaceBettingArea(){
        (getActivity().findViewById(R.id.winner_betting)).setOnClickListener(this);
        (getActivity().findViewById(R.id.loser_betting)).setOnClickListener(this);
    }

    /**
     * Adds the dice button (pyramid) to the OnClickListener
     * Adds all dices to the ArrayList dices
     */
    private void initializeDiceArea(){
        (getActivity().findViewById(R.id.dice)).setOnClickListener(this);
    }

    /**
     * This is the main method. After each player has finished his turn, the whole board is redraw.
     */
    private void play(){
        gameRaceTrack();
        // TODO: on PUSH from SERVER; get all new information.
        // -> see subscribeToAreaUpdates() and put code there for what to do on an update of areas

//        gameMoves();
//        gameRaceTrack();
//        gameLegBettingArea();
//        gameRaceBettingArea();
        gameDiceArea();
        addItemsToRaceTrack();
    }

    private void initiateGameMove(Moves moveType, GameColors legBettingTileColor, Boolean raceBettingOnWinner, Boolean desertTileAsOasis, Integer desertTilePosition) {
        Move move = Move.create(token, moveType, legBettingTileColor, raceBettingOnWinner, desertTileAsOasis, desertTilePosition);

        RestService.getInstance(getActivity()).initiateGameMove(gameId, move, new Callback<Move>() {
            @Override
            public void success(Move move, Response response) {
                AlertDialog dialog = dummyPopup("success: " + response.toString());
                dialog.show();
            }

            @Override
            public void failure(RetrofitError error) {
                AlertDialog dialog = dummyPopup("failure: " + error.toString());
                dialog.show();
            }
        });
    }

    /** transforms the RaceTrackBean into an ArrayList of RaceTrackField Objects
     * maybe we should change this .. it sounds really stupid
     * @api  http://docs.sopra.apiary.io/#reference/games/game-race-track/retrieve-race-track
     */
    private void gameRaceTrack() {
        RestService.getInstance(getActivity()).getRacetrack(gameId, new Callback<RaceTrackBean>() {

            @Override
            public void success(RaceTrackBean newRaceTrack, Response response) {
                for (RaceTrackObjectBean newField : newRaceTrack.fields()){
                    RaceTrackField field = raceTrack.get(newRaceTrack.fields().indexOf(newField));
                    if (newField.isOasis() == null) {
                        ArrayList<Integer> camelStack = new ArrayList<>();
                        for (GameColors camelColor : newField.stack()){
                            camelStack.add(camels.get(camelColor.ordinal()));
                        }
                        field.setCamels(camelStack);
                    }
                    else if (newField.isOasis()){
                        field.setOasis(interactionTiles.get(newField.playerId().intValue()).getOasis());
                    } else if (!newField.isOasis()){
                        field.setDesert(interactionTiles.get(newField.playerId().intValue()).getDesert());
                    }
                }
                AlertDialog dialog = dummyPopup("success: " + response.toString());
                dialog.show();
            }

            @Override
            public void failure(RetrofitError error) {
                AlertDialog dialog = dummyPopup("failure: " + error.toString());
                dialog.show();
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
                for (DieBean dice: diceArea.rolledDice()){
                    dices.get(dice.color().ordinal()).setDicePointer(dice.faceValue());
                }

                AlertDialog dialog = dummyPopup("success: " + response.toString());
                dialog.show();
            }

            @Override
            public void failure(RetrofitError error) {
                AlertDialog dialog = dummyPopup("failure: " + error.toString());
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
