package ch.uzh.ifi.seal.soprafs15.group_09_android.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import ch.uzh.ifi.seal.soprafs15.group_09_android.R;
import ch.uzh.ifi.seal.soprafs15.group_09_android.activities.GameActivity;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.*;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.beans.CamelBean;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.beans.DiceAreaBean;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.beans.DieBean;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.beans.GameBean;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.beans.LegBettingAreaBean;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.beans.MoveBean;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.beans.RaceBettingAreaBean;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.beans.RaceTrackBean;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.beans.RaceTrackObjectBean;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.beans.UserBean;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.enums.AreaName;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.events.AbstractPusherEvent;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.events.PlayerTurnEvent;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.events.PushEventNameEnum;
import ch.uzh.ifi.seal.soprafs15.group_09_android.service.*;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.enums.GameColors;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.enums.Moves;
import ch.uzh.ifi.seal.soprafs15.group_09_android.utils.PlayerArrayAdapter;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
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
    private Integer fastModeButtonId;
    private Integer helpButtonId;
    private Integer playerIconId;
    private Button acceptButton;
    private Button rejectButton;
    private ImageView modifiedButton;
    private ImageView pyramidTile;
    private ImageView fastModeButton;

    // class variables
    private List<UserBean> players;
    private MoveBean lastMove;
    private Long userId;
    private Long gameId;
    private GameBean game;
    private Integer playerId;
    private String token;
    private Boolean isOwner = false;
    private boolean interactionIsPrevented = false;
    private Boolean isFastMode = false;
    private Boolean showQuickGuidePopup = true;
    private String channelName;

    private PlayerArrayAdapter playerArrayAdapter; // adapts the ArrayList of Games to the ListView

    private PopupWindow popupWindow;
    private View anchorView;
    private Bundle savedInstanceState;
    private ViewGroup container;

    private Drawable lastResource;
    private int currentPyramidTile;
    private Boolean isDesertTileAsOasis = null;
    private GameColors pickedCardColor;
    private LegBettingTile pickedTile;
    private Boolean raceBettingOnWinner = null;

    private HashMap<AreaName, AreaUpdateSubscriber> subscribedAreas = new HashMap<>();
    private HashMap<PushEventNameEnum, PusherEventSubscriber> subscribedPushers = new HashMap<>();

    private ImageView instructionImage;
    private int current = 0;
    private ArrayList<Integer> images = new ArrayList<>();

    private OnBackPressedListener onBackPressedListener;

    public interface OnBackPressedListener {
        public void unsubscribeFromEvents();
        public void unsubscribeFromAreas();
        public void setSubscribedAreas (HashMap<AreaName, AreaUpdateSubscriber> subscribedAreas);
        public void setSubscribedPushers (HashMap<PushEventNameEnum, PusherEventSubscriber> subscribedPushers);
    }

    public static GameFragment newInstance() {
        return new GameFragment();
    }

    public GameFragment() { }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            onBackPressedListener = (OnBackPressedListener) activity;
        } catch (ClassCastException castException) {
            /** The activity does not implement the listener. */
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = getActivity().getIntent().getExtras();
        gameId = b.getLong("gameId");
        userId = b.getLong("userId");
        if (b.containsKey("playerId")) playerId = b.getInt("playerId");
        else playerId = 8;
        if (b.containsKey("isFastMode")) isFastMode = b.getBoolean("isFastMode");
        isOwner = b.getBoolean("isOwner");
        channelName = b.getString("gameChannel");

        SharedPreferences sharedPref = getActivity().getSharedPreferences("token", Context.MODE_PRIVATE);
        token = sharedPref.getString("token", token);

        interactionIsPrevented = isFastMode;
        showQuickGuidePopup = !isFastMode;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.savedInstanceState = savedInstanceState;
        this.container = container;
        return inflater.inflate(R.layout.fragment_game, container, false);
    }

    @Override
    public void onStart(){
        super.onStart();
        addClickListenerToButtons();
        cleanRack(true);

        subscribeToAreaUpdates();
        subscribeToAllEvents();
        AreaService.getInstance(getActivity()).getAreasAndNotifySubscriber(gameId);
    }

    @Override
    public void onResume(){
        super.onResume();
        getGameStatus();
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
        // Show quick guide popup once after the first click
        if (showQuickGuidePopup) {
            quickGuidePopup();
            showQuickGuidePopup = false;
            return;
        }

        // prevent action from a player if it's not his turn
        if ( !isFastMode && !interactionIsPrevented && (game == null || playerId.equals(game.currentPlayerId()))) {
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
                rollDicePopup(v, R.layout.popup_roll_dice, Moves.DICE_ROLLING, true);
                return;
            }
        }
        if (pyramidFieldId == v.getId()) {
            rollDicePopup(v, R.layout.popup_roll_dice, Moves.DICE_ROLLING, false);
            return;
        }
        if (helpButtonId == v.getId()){
            instructionsPopup(R.layout.popup_instructions);
            return;
        }
        if (playerIconId == v.getId()){
            playerInfoPopup(v, R.layout.popup_player_info);
            return;
        }
        if (isFastMode && fastModeButtonId == v.getId()){
            fastModeButton.setVisibility(View.GONE);
            initiateNextFastModeMove();
        }
    }

    private void initiateNextFastModeMove() {
        RestService.getInstance(getActivity()).triggerNextMoveInFastMode(gameId, UserBean.setToken(token), new Callback<MoveBean>() {
            @Override
            public void success(MoveBean move, Response response) {
                lastMove = move;
                AreaService.getInstance(getActivity()).getAreasAndNotifySubscriber(gameId);
                fastModeButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void failure(RetrofitError error) {
                fastModeButton.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(), " Failed: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
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

        // pyramid card tile stack
        pyramidTileId = R.id.pyramid_tile;
        pyramidTile = (ImageView) getActivity().findViewById(pyramidTileId);
        pyramidTile.setImageResource(R.drawable.pyramid_tile_1_button);

        // next move event button for fast mode
        fastModeButtonId = R.id.next_move_event_fastmode;
        fastModeButton = (ImageView) getActivity().findViewById(fastModeButtonId);
        if (isFastMode) (getActivity().findViewById(fastModeButtonId)).setOnClickListener(this);
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
    private void rollDicePopup(View v, int popup_roll_dice, final Moves diceRolling, boolean canRollDice) {
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

        if (canRollDice) {
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
        } else {
            acceptButton.setVisibility(View.GONE);
        }

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
                if (modifiedButton != null)
                    ((RelativeLayout) modifiedButton.getParent()).removeView(modifiedButton);
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
            lp.width = 147;
            lp.height = 234;
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
        View popupView = defaultPopup(getView(), R.layout.fragment_game_finish);
        pyramidTile.setImageResource(R.drawable.pyramid_tile_1_button);

        roundEvaluation(popupView);

        TextView popupTitle = (TextView) popupView.findViewById(R.id.popupTitle);
        TextView listTitle = (TextView) popupView.findViewById(R.id.list_title);

        popupTitle.setText("Round Evaluation");
        listTitle.setText("Round Ranking");

        Button closeButton = (Button) popupView.findViewById(R.id.close);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    private void quickGuidePopup() {
        View popupView = defaultPopup(getView(), R.layout.popup_quick_guide);

        Button closeButton = (Button) popupView.findViewById(R.id.reject);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        Button instructionGuideButton = (Button) popupView.findViewById(R.id.accept);

        instructionGuideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                giveInstructions();
            }
        });
    }

    private void giveInstructions(){
        instructionImage = (ImageView) getActivity().findViewById(R.id.ivInstructions);

        images.clear();
        current = 0;
        images.add(R.drawable.username_icon_explain);
        images.add(R.drawable.current_player_explain);
        images.add(R.drawable.help_rules_money);
        images.add(R.drawable.roll_a_dice);
        images.add(R.drawable.desert_oasis_tile_placing);
        images.add(R.drawable.take_legbetting_tile);
        images.add(R.drawable.overall_winner_loser);

        instructionImage.setVisibility(View.VISIBLE);
        instructionImage.setImageResource(images.get(current++));

        instructionImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (current < images.size()){
                    instructionImage.setImageResource(images.get(current++));
                } else {
                    instructionImage.setVisibility(View.GONE);
                }
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
                        if (pyramidTiles.size() < 4) pyramidTiles.add(pyramidTiles.size() + 1);
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

        if (lastMove != null){
            ImageView button;
            String cardDrawableName = "c" + lastMove.playerId() + "_button";
            final int cardDrawableId = getActivity().getResources().getIdentifier(cardDrawableName, "drawable", getActivity().getPackageName());
            if (lastMove.raceBettingOnWinner() != null) {
                if (lastMove.raceBettingOnWinner()) {
                    button = (ImageView) getActivity().findViewById(raceBettingFieldIds.get(0));
                } else {
                    button = (ImageView) getActivity().findViewById(raceBettingFieldIds.get(1));
                }
                button.setImageResource(cardDrawableId);
            }
        }
    }

    private void updateHeaderBar(){
        ImageView playerIcon = (ImageView) getActivity().findViewById(playerIconId);
        TextView playerName = (TextView) getActivity().findViewById(R.id.player_name);
        ImageView currentPlayerIcon = (ImageView) getActivity().findViewById(R.id.current_player_icon);
        TextView currentPlayerName = (TextView) getActivity().findViewById(R.id.current_player_name);
        TextView money = (TextView) getActivity().findViewById(R.id.money);
        TextView currentPlaying = (TextView) getActivity().findViewById(R.id.current_playing);

        playerIcon.setBackgroundResource(getActivity().getResources().getIdentifier("c" + playerId + "_head", "drawable", getActivity().getPackageName()));

        if (isFastMode){
            playerName.setText("FASTMODE");
            currentPlayerName.setText("PLEASE TRIGGER NEXT FAST MODE MOVE");
            currentPlaying.setText("");
            currentPlayerIcon.setVisibility(View.GONE);
            fastModeButton.setVisibility(View.VISIBLE);
        } else {
            playerName.setText(players.get(playerId - 1).username());
            if (game == null || playerId.equals(game.currentPlayerId())) {
                currentPlayerName.setText("YOU");
                currentPlayerIcon.setVisibility(View.GONE);
            } else {
                currentPlayerName.setText(players.get(game.currentPlayerId() - 1).username());
                currentPlayerIcon.setBackgroundResource(getActivity().getResources().getIdentifier("c" + game.currentPlayerId() + "_head", "drawable", getActivity().getPackageName()));
                currentPlayerIcon.setVisibility(View.VISIBLE);
            }
            money.setText(players.get(playerId - 1).money() + "");
        }
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

    public void getGameStatus() {
        RestService.getInstance(getActivity()).getGame(gameId, new Callback<GameBean>() {
            @Override
            public void success(GameBean newGameStatus, Response response) {
                game = newGameStatus;
                getPlayerStatus();
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Toast.makeText(getActivity(), "Get Game's Status Failed: " + retrofitError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getMoves() {
        RestService.getInstance(getActivity()).getGameMoves(gameId, new Callback<List<MoveBean>>() {
            @Override
            public void success(List<MoveBean> newMoves, Response response) {
                lastMove = newMoves.get(newMoves.size() - 1);

                if (!lastMove.playerId().equals(playerId)) {
                    String message = "" + players.get(lastMove.playerId() - 1).username();
                    switch (lastMove.move()) {
                        case RACE_BETTING:
                            updateRaceBettingFields();
                            if (lastMove.raceBettingOnWinner())
                                message += " has bet on the Winner Camel.";
                            else message += " has bet on the Loser Camel.";
                            break;
                        case LEG_BETTING:
                            message += " has taken a " + lastMove.legBettingTile().color().name().toLowerCase() + " Legbetting Card.";
                            break;
                        case DESERT_TILE_PLACING:
                            if (lastMove.desertTileAsOasis())
                                message += " has placed a Oasis Tile on Field " + lastMove.desertTilePosition() + ".";
                            else
                                message += " has placed a Desert Tile on Field " + lastMove.desertTilePosition() + ".";
                            break;
                        case DICE_ROLLING:
                            message += " has rolled the Dice.";
                            break;
                        default:
                            message += " has performed an unknown move.";
                            break;
                    }
                    Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Toast.makeText(getActivity(), "Get Moves Failed: " + retrofitError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void roundEvaluation(View view) {
        playerArrayAdapter = new PlayerArrayAdapter(
                getActivity(),
                R.layout.player_item,
                R.id.player_item_text,
                R.id.player_item_description,
                R.id.player_item_icon,
                new ArrayList<UserBean>(),
                true);
        ListView list = (ListView) view.findViewById(android.R.id.list);
        list.setAdapter(playerArrayAdapter);

        RestService.getInstance(getActivity()).getPlayers(gameId, new Callback<List<UserBean>>() {
            @Override
            public void success(List<UserBean> users, Response response) {
                playerArrayAdapter.clear();
                Collections.sort(users, new Comparator<UserBean>() {
                    @Override
                    public int compare(UserBean user1, UserBean user2) {

                        return user2.money().compareTo(user1.money());
                    }
                });
                playerArrayAdapter.addAll(users);
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
        AreaName areaName;
        AreaUpdateSubscriber areaUpdateSubscriber;
        AreaService.getInstance(getActivity()).addSubscriber(
                areaName = AreaName.DICE_AREA,
                areaUpdateSubscriber = new AreaUpdateSubscriber() {
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
        subscribedAreas.put(areaName,areaUpdateSubscriber);

        AreaService.getInstance(getActivity()).addSubscriber(
                areaName = AreaName.LEG_BETTING_AREA,
                areaUpdateSubscriber = new AreaUpdateSubscriber() {
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
        subscribedAreas.put(areaName,areaUpdateSubscriber);

        AreaService.getInstance(getActivity()).addSubscriber(
                areaName = AreaName.RACE_BETTING_AREA,
                areaUpdateSubscriber = new AreaUpdateSubscriber() {
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
        subscribedAreas.put(areaName,areaUpdateSubscriber);

        AreaService.getInstance(getActivity()).addSubscriber(
                areaName = AreaName.RACE_TRACK,
                areaUpdateSubscriber = new AreaUpdateSubscriber() {
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
        subscribedAreas.put(areaName, areaUpdateSubscriber);
        onBackPressedListener.setSubscribedAreas(subscribedAreas);
    }

    private void subscribeToAllEvents() {
        subscribeToMoveEvents();
        subscribeToPlayerTurnEvents();
        subscribeToLegOverEvents();
        subscribeToGameFinishedEvents();
    }

    private void subscribeToMoveEvents() {
        PushEventNameEnum pushEventNameEnum;
        PusherEventSubscriber pusherEventSubscriber;

        Log.i("GameFragment", "subscribed to MOVE_EVENT");
        PusherService.getInstance(getActivity()).addSubscriber(
                pushEventNameEnum = PushEventNameEnum.MOVE_EVENT,
                pusherEventSubscriber = new PusherEventSubscriber() {
                    @Override
                    public void onNewEvent(final AbstractPusherEvent moveEvent) {
                        Log.d("GameFragment", "got new MOVE_EVENT");

                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                getGameStatus();
                                getMoves();
                                interactionIsPrevented = false;
                            }
                        });
                    }
                });
        subscribedPushers.put(pushEventNameEnum, pusherEventSubscriber);
        onBackPressedListener.setSubscribedPushers(subscribedPushers);
    }

    private void subscribeToPlayerTurnEvents() {
        PushEventNameEnum pushEventNameEnum;
        PusherEventSubscriber pusherEventSubscriber;

        Log.i("GameFragment", "subscribed to PLAYER_TURN_EVENT");
        PusherService.getInstance(getActivity()).addSubscriber(
                pushEventNameEnum = PushEventNameEnum.PLAYER_TURN_EVENT,
                pusherEventSubscriber = new PusherEventSubscriber() {
                    @Override
                    public void onNewEvent(final AbstractPusherEvent event) {
                        Log.d("GameFragment", "got new PLAYER_TURN_EVENT");
                        interactionIsPrevented = false;
                        PlayerTurnEvent playerTurnEvent = (PlayerTurnEvent) event;
                    }
                });
        subscribedPushers.put(pushEventNameEnum, pusherEventSubscriber);
        onBackPressedListener.setSubscribedPushers(subscribedPushers);
    }

    private void subscribeToLegOverEvents() {
        PushEventNameEnum pushEventNameEnum;
        PusherEventSubscriber pusherEventSubscriber;

        Log.i("GameFragment", "subscribed to LEG_OVER_EVENT");
        PusherService.getInstance(getActivity()).addSubscriber(
                pushEventNameEnum = PushEventNameEnum.LEG_OVER_EVENT,
                pusherEventSubscriber = new PusherEventSubscriber() {
                    @Override
                    public void onNewEvent(final AbstractPusherEvent moveEvent) {
                        Log.d("GameFragment", "got new LEG_OVER_EVENT");
                        cleanRack();

                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                if (popupWindow != null) popupWindow.dismiss();
                                interactionIsPrevented = false;
                                roundEvaluationPopup();
                            }
                        });
                    }
                });
        subscribedPushers.put(pushEventNameEnum, pusherEventSubscriber);
        onBackPressedListener.setSubscribedPushers(subscribedPushers);
    }

    private void subscribeToGameFinishedEvents(){
        PushEventNameEnum pushEventNameEnum;
        PusherEventSubscriber pusherEventSubscriber;

        Log.i("GameFragment", "subscribed to GAME_FINISHED_EVENT");
        PusherService.getInstance(getActivity()).addSubscriber(
                pushEventNameEnum = PushEventNameEnum.GAME_FINISHED_EVENT,
                pusherEventSubscriber = new PusherEventSubscriber() {
                    @Override
                    public void onNewEvent(final AbstractPusherEvent moveEvent) {
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                Log.d("GameFragment", "got new GAME_FINISHED_EVENT");
                                onBackPressedListener.unsubscribeFromAreas();
                                onBackPressedListener.unsubscribeFromEvents();

                                PusherService.getInstance(getActivity()).unregister(gameId, channelName);
                                gameFinishEvaluation();
                            }
                        });
                    }
        });
        subscribedPushers.put(pushEventNameEnum, pusherEventSubscriber);
        onBackPressedListener.setSubscribedPushers(subscribedPushers);
    }
}