package ch.uzh.ifi.seal.soprafs15.group_09_android.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import ch.uzh.ifi.seal.soprafs15.group_09_android.R;
import ch.uzh.ifi.seal.soprafs15.group_09_android.activities.GameActivity;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.*;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.beans.CamelBean;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.beans.DieBean;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.beans.MoveBean;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.beans.RaceTrackObjectBean;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.beans.UserBean;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.enums.AreaName;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.events.AbstractPusherEvent;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.events.MoveEvent;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.events.PlayerTurnEvent;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.events.PushEventNameEnum;
import ch.uzh.ifi.seal.soprafs15.group_09_android.service.*;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.enums.GameColors;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.enums.Moves;
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

    // the player's rack
    private ArrayList<GameColors> raceBettingCards = new ArrayList<>();
    private ArrayList<LegBettingTile> legBettingTiles = new ArrayList<>();
    private ArrayList<Integer> pyramidTiles = new ArrayList<>();
    private Boolean tileIsPlaced;
    private Boolean hasNoMoreRaceBettingCards;

    // all buttons in game
    private ArrayList<Integer> raceTrackFieldIds = new ArrayList<>();
    private ArrayList<Integer> legBettingFieldIds = new ArrayList<>();
    private ArrayList<Integer> raceBettingFieldIds = new ArrayList<>();
    private Integer pyramidFieldId;
    private Integer pyramidTileId;
    private Integer helpButtonId;
    private Integer playerIconId;
    private Button acceptButton;
    private Button rejectButton;
    private ImageView modifiedButton;
    private ImageView pyramidTile;

    // class variables
    private List<UserBean> players;
    private PlayerTurnEvent playerTurnEvent;
    private Long userId;
    private Long gameId;
    private Integer playerId;
    private String token;
    private Boolean isOwner = false;
    private Boolean interactionIsPrevented = false;

    private PopupWindow popupWindow;
    private View anchorView;
    private Bundle savedInstanceState;
    private ViewGroup container;

    private List<UserBean> usersOrderedByMoney;
    private Drawable lastResource;
    private int currentPyramidTile;
    private Boolean isDesertTileAsOasis = null;
    private GameColors pickedCardColor;
    private LegBettingTile pickedTile;
    private Boolean raceBettingOnWinner = null;

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
        cleanRack(true);
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
        if ( !interactionIsPrevented && (playerTurnEvent == null || playerId.equals(playerTurnEvent.getPlayerId())) ) {
            if (!tileIsPlaced) {
                for (Integer button : raceTrackFieldIds) {
                    if (button == v.getId()) {
                        interactionTilePopup(v, R.layout.popup_interaction_tile, Moves.DESERT_TILE_PLACING);
                        return;
                    }
                }
            }
            for (Integer button : legBettingFieldIds) {
                if (button == v.getId()) {

                    pickedCardColor = GameColors.values()[legBettingFieldIds.indexOf(button)];
                    modifiedButton = (ImageView) v.findViewById(button);
                    lastResource = modifiedButton.getDrawable();
                    if (lastResource == null) return; // forbid picking "empty" card

                    legBettingPopup(v, R.layout.popup_leg_betting, Moves.LEG_BETTING);
                    return;
                }
            }
            if (!hasNoMoreRaceBettingCards){
                for (Integer button : raceBettingFieldIds) {
                    if (button == v.getId()) {
                        raceBettingPopup(v, R.layout.popup_race_betting, Moves.RACE_BETTING, raceBettingFieldIds.indexOf(button));
                        return;
                    }
                }
            }
            if (pyramidFieldId == v.getId()) {
                rollDicePopup(v, R.layout.popup_roll_dice, Moves.DICE_ROLLING);
            }
        }
        if (helpButtonId == v.getId()){
            instructionsPopup(R.layout.popup_instructions);
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

        pyramidTileId = R.id.pyramid_tile;
        pyramidTile = (ImageView) getActivity().findViewById(pyramidTileId);
        pyramidTile.setImageResource(R.drawable.pyramid_tile_1_button);
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
     *  - Accept: Execute a MoveBean and close the Popup
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

        ArrayList<String> diceImageNames = new ArrayList<>();
        for (GameColors color : GameColors.values()) diceImageNames.add("0_" + color.name().toLowerCase());

        if (!diceArea.getRolledDice().isEmpty()) {
            for (DieBean dieBean : diceArea.getRolledDice()){
                diceImageNames.set(dieBean.color().ordinal(), dieBean.faceValue() + "_" + dieBean.color().name().toLowerCase());
            }
        }

        ImageView dice;
        for (GameColors color : GameColors.values()) {
            dice = (ImageView) popupView.findViewById(getActivity().getResources().getIdentifier("dice_" + color.name().toLowerCase(), "id", getActivity().getPackageName()));
            dice.setImageResource(getActivity().getResources().getIdentifier("roll_dice_" + diceImageNames.get(color.ordinal()), "drawable", getActivity().getPackageName()));
        }

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interactionIsPrevented = true;
                currentPyramidTile = diceArea.getRolledDice().size() + 2;
                String image;
                ImageView pyramidCard = (ImageView) getActivity().findViewById(R.id.pyramid_tile);
                if (currentPyramidTile > 5) image = "empty_image";
                else image = "pyramid_tile_" + currentPyramidTile + "_button";
                pyramidCard.setImageResource(getActivity().getResources().getIdentifier(image, "drawable", getActivity().getPackageName()));
                initiateGameMove(diceRolling, null, null, null, null, null);
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
        raceBettingOnWinner = (type == 0);

        View popupView = defaultPopup(v, popup_race_betting);
        TextView title = (TextView) popupView.findViewById(R.id.popupTitle);

        // check if it is olle or tolle betting
        if (raceBettingOnWinner) title.setText(R.string.title_raceBet_olle); // cast the type {0 or 1} to boolean
        else title.setText(R.string.title_raceBet_tolle);

        final int characterCardDrawableId = getActivity().getResources().getIdentifier("c" + playerId + "_button", "drawable", getActivity().getPackageName());
        int buttonId;
        Integer cardDrawableId;
        String cardImageName = "c" + playerId + "_racebettingcard_";
        ImageButton card;

        for (final GameColors color : GameColors.values()){
            buttonId = getActivity().getResources().getIdentifier("card_" + color.name().toLowerCase(), "id", getActivity().getPackageName());
            card = (ImageButton) popupView.findViewById(buttonId);
            if (raceBettingCards.get(color.ordinal()) != null) {
                cardDrawableId = getActivity().getResources().getIdentifier(cardImageName + color.name().toLowerCase(), "drawable", getActivity().getPackageName());
                card.setImageResource(cardDrawableId);
                final String buttonText = color.name().toLowerCase() + " camel";
                card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        acceptButton.setVisibility(View.VISIBLE);
                        pickedCardColor = color;
                        acceptButton.setText(buttonText);
                        modifiedButton.setImageResource(characterCardDrawableId);
                    }
                });
            } else {
                card.setImageResource(R.drawable.empty_image);
            }
        }

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pickedCardColor != null) {
                    interactionIsPrevented = true;
                    initiateGameMove(raceBetting, null, raceBettingOnWinner, pickedCardColor, null, null);
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

    private void legBettingPopup(View v, int popup_leg_betting, final Moves legBetting) {
        View popupView = defaultPopup(v, popup_leg_betting);

        Integer cardValue = null;
        for (LegBettingTile tile : legBettingArea.getTopLegBettingTiles()) {
            if (tile != null && tile.color() != null && tile.color() == pickedCardColor) {
                pickedTile = tile;
                cardValue = tile.leadingPositionGain();
            }
        }

        ImageView takenCard = (ImageView) popupView.findViewById(R.id.card);
        String cardImageName = "legbettingtile_" + pickedCardColor.name().toLowerCase() + "_" + cardValue;
        final int cardDrawableId = getActivity().getResources().getIdentifier(cardImageName, "drawable", getActivity().getPackageName());
        takenCard.setImageResource(cardDrawableId);

        // find the id for the next card
        int nextResourceId;
        if (cardValue != null && cardValue == 5) nextResourceId = 3;
        else if (cardValue != null && cardValue == 3) nextResourceId = 2;
        else nextResourceId = 0;

        // do not set any image when next card value is 0
        if (nextResourceId == 0) modifiedButton.setImageResource(0);
        else {
            // compose the new card drawable id
            String newCardImageName = "legbettingtile_" + pickedCardColor.name().toLowerCase() + "_" + nextResourceId + "_button";
            int newcardDrawableId = getActivity().getResources().getIdentifier(newCardImageName, "drawable", getActivity().getPackageName());
            modifiedButton.setImageResource(newcardDrawableId);
        }

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pickedCardColor != null) {
                    interactionIsPrevented = true;
                    initiateGameMove(legBetting, pickedCardColor, null, null, null, null);
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
                isDesertTileAsOasis = false;
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
                isDesertTileAsOasis = true;
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
                            interactionIsPrevented = true;
                            initiateGameMove(desertTilePlacing, null, null, null, isDesertTileAsOasis, raceTrackFieldIds.indexOf(field)+1);
                            break;
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

        prefix = "c" + playerId + "_racebettingcard_";
        for (GameColors raceBettingCard : raceBettingCards){
            if (raceBettingCard != null) cardNames.add(prefix + raceBettingCard.name().toLowerCase());
        }
        if (!tileIsPlaced){
            cardNames.add("c" + playerId + "_desert");
            cardNames.add("c" + playerId + "_oasis");
        }
        prefix = "legbettingtile_";
        for (LegBettingTile legBettingTile : legBettingTiles){
            cardNames.add(prefix + legBettingTile.color().name().toLowerCase() + "_" + legBettingTile.leadingPositionGain());
        }
        prefix = "pyramid_tile_";
        for (Integer pyramidTile : pyramidTiles){
            cardNames.add(prefix + pyramidTile);
        }

        for (String card : cardNames){
            image = new ImageView(getActivity());
            image.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            image.setImageResource(getActivity().getResources().getIdentifier(card, "drawable", getActivity().getPackageName()));
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT );
            lp.width = 98;
            lp.height = 156;
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
     */
    public void instructionsPopup(int layout) {
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
        pyramidTile.setImageResource(R.drawable.pyramid_tile_1_button);
        TextView description = (TextView) popupView.findViewById(R.id.description);

        String message = "";
        Integer counter = 1;

        for (UserBean user : usersOrderedByMoney) {
            message += counter + ". UserBean " + user.username() + " has " + user.money() + " egypt pounds. \n";
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

    private void initiateGameMove(final Moves moveType, GameColors legBettingTileColor, Boolean raceBettingOnWinner, GameColors raceBettingColor, Boolean desertTileAsOasis, Integer desertTilePosition) {
        MoveBean move = MoveBean.create(token, moveType, legBettingTileColor, raceBettingOnWinner, raceBettingColor, desertTileAsOasis, desertTilePosition);
        RestService.getInstance(getActivity()).initiateGameMove(gameId, move, new Callback<MoveBean>() {
            @Override
            public void success(MoveBean move, Response response) {
                switch (moveType) {
                    case DICE_ROLLING:
                        System.out.println("added tile : " +  (pyramidTiles.size() + 1) + " to player's rack");
                        pyramidTiles.add(pyramidTiles.size() + 1);
                        break;
                    case LEG_BETTING:
                        legBettingTiles.add(pickedTile);
                        break;
                    case RACE_BETTING:
                        raceBettingCards.set(pickedCardColor.ordinal(), null);
                        break;
                    case DESERT_TILE_PLACING:
                        tileIsPlaced = true;
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), moveType.name() + " Failed: " + error.getMessage(), Toast.LENGTH_LONG).show();

                updateLegBettingFields();
                updateRaceBettingFields();
                updateRaceTrackFields();
                interactionIsPrevented = false;
            }
        });
    }

    private void cleanRack(boolean isFirstRun){
        if (isFirstRun) {
            raceBettingCards.clear();
            for(GameColors color : GameColors.values()){
                raceBettingCards.add(color);
            }
        }
        cleanRack();
    }
    private void cleanRack(){
        legBettingTiles.clear();
        pyramidTiles.clear();
        tileIsPlaced = false;
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
            if (field.position() > raceTrackFieldIds.size()) fieldId = raceTrackFieldIds.get(field.position() -1 -raceTrackFieldIds.size());
            else fieldId = raceTrackFieldIds.get(field.position()-1);
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
        ImageView legBetButton;
        String cardImageName;
        int cardDrawableId;
        for (GameColors color : GameColors.values()){
            legBetFieldID = legBettingFieldIds.get(color.ordinal());
            legBetButton = (ImageView) getActivity().findViewById(legBetFieldID);
            legBetButton.setImageResource(0);
            for(LegBettingTile topTile : legBettingArea.getTopLegBettingTiles()){
                if (!(topTile == null) && !(topTile.color() == null)){
                    if ( topTile.color() == color ) {
                        // compose the correct name for the current color's top leg betting tile
                        cardImageName = "legbettingtile_" + color.name().toLowerCase() + "_" + topTile.leadingPositionGain() + "_button";
                        cardDrawableId = getActivity().getResources().getIdentifier(cardImageName, "drawable", getActivity().getPackageName());
                        legBetButton.setImageResource(cardDrawableId);
                        break;
                    }
                }
            }
        }
    }

    private void updateRaceBettingFields(){
        hasNoMoreRaceBettingCards = true;
        for (GameColors color : GameColors.values()){
            if (raceBettingCards.get(color.ordinal()) != null) {
                hasNoMoreRaceBettingCards = false;
                break;
            }
        }

/* TODO: get the last playerId who has set the card on the specific field (winner/loser)
        ImageView tolleCamelButton = (ImageView) getActivity().findViewById(raceBettingFieldIds.get(0));
        ImageView olleCamelButton = (ImageView) getActivity().findViewById(raceBettingFieldIds.get(1));

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
        playerName.setText(players.get(playerId - 1).username());
        if ( playerTurnEvent == null || playerId.equals(playerTurnEvent.getPlayerId()) ) {
            currentPlayerName.setText("YOU");
            currentPlayerIcon.setVisibility(View.GONE);
        } else {
            currentPlayerName.setText(players.get(playerTurnEvent.getPlayerId()-1).username());
            currentPlayerIcon.setImageResource(getActivity().getResources().getIdentifier("c" + playerTurnEvent.getPlayerId() + "_head", "id", getActivity().getPackageName()));
            currentPlayerIcon.setVisibility(View.VISIBLE);
        }
        money.setText(players.get(playerId-1).money()+"");
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
        RestService.getInstance(getActivity()).getPlayers(gameId, new Callback<List<UserBean>>() {
            @Override
            public void success(List<UserBean> newPlayers, Response response) {
                players = newPlayers;
                updateHeaderBar();
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Toast.makeText(getActivity(), "Get Player's Status Failed: " + retrofitError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void roundEvaluation() {
        RestService.getInstance(getActivity()).getPlayers(gameId, new Callback<List<UserBean>>() {
            @Override
            public void success(List<UserBean> users, Response response) {
                Collections.sort(users, new Comparator<UserBean>() {
                    @Override
                    public int compare(UserBean user1, UserBean user2) {

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
                                interactionIsPrevented = false;
                            }
                        });
                }
                });

        PusherService.getInstance(getActivity()).addSubscriber(PushEventNameEnum.PLAYER_TURN_EVENT,
                new PusherEventSubscriber() {
                    @Override
                    public void onNewEvent(final AbstractPusherEvent event) {
                        System.out.println("got new event");
                        interactionIsPrevented = false;
                        playerTurnEvent = (PlayerTurnEvent) event;

                        updateHeaderBar();

                        if(playerId.equals(playerTurnEvent.getPlayerId())){
                            // TODO notify player that it is her turn
                        }
                    }
                });

        PusherService.getInstance(getActivity()).addSubscriber(PushEventNameEnum.LEG_OVER_EVENT,
            new PusherEventSubscriber() {
                @Override
                public void onNewEvent(final AbstractPusherEvent moveEvent) {
                    roundEvaluation();
                    cleanRack();
                }
            });

        PusherService.getInstance(getActivity()).addSubscriber(PushEventNameEnum.GAME_FINISHED_EVENT,
                new PusherEventSubscriber() {
                    @Override
                    public void onNewEvent(final AbstractPusherEvent moveEvent) {
                        System.out.println("WRAAAAAAAAAAAAAHHHHHHH");

                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                // TODO: remove from subscribers
                                gameFinishEvaluation();
                            }
                        });
                    }
                });
    }
}