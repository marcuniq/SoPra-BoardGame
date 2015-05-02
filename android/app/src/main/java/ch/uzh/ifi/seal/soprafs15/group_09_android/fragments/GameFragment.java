package ch.uzh.ifi.seal.soprafs15.group_09_android.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import ch.uzh.ifi.seal.soprafs15.group_09_android.R;
import ch.uzh.ifi.seal.soprafs15.group_09_android.activities.GameActivity;
import ch.uzh.ifi.seal.soprafs15.group_09_android.activities.MenuActivity;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.*;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.beans.CamelBean;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.beans.RaceTrackObjectBean;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.events.AbstractPusherEvent;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.events.MoveEvent;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.events.PlayerTurnEvent;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.events.PushEventNameEnum;
import ch.uzh.ifi.seal.soprafs15.group_09_android.service.*;
import ch.uzh.ifi.seal.soprafs15.group_09_android.utils.GameColors;
import ch.uzh.ifi.seal.soprafs15.group_09_android.utils.Moves;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GameFragment extends Fragment implements View.OnClickListener {

    // the areas
    private DiceArea diceArea;
    private RaceTrack raceTrack;
    private LegBettingArea legBettingArea;
    private RaceBettingArea raceBettingArea;

    // all buttons in game
    private ArrayList<Integer> raceTrackFieldIds = new ArrayList<>();
    private ArrayList<Integer> legBettingFieldIds = new ArrayList<>();
    private ArrayList<Integer> raceBettingFieldIds = new ArrayList<>();
    private Integer pyramidFieldId;
    private Integer helpButtonId;
    private Integer playerIconId;
    private Button acceptButton;
    private Button rejectButton;
    private ImageView modifiedButton;

    // class variables
    private User player;
    private User currentPlayer;
    private Long userId;
    private Long gameId;
    private Integer playerId;
    private String token;
    private Boolean isOwner = false;

    private PopupWindow popupWindow;
    private View anchorView;
    private Bundle savedInstanceState;
    private ViewGroup container;

    private List<User> usersOrderedByMoney;
    private Drawable lastResource;
    private int currentPyramidTile;
    private Boolean isDesertTileAsOasis = null;
    private GameColors pickedCardColor;

    public static GameFragment newInstance() {
        return new GameFragment();
    }

    public GameFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = getActivity().getIntent().getExtras();
        gameId = b.getLong("gameId");
        userId = b.getLong("userId");
        playerId = b.getInt("playerId");
        isOwner = b.getBoolean("isOwner");

        SharedPreferences sharedPref = getActivity().getSharedPreferences("token", Context.MODE_PRIVATE);
        token = sharedPref.getString("token", token);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.savedInstanceState = savedInstanceState;
        this.container = container;
        return inflater.inflate(R.layout.fragment_game, container, false);
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
        // prevent action from a player if it's not his turn
        if (player.equals(currentPlayer)) {
            for (Integer button : raceTrackFieldIds) {
                if (button == v.getId()) {
                    interactionTilePopup(v, R.layout.popup_interaction_tile, Moves.DESERT_TILE_PLACING);
                    return;
                }
            }
            for (Integer button : legBettingFieldIds) {
                if (button == v.getId()) {
                    legBettingPopup(v, R.layout.popup_leg_betting, Moves.LEG_BETTING, legBettingFieldIds.indexOf(button));
                    return;
                }
            }
            for (Integer button : raceBettingFieldIds) {
                if (button == v.getId()) {
                    raceBettingPopup(v, R.layout.popup_race_betting, Moves.RACE_BETTING, raceBettingFieldIds.indexOf(button));
                    return;
                }
            }
            if (pyramidFieldId == v.getId()) {
                rollDicePopup(v, R.layout.popup_roll_dice, Moves.DICE_ROLLING);
            }
        }
        if (helpButtonId == v.getId()){
            instructionsPopup(v, R.layout.popup_instructions);
        }
        if (playerIconId == v.getId()){
            playerInfoPopup(v, R.layout.popup_player_info);
        }
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
            raceTrackFieldIds.add(fieldRID);
            (getActivity().findViewById(fieldRID)).setOnClickListener(this);
        }

        // for all legbetting buttons
        for (GameColors color: GameColors.values()) {
            fieldRID = getActivity().getResources().getIdentifier("legbetting_" + color.name().toLowerCase(), "id", getActivity().getPackageName());
            legBettingFieldIds.add(fieldRID);
            (getActivity().findViewById(fieldRID)).setOnClickListener(this);
        }

        // olle tolle camel betting buttons
        raceBettingFieldIds.add(R.id.winner_betting);
        raceBettingFieldIds.add(R.id.loser_betting);
        (getActivity().findViewById(R.id.winner_betting)).setOnClickListener(this);
        (getActivity().findViewById(R.id.loser_betting)).setOnClickListener(this);

        // pyramid button to roll the dices
        pyramidFieldId = R.id.dice;
        (getActivity().findViewById(pyramidFieldId)).setOnClickListener(this);

        // for the help; displays game rules
        helpButtonId = R.id.help;
        (getActivity().findViewById(helpButtonId)).setOnClickListener(this);

        // button to display the players rack
        playerIconId = R.id.player_icon;
        (getActivity().findViewById(playerIconId)).setOnClickListener(this);

        currentPyramidTile = 1;
        ImageView pyramidTile = (ImageView) getActivity().findViewById(R.id.pyramid_tile);
        pyramidTile.setImageResource(getActivity().getResources().getIdentifier("pyramid_tile_" + currentPyramidTile + "_button", "drawable", getActivity().getPackageName()));
    }

    /**
     * This is the main method. After each player has finished his turn, the whole board is redraw.
     */
    private void play(){
        AreaService.getInstance(getActivity()).getAreasAndNotifySubscriber(gameId);
        getPlayerStatus();
    }

    /**
     * Displays a Popup with the given layout and draws all the dices
     * Two Options:
     *  - Accept: Execute a Move and close the Popup
     *  - Reject: abort, close Popup
     */
    public View defaultPopup(View v, int layout) {
        anchorView = v;
        View popupView = getLayoutInflater(savedInstanceState).inflate(layout, container, false);
        popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setWidth(getActivity().findViewById(R.id.game_board).getWidth());
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(anchorView, Gravity.CENTER_HORIZONTAL, 0, 0);

        acceptButton = (Button) popupView.findViewById(R.id.accept);
        rejectButton = (Button) popupView.findViewById(R.id.reject);

        return popupView;
    }

    /**
     * Rolls a dice:
     * - get all current face values of the diceAra; if element is null, then add the [?] dice (roll_dice_0_COLOR)
     * - add for all GameColors the dices (ImageView) and add the corresponding image drawable ID from diceNames
     * - add onClick listener for the accept and reject buttons
     *  - accept: initiate a game move (ROLL_DICE)
     *  - reject: dismiss this popup
     *
     * @param v the button that was clicked
     * @param popup_roll_dice the popup's layout that will be shown
     * @param diceRolling the type of MOVE that will be executed on accept
     */
    private void rollDicePopup(View v, int popup_roll_dice, final Moves diceRolling) {
        View popupView = defaultPopup(v,popup_roll_dice);

        String diceNames[] = new String[5];
        String diceImageName = "roll_dice_";
        for (int i = 0; i < 5; i++){
            if (diceArea.getRolledDice().isEmpty() || diceArea.getRolledDice().size() < i + 1) diceNames[i] = "0";
            else diceNames[diceArea.getRolledDice().get(i).color().ordinal()] = diceArea.getRolledDice().get(i).faceValue()+"";
        }

        ImageView dice;
        for (GameColors color : GameColors.values()){
            dice = (ImageView) popupView.findViewById(getActivity().getResources().getIdentifier("dice_" + color.name().toLowerCase(), "id", getActivity().getPackageName()));
            dice.setImageResource(getActivity().getResources().getIdentifier(diceImageName + diceNames[color.ordinal()] + "_" + color.name().toLowerCase(), "drawable", getActivity().getPackageName()));
        }

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPyramidTile += 1;
                String image = "pyramid_tile_" + currentPyramidTile + "_button";
                ImageView pyramidCard = (ImageView) getActivity().findViewById(R.id.pyramid_tile);
                if (currentPyramidTile++ > 5) image = "empty_image";
                pyramidCard.setImageResource(getActivity().getResources().getIdentifier(image, "drawable", getActivity().getPackageName()));
                initiateGameMove(diceRolling, null, null, null, null);
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

    private void raceBettingPopup(View v, int popup_race_betting, final Moves raceBetting, int type) {
        modifiedButton = (ImageView) v.findViewById(v.getId());
        lastResource = modifiedButton.getDrawable();
        pickedCardColor = null; // because card color is outside function accessible we need to set it to null every time

        View popupView = defaultPopup(v, popup_race_betting);
        TextView title = (TextView) popupView.findViewById(R.id.popupTitle);

        // check if it is olle or tolle betting
        if ((type != 0)) title.setText(R.string.title_raceBet_olle); // cast the type {0 or 1} to boolean
        else title.setText(R.string.title_raceBet_tolle);

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
                acceptButton.setVisibility(View.VISIBLE);
                pickedCardColor = GameColors.BLUE;
                acceptButton.setText(R.string.button_text_blue);
                modifiedButton.setImageResource(characterCardDrawableId);
            }
        });
        cardGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptButton.setVisibility(View.VISIBLE);
                pickedCardColor = GameColors.GREEN;
                acceptButton.setText(R.string.button_text_green);
                modifiedButton.setImageResource(characterCardDrawableId);
            }
        });
        cardOrange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptButton.setVisibility(View.VISIBLE);
                pickedCardColor = GameColors.ORANGE;
                acceptButton.setText(R.string.button_text_orange);
                modifiedButton.setImageResource(characterCardDrawableId);
            }
        });
        cardYellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptButton.setVisibility(View.VISIBLE);
                pickedCardColor = GameColors.YELLOW;
                acceptButton.setText(R.string.button_text_yellow);
                modifiedButton.setImageResource(characterCardDrawableId);
            }
        });
        cardWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptButton.setVisibility(View.VISIBLE);
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

        View popupView = defaultPopup(v, popup_leg_betting);
        ImageView card = (ImageView) popupView.findViewById(R.id.card);
        Integer cardValue = legBettingArea.getTopLegBettingTiles().get(GameColors.values()[color].ordinal()).leadingPositionGain();

        String cardImageName = "legbettingtile_" + pickedCardColor.name().toLowerCase() + "_" + cardValue;
        final int cardDrawableId = getActivity().getResources().getIdentifier(cardImageName, "drawable", getActivity().getPackageName());
        card.setImageResource(cardDrawableId);

        // find the id for the next card
        int nextResourceId;
        if (cardValue != null && cardValue == 5) nextResourceId = 3;
        else if (cardValue != null && cardValue == 3) nextResourceId = 2;
        else nextResourceId = 0;

        // compose the new card drawable id
        String newCardImageName = "legbettingtile_" + pickedCardColor.name().toLowerCase() + "_" + nextResourceId + "_button";
        final int newcardDrawableId = getActivity().getResources().getIdentifier(newCardImageName, "drawable", getActivity().getPackageName());

        // do not set any image when next card value is 0
        if (nextResourceId == 0) modifiedButton.setImageResource(R.drawable.empty_image);
        else modifiedButton.setImageResource(newcardDrawableId);

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pickedCardColor != null) {
                    initiateGameMove(legBetting, null, null, null, null);
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

    private void interactionTilePopup(View v, int popup_interaction_tile, final Moves desertTilePlacing) {
        modifiedButton = null;
        isDesertTileAsOasis = null;

        View popupView = defaultPopup(v, popup_interaction_tile);
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
                acceptButton.setVisibility(View.VISIBLE);
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
                acceptButton.setVisibility(View.VISIBLE);
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
                    for (Integer field : raceTrackFieldIds) {
                        if (anchorView.getId() == field) {
                            initiateGameMove(desertTilePlacing, null, null, isDesertTileAsOasis, raceTrackFieldIds.indexOf(field));
                        }
                    }
                    popupWindow.dismiss();
                }
            }
        });

        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (modifiedButton != null)
                    ((RelativeLayout) modifiedButton.getParent()).removeView(modifiedButton);
                popupWindow.dismiss();
            }
        });
    }

    public void playerInfoPopup(View v, int layout) {
        View popupView = defaultPopup(v, layout);
        GridLayout grid = (GridLayout) popupView.findViewById(R.id.grid);
        ArrayList<String> cardNames = new ArrayList<>();

        ImageView image;
        String prefix;

/*      //TODO: THIS PART IS NOT WORKING YET!
        prefix = "c" + playerId + "_racebettingcard_";
        for (RaceBettingCard raceBettingCard : player.raceBettingCards()){
            cardNames.add(prefix + raceBettingCard.color().name().toLowerCase());
        }
        prefix = "legbettingtile_";
        for (LegBettingTile legBettingTile : player.legBettingTiles()){
            cardNames.add(prefix + legBettingTile.color().name().toLowerCase());
        }
        prefix = "pyramid_tile";
        for (PyramidTile pyramidTile : player.pyramidTiles()){
            cardNames.add(prefix + pyramidTile.value());
        }*/

        for (String card : cardNames){
            image = new ImageView(getActivity());
            image.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            image.setImageResource(getActivity().getResources().getIdentifier(card, "drawable", getActivity().getPackageName()));
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT );
            lp.setMargins(4, 4, 4, 4);
            lp.width = 130;
            image.setLayoutParams(lp);
            image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            grid.addView(image);
        }

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
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
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable(
                getActivity().getResources(),
                Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)));
        popupWindow.setWidth(900);
        popupWindow.showAtLocation(getActivity().findViewById(R.id.game_board), Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private void roundEvaluationPopup() {
        View popupView = defaultPopup(getView(), R.layout.popup_round_evaluation);

        TextView description = (TextView) popupView.findViewById(R.id.description);

        String message = "";
        Integer counter = 1;

        for (User user : usersOrderedByMoney) {
            message += counter + ". User " + user.username() + " has " + user.money() + " egypt pounds. \n";
            counter++;
        }

        description.setText(message);

        acceptButton.setText("OK");
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        rejectButton.setVisibility(View.INVISIBLE);
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
            public void onClick(DialogInterface dialog, int id) {
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

    private void initiateGameMove(final Moves moveType, GameColors legBettingTileColor, Boolean raceBettingOnWinner, Boolean desertTileAsOasis, Integer desertTilePosition) {
        Move move = Move.create(token, moveType, legBettingTileColor, raceBettingOnWinner, desertTileAsOasis, desertTilePosition);
        RestService.getInstance(getActivity()).initiateGameMove(gameId, move, new Callback<Move>() {
            @Override
            public void success(Move move, Response response) {
                AlertDialog dialog = dummyPopup("success: " + response.toString() + move.toString());
                dialog.show();

                updateLegBettingFields();
                updateRaceBettingFields();
                updateRaceTrackFields();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), moveType.name() + " Failed: " + error.getMessage(), Toast.LENGTH_LONG).show();

                updateLegBettingFields();
                updateRaceBettingFields();
                updateRaceTrackFields();
            }
        });
    }

    /**
     * Draw the raceTrack according to the game status: all camels, all desert/oasis tiles etc.
     */
    private void updateRaceTrackFields() {
        int[] margins = {0, 10, 20, 30, 40};
        int fieldId;
        String imageName;
        int drawableId;
        clearRaceTrack();

        if (raceTrack == null) return;
        for (RaceTrackObjectBean field: raceTrack.getFields()) {
            List<CamelBean> camels = field.stack();
            fieldId = raceTrackFieldIds.get(raceTrack.getFields().indexOf(field));
            RelativeLayout fieldLayout = (RelativeLayout) getActivity().findViewById(fieldId);

            if (camels != null && field.isOasis() == null) {
                for (int pos = 0; pos < camels.size(); pos++){
                    imageName = "camel_" + camels.get(pos).color().name().toLowerCase();
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

    private void updateLegBettingFields(){
        Integer legBetFieldID;
        int color;
        for(LegBettingTile topTile : legBettingArea.getTopLegBettingTiles()){
            color = legBettingArea.getTopLegBettingTiles().indexOf(topTile);
            legBetFieldID = legBettingFieldIds.get(color);
            ImageView legBetButton = (ImageView) getActivity().findViewById(legBetFieldID);

            // compose the correct name for the current color's top leg betting tile
            String cardImageName = "legbettingtile_" + GameColors.values()[color].name().toLowerCase() + "_" + topTile.leadingPositionGain() + "_button";
            final int cardDrawableId = getActivity().getResources().getIdentifier(cardImageName, "drawable", getActivity().getPackageName());
            legBetButton.setImageResource(cardDrawableId);
        }
    }

    private void updateRaceBettingFields(){
        ImageView tolleCamelButton = (ImageView) getActivity().findViewById(raceBettingFieldIds.get(0));
        ImageView olleCamelButton = (ImageView) getActivity().findViewById(raceBettingFieldIds.get(1));

/* TODO: get the last playerId who has set the card on the specific field (winner/loser)
        String tolleCamelImageName = "c_" + raceBettingArea.getNrOfWinnerBetting() + "_button";
        String olleCamelImageName = "c_" + raceBettingArea.getNrOfLoserBetting() + "_button";
        final int tolleCamelDrawableId = getActivity().getResources().getIdentifier(tolleCamelImageName, "drawable", getActivity().getPackageName());
        final int olleCamelDrawableId = getActivity().getResources().getIdentifier(olleCamelImageName, "drawable", getActivity().getPackageName());
        tolleCamelButton.setImageResource(tolleCamelDrawableId);
        olleCamelButton.setImageResource(olleCamelDrawableId);*/
    }

    private void updateHeaderBar(){
        ImageView playerIcon = (ImageView) getActivity().findViewById(playerIconId);
        TextView playerName = (TextView) getActivity().findViewById(R.id.player_name);
        ImageView currentPlayerIcon = (ImageView) getActivity().findViewById(R.id.current_player_icon);
        TextView currentPlayerName = (TextView) getActivity().findViewById(R.id.current_player_name);
        TextView money = (TextView) getActivity().findViewById(R.id.money);

        playerIcon.setImageResource(getActivity().getResources().getIdentifier("c" + playerId + "_head", "id", getActivity().getPackageName()));
        playerName.setText(player.username());
        if (player.equals(currentPlayer)) {
            currentPlayerName.setText("YOU");
            currentPlayerIcon.setVisibility(View.GONE);
        } else {
            currentPlayerName.setText(currentPlayer.username());
            currentPlayerIcon.setImageResource(getActivity().getResources().getIdentifier("c" + currentPlayer.id() + "_head", "id", getActivity().getPackageName()));
            currentPlayerIcon.setVisibility(View.VISIBLE);
        }
        money.setText(player.money()+"");
    }

    /**
     * Removes all views from the racetrack
     */
    private void clearRaceTrack() {
        RelativeLayout fieldLayout;
        for (Integer field: raceTrackFieldIds) {
            fieldLayout = (RelativeLayout) getActivity().findViewById(field);
            if (!(fieldLayout == null) && fieldLayout.getChildCount() > 0) {
                fieldLayout.removeAllViews();
            }
        }
    }

    public void getPlayerStatus() {
        RestService.getInstance(getActivity()).getGamePlayer(gameId, playerId, new Callback<User>() {
            @Override
            public void success(User user, Response response) {
                player = user;
                currentPlayer = player; // TODO: get the current player
                updateHeaderBar();
                gameFinishEvaluation();
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Toast.makeText(getActivity(), "Get Player's Status Failed: " + retrofitError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void roundEvaluation() {
        RestService.getInstance(getActivity()).getPlayers(gameId, new Callback<List<User>>() {
            @Override
            public void success(List<User> users, Response response) {
                Collections.sort(users, new Comparator<User>() {
                    @Override
                    public int compare(User user1, User user2) {

                        return user1.money().compareTo(user2.money());
                    }
                });

                usersOrderedByMoney = users;

                roundEvaluationPopup();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), "Get List of Players Failed: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void gameFinishEvaluation(){
        Bundle b = new Bundle();
        b.putLong("userId", userId);
        b.putLong("gameId", gameId);
        b.putBoolean("isOwner", isOwner);
        Fragment fragment = new GameFinishFragment();
        fragment.setArguments(b);

        ((GameActivity) getActivity()).pushFragment(fragment);
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
                diceArea = (DiceArea) area;
            }

            @Override
            public void onError(String errorMessage) {
                AlertDialog dialog = dummyPopup("DICE_AREA subscribe error: " + errorMessage);
                dialog.show();
            }
        });

        AreaService.getInstance(getActivity()).addSubscriber(AreaName.LEG_BETTING_AREA, new AreaUpdateSubscriber() {
            @Override
            public void onUpdate(AbstractArea area) {
                legBettingArea = (LegBettingArea) area;
                updateLegBettingFields();
            }

            @Override
            public void onError(String errorMessage) {
                AlertDialog dialog = dummyPopup("LEG_BETTING_AREA subscribe error: " + errorMessage);
                dialog.show();
            }
        });

        AreaService.getInstance(getActivity()).addSubscriber(AreaName.RACE_BETTING_AREA, new AreaUpdateSubscriber() {
            @Override
            public void onUpdate(AbstractArea area) {
                raceBettingArea = (RaceBettingArea) area;
                updateRaceBettingFields();
            }

            @Override
            public void onError(String errorMessage) {
                AlertDialog dialog = dummyPopup("RACE_BETTING_AREA subscribe error: " + errorMessage);
                dialog.show();
            }
        });

        AreaService.getInstance(getActivity()).addSubscriber(AreaName.RACE_TRACK, new AreaUpdateSubscriber() {
            @Override
            public void onUpdate(AbstractArea area) {
                raceTrack = (RaceTrack) area;
                updateRaceTrackFields();
            }

            @Override
            public void onError(String errorMessage) {
                AlertDialog dialog = dummyPopup("RACE_TRACK subscribe error: " + errorMessage);
                dialog.show();
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
                        getPlayerStatus();
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getActivity(), "new move event: " +
                                        ((MoveEvent) moveEvent).getMoveId(), Toast.LENGTH_SHORT).show();
                                updateHeaderBar();
                            }
                        });
                }
                });

        PusherService.getInstance(getActivity()).addSubscriber(PushEventNameEnum.PLAYER_TURN_EVENT,
                new PusherEventSubscriber() {
                    @Override
                    public void onNewEvent(final AbstractPusherEvent event) {
                        System.out.println("got new event");

                        PlayerTurnEvent playerTurnEvent = (PlayerTurnEvent) event;

                        if(playerId == playerTurnEvent.getPlayerId()){
                            // TODO notify player that it is her turn
                            updateHeaderBar();
                        }
                    }
                });

        PusherService.getInstance(getActivity()).addSubscriber(PushEventNameEnum.LEG_OVER_EVENT,
            new PusherEventSubscriber() {
                @Override
                public void onNewEvent(final AbstractPusherEvent moveEvent) {
                    roundEvaluation();
                }
            });
    }
}