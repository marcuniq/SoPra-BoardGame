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
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.Move;
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
import ch.uzh.ifi.seal.soprafs15.group_09_android.utils.GameColors;
import ch.uzh.ifi.seal.soprafs15.group_09_android.utils.InteractionTile;
import ch.uzh.ifi.seal.soprafs15.group_09_android.utils.LegBet;
import ch.uzh.ifi.seal.soprafs15.group_09_android.utils.Moves;
import ch.uzh.ifi.seal.soprafs15.group_09_android.utils.Popup;
import ch.uzh.ifi.seal.soprafs15.group_09_android.utils.RaceTrackField;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class GameFragment extends Fragment implements View.OnClickListener {

    private DiceAreaBean diceArea;
    private RaceTrackBean raceTrack;
    private ArrayList<Integer> raceTrackFields = new ArrayList<>();
    private ArrayList<Integer> legBettingFields = new ArrayList<>();
    private ArrayList<Integer> raceBettingFields = new ArrayList<>();
    private Integer pyramidField;
    private Integer helpButton;

    private Bundle savedInstanceState;
    private ViewGroup container;
    private Long playerId = 1L; // TODO: set correct player id
    private Long gameId;
    private String token;
    private GameColors pickedCardColor;
    private PopupWindow popupWindow;
    private Button acceptButton;
    private Button rejectButton;

    private View anchorView;
    private ImageView modifiedButton;
    private Drawable lastResource;
    private Integer lastResourceId;
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
        token = b.getString("token");
        return v;
    }

    @Override
    public void onResume(){
        super.onResume();

        addClickListenerToButtons();
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
        for (Integer button: raceTrackFields) {
            if (button == v.getId()) {
                interactionTilePopup(v, R.layout.popup_interaction_tile, Moves.DESERT_TILE_PLACING);
                return;
            }
        }
        for (Integer button: legBettingFields) {
            if (button == v.getId()) {
                legBettingPopup(v, R.layout.popup_leg_betting, Moves.LEG_BETTING, legBettingFields.indexOf(button));
                return;
            }
        }
        for (Integer button : raceBettingFields) {
            if (button == v.getId()) {
                raceBettingPopup(v, R.layout.popup_race_betting, Moves.RACE_BETTING, raceBettingFields.indexOf(button));
                return;
            }
        }
        if (pyramidField == v.getId()){
            rollDicePopup(v, R.layout.popup_roll_dice, Moves.DICE_ROLLING);
        }
        if (helpButton == v.getId()){
            instructionsPopup(v, R.layout.popup_instructions);
        }
    }

    private void rollDicePopup(View v, int popup_roll_dice, final Moves diceRolling) {
        View popupView = defaultPopup(popup_roll_dice);

        ImageView blueDice = (ImageView) popupView.findViewById(R.id.dice_blue);
        ImageView greenDice = (ImageView) popupView.findViewById(R.id.dice_green);
        ImageView orangeDice = (ImageView) popupView.findViewById(R.id.dice_orange);
        ImageView yellowDice = (ImageView) popupView.findViewById(R.id.dice_yellow);
        ImageView whiteDice = (ImageView) popupView.findViewById(R.id.dice_white);

        List<DieBean> dices = diceArea.rolledDice();
        String diceImageName = "rolled_dice_";
        final int diceBlueDrawableId = getActivity().getResources().getIdentifier(diceImageName + dices.get(GameColors.BLUE.ordinal()).faceValue() + "_blue", "drawable", getActivity().getPackageName());
        final int diceGreenDrawableId = getActivity().getResources().getIdentifier(diceImageName + dices.get(GameColors.GREEN.ordinal()).faceValue() + "_green", "drawable", getActivity().getPackageName());
        final int diceOrangeDrawableId = getActivity().getResources().getIdentifier(diceImageName + dices.get(GameColors.ORANGE.ordinal()).faceValue() + "_orange", "drawable", getActivity().getPackageName());
        final int diceYellowDrawableId = getActivity().getResources().getIdentifier(diceImageName + dices.get(GameColors.YELLOW.ordinal()).faceValue() + "_yellow", "drawable", getActivity().getPackageName());
        final int diceWhiteDrawableId = getActivity().getResources().getIdentifier(diceImageName + dices.get(GameColors.WHITE.ordinal()).faceValue() + "_white", "drawable", getActivity().getPackageName());

        blueDice.setImageResource(diceBlueDrawableId);
        greenDice.setImageResource(diceGreenDrawableId);
        orangeDice.setImageResource(diceOrangeDrawableId);
        yellowDice.setImageResource(diceYellowDrawableId);
        whiteDice.setImageResource(diceWhiteDrawableId);

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiateGameMove(diceRolling, null, null, null, null);
            }
        });

        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    private void raceBettingPopup(View v, int popup_race_betting, final Moves raceBetting, int type) {
        isRaceBettingOnWinner = (type != 0); // cast the type {0 or 1} to boolean
        modifiedButton = (ImageView) v.findViewById(v.getId());
        lastResource = modifiedButton.getDrawable();
        pickedCardColor = null; // because card color is outside function accessible we need to set it to null every time

        View popupView = defaultPopup(popup_race_betting);
        TextView title = (TextView) popupView.findViewById(R.id.popupTitle);

        // check if it is olle or tolle betting
        if (isRaceBettingOnWinner) title.setText(R.string.title_raceBet_tolle);
        else title.setText(R.string.title_raceBet_olle);

        ImageButton cardBlue = (ImageButton) popupView.findViewById(R.id.card_blue);
        ImageButton cardGreen = (ImageButton) popupView.findViewById(R.id.card_green);
        ImageButton cardOrange = (ImageButton) popupView.findViewById(R.id.card_orange);
        ImageButton cardYellow = (ImageButton) popupView.findViewById(R.id.card_yellow);
        ImageButton cardWhite = (ImageButton) popupView.findViewById(R.id.card_white);

        String cardImageName = "c" + playerId + "_racebettingcard_";
        String characterCardImageName = "c" + playerId + "_button";
        final int cardBlueDrawableId = getActivity().getResources().getIdentifier(cardImageName + "blue", "drawable", getActivity().getPackageName());
        final int cardGreenDrawableId = getActivity().getResources().getIdentifier(cardImageName + "green", "drawable", getActivity().getPackageName());
        final int cardOrangeDrawableId = getActivity().getResources().getIdentifier(cardImageName + "orange", "drawable", getActivity().getPackageName());
        final int cardYellowDrawableId = getActivity().getResources().getIdentifier(cardImageName + "yellow", "drawable", getActivity().getPackageName());
        final int cardWhiteDrawableId = getActivity().getResources().getIdentifier(cardImageName + "white", "drawable", getActivity().getPackageName());
        final int characterCardDrawableId = getActivity().getResources().getIdentifier(characterCardImageName, "drawable", getActivity().getPackageName());

        cardBlue.setImageResource(cardBlueDrawableId);
        cardGreen.setImageResource(cardGreenDrawableId);
        cardOrange.setImageResource(cardOrangeDrawableId);
        cardYellow.setImageResource(cardYellowDrawableId);
        cardWhite.setImageResource(cardWhiteDrawableId);

        cardBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickedCardColor = GameColors.BLUE;
                acceptButton.setText(R.string.button_text_blue);
                modifiedButton.setImageResource(characterCardDrawableId);
            }
        });
        cardGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickedCardColor = GameColors.GREEN;
                acceptButton.setText(R.string.button_text_green);
                modifiedButton.setImageResource(characterCardDrawableId);
            }
        });
        cardOrange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickedCardColor = GameColors.ORANGE;
                acceptButton.setText(R.string.button_text_orange);
                modifiedButton.setImageResource(characterCardDrawableId);
            }
        });
        cardYellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickedCardColor = GameColors.YELLOW;
                acceptButton.setText(R.string.button_text_yellow);
                modifiedButton.setImageResource(characterCardDrawableId);
            }
        });
        cardWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickedCardColor = GameColors.WHITE;
                acceptButton.setText(R.string.button_text_white);
                modifiedButton.setImageResource(characterCardDrawableId);
            }
        });

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pickedCardColor != null) {
                    initiateGameMove(raceBetting, null, null, null, null);
                    popupWindow.dismiss();
                }
            }
        });

        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifiedButton.setImageDrawable(lastResource);
                popupWindow.dismiss();
            }
        });
    }

    private void legBettingPopup(View v, int popup_leg_betting, final Moves legBetting, int color) {
        pickedCardColor = GameColors.values()[color];
        modifiedButton = (ImageView) v.findViewById(v.getId());
        lastResource = modifiedButton.getDrawable();
        if (lastResource == null) return; // forbid picking "empty" card
        lastResourceId = modifiedButton.getId();

        View popupView = defaultPopup(popup_leg_betting);
        ImageView card = (ImageView) popupView.findViewById(R.id.card);
        card.setImageDrawable(lastResource);

        // build the next card name on the base of the current
        String name = getResources().getResourceEntryName(lastResourceId);
        int cardValue = Integer.parseInt(name.substring(name.length()-8,name.length()-7)); // find card number

        // find the id for the next card
        int nextResourceId;
        if (cardValue == 5) nextResourceId = 3;
        else if (cardValue == 3) nextResourceId = 2;
        else nextResourceId = 0;

        // compose the new card drawable id
        String cardImageName = "legbettingtile_" + pickedCardColor.name().toLowerCase() + "_" + nextResourceId + "_button";
        final int cardBlueDrawableId = getActivity().getResources().getIdentifier(cardImageName, "drawable", getActivity().getPackageName());

        // do not set any image when next card value is 0
        if (nextResourceId == 0) modifiedButton.setImageDrawable(null);
        else modifiedButton.setImageResource(cardBlueDrawableId);

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pickedCardColor != null) {
                    initiateGameMove(legBetting, null, null, null, null);
                }
            }
        });

        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifiedButton.setImageDrawable(lastResource);
            }
        });
    }

    private void interactionTilePopup(View v, int popup_interaction_tile, final Moves desertTilePlacing) {
        modifiedButton = null;
        isDesertTileAsOasis = null;

        View popupView = defaultPopup(popup_interaction_tile);
        ImageButton desertTile = (ImageButton) popupView.findViewById(R.id.desert_tile);
        ImageButton oasisTile = (ImageButton) popupView.findViewById(R.id.oasis_tile);

        String desertImageName = "c" + playerId.toString() + "_desert";
        String oasisImageName = "c" + playerId.toString() + "_oasis";
        final int desertDrawableId = getActivity().getResources().getIdentifier(desertImageName, "drawable", getActivity().getPackageName());
        final int oasisDrawableId = getActivity().getResources().getIdentifier(oasisImageName, "drawable", getActivity().getPackageName());
        desertTile.setImageResource(desertDrawableId);
        oasisTile.setImageResource(oasisDrawableId);

        desertTile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (modifiedButton != null) ((RelativeLayout) modifiedButton.getParent()).removeView(modifiedButton);
                isDesertTileAsOasis = true;
                RelativeLayout fieldLayout = (RelativeLayout) anchorView;
                acceptButton.setText(R.string.button_text_desert);
                modifiedButton = createDynamicImage(0,
                        desertDrawableId,
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
                        oasisDrawableId,
                        RelativeLayout.CENTER_HORIZONTAL,
                        RelativeLayout.CENTER_VERTICAL);
                ViewGroup.LayoutParams params = modifiedButton.getLayoutParams();
                params.width = 75;
                params.height = 75;
                fieldLayout.addView(modifiedButton);
            }
        });

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDesertTileAsOasis != null) {
                    for (Integer field : raceTrackFields) {
                        if (anchorView.getId() == field) {
                            initiateGameMove(desertTilePlacing, null, null, isDesertTileAsOasis, raceTrackFields.indexOf(field));
                        }
                    }
                }
            }
        });

        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (modifiedButton != null) ((RelativeLayout) modifiedButton.getParent()).removeView(modifiedButton);
                popupWindow.dismiss();
            }
        });
    }

    /**
     * Displays a Popup with the given layout and draws all the dices
     * Two Options:
     *  - Accept: Execute a Move and close the Popup
     *  - Reject: abort, close Popup
     */
    public View defaultPopup(int layout) {
        View popupView = getLayoutInflater(savedInstanceState).inflate(layout, container, false);
        popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(anchorView, Gravity.CENTER_HORIZONTAL, 0, 0);

        acceptButton = (Button) popupView.findViewById(R.id.accept);
        rejectButton = (Button) popupView.findViewById(R.id.reject);

        return popupView;
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
    private void addClickListenerToButtons(){
        int fieldRID;

        // for all race track fields (buttons)
        for (int i = 1; i<=16; i++){
            fieldRID = getActivity().getResources().getIdentifier("field" + i, "id", getActivity().getPackageName());
            raceTrackFields.add(fieldRID);
            (getActivity().findViewById(fieldRID)).setOnClickListener(this);
        }

        // for all legbetting buttons
        for (GameColors color: GameColors.values()) {
            fieldRID = getActivity().getResources().getIdentifier("legbetting_" + color.name().toLowerCase(), "id", getActivity().getPackageName());
            legBettingFields.add(fieldRID);
            (getActivity().findViewById(fieldRID)).setOnClickListener(this);
        }

        // olle tolle camel betting buttons
        raceBettingFields.add(R.id.winner_betting);
        raceBettingFields.add(R.id.loser_betting);
        (getActivity().findViewById(R.id.winner_betting)).setOnClickListener(this);
        (getActivity().findViewById(R.id.loser_betting)).setOnClickListener(this);

        // pyramid button to roll the dices
        pyramidField = R.id.dice;
        (getActivity().findViewById(pyramidField)).setOnClickListener(this);

        // for the help; displays game rules
        helpButton = R.id.help;
        (getActivity().findViewById(helpButton)).setOnClickListener(this);
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
                raceTrack = newRaceTrack;
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
            public void success(DiceAreaBean newDiceArea, Response response) {
                diceArea = newDiceArea;
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
        int fieldId;
        String imageName;
        int drawableId;
        cleanRaceTrack();

        for (RaceTrackObjectBean field: raceTrack.fields()) {
            List<GameColors> camels = field.stack();
            fieldId = raceTrackFields.get(raceTrack.fields().indexOf(field));
            RelativeLayout fieldLayout = (RelativeLayout) getActivity().findViewById(fieldId);

            if (field.isOasis() == null) {
                for (int pos = 0; pos < camels.size(); pos++){
                    imageName = "camel_" + camels.get(pos).name().toLowerCase();
                    drawableId = getActivity().getResources().getIdentifier(imageName, "drawable", getActivity().getPackageName());
                    fieldLayout.addView(createDynamicImage(margins[pos],
                            drawableId,
                            RelativeLayout.ALIGN_PARENT_BOTTOM,
                            RelativeLayout.ALIGN_PARENT_LEFT));
                }
            } else if (field.isOasis()) {
                imageName = "c" + field.playerId() + "_oasis";
                drawableId = getActivity().getResources().getIdentifier(imageName, "drawable", getActivity().getPackageName());
                fieldLayout.addView(createDynamicImage(0,
                        drawableId,
                        RelativeLayout.CENTER_HORIZONTAL,
                        RelativeLayout.CENTER_VERTICAL));
            } else if (!field.isOasis()) {
                imageName = "c" + field.playerId() + "_desert";
                drawableId = getActivity().getResources().getIdentifier(imageName, "drawable", getActivity().getPackageName());
                fieldLayout.addView(createDynamicImage(0,
                        drawableId,
                        RelativeLayout.CENTER_HORIZONTAL,
                        RelativeLayout.CENTER_VERTICAL));
            }
        }
    }

    /**
     * Removes all views from the racetrack
     */
    private void cleanRaceTrack() {
        RelativeLayout fieldLayout;
        for (Integer field: raceTrackFields) {
            fieldLayout = (RelativeLayout) getActivity().findViewById(field);
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
