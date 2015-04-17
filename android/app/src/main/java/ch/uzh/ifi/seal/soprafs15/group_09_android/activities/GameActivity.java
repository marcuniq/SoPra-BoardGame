package ch.uzh.ifi.seal.soprafs15.group_09_android.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import android.widget.TextView;
import ch.uzh.ifi.seal.soprafs15.group_09_android.R;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.*;
import ch.uzh.ifi.seal.soprafs15.group_09_android.service.RestService;
import ch.uzh.ifi.seal.soprafs15.group_09_android.utils.GameField;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class GameActivity extends Activity {

    private ArrayList<GameField> gameFields = new ArrayList<>();
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

        initializeGameField();
        play();

        setContentView(R.layout.activity_game);
    }

    private ImageView addCamel(int margin, int imageResourceId){

        ImageView image = new ImageView(this);
        image.setImageResource(imageResourceId);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT );
        lp.setMargins(margin, 0, 0, margin);
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        image.setLayoutParams(lp);

        return image;
    }

    private void addInteractionTile (RelativeLayout field, int imageResourceId){
        field.setBackgroundResource(imageResourceId);
    }

    /**
     * Adds all the fields to the game
     */
    private void initializeGameField(){
        gameFields.add(new GameField(R.id.field1));
        gameFields.add(new GameField(R.id.field2));
        gameFields.add(new GameField(R.id.field3));
        gameFields.add(new GameField(R.id.field4));
        gameFields.add(new GameField(R.id.field5));
        gameFields.add(new GameField(R.id.field6));
        gameFields.add(new GameField(R.id.field7));
        gameFields.add(new GameField(R.id.field8));
        gameFields.add(new GameField(R.id.field9));
        gameFields.add(new GameField(R.id.field10));
        gameFields.add(new GameField(R.id.field11));
        gameFields.add(new GameField(R.id.field12));
        gameFields.add(new GameField(R.id.field13));
        gameFields.add(new GameField(R.id.field14));
        gameFields.add(new GameField(R.id.field15));
        gameFields.add(new GameField(R.id.field16));
    }

    private void play(){
//        gameMoves();
//        gameRaceTrack();
//        gameLegBettingArea();
//        gameRaceBettingArea();
//        gameDiceArea();
        drawBoard();
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
            public void success(List<RaceTrack> raceTracks, Response response) {

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
     * Draw the board according to the game status
     */
    private void drawBoard() {
        int[] margins = {0,10,20,30,40};

        for (GameField field: gameFields) {
            ArrayList<Integer> camels = field.getCamels();
            RelativeLayout fieldLayout = (RelativeLayout) findViewById(field.getPosition());

            if (field.hasDesert()) {
                //addInteractionTile(fieldLayout,R.id.desertTile);
            } else if (field.hasOase()) {
                //addInteractionTile(fieldLayout,R.id.oaseTile);
            } else if (!camels.isEmpty()) {
                for (int pos = 0; pos < camels.size(); pos++){
                    fieldLayout.addView(addCamel(margins[pos], camels.get(pos)));
                }
            }
        }
    }

}
