package ch.uzh.ifi.seal.soprafs15.model;

import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameStatus;
import ch.uzh.ifi.seal.soprafs15.model.game.*;
import ch.uzh.ifi.seal.soprafs15.model.move.Move;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @author Marco
 *
 * Responsible for managing the game state and undoing moves
 */
@Entity
public class StateManager implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(mappedBy = "stateManager", cascade=CascadeType.ALL)
    private Game game;

    @OneToOne(mappedBy = "stateManager", cascade=CascadeType.ALL)
    private GameState gameState;


    public StateManager(){}

    public StateManager(Game game){
        this.game = game;
        init();
    }

    private void init(){
        gameState = new GameState(this);
    }

    /**
     * Undo handling
     */
    public void undoMove(){
        if(gameState.getMoves().size() > 0){
            Move move = gameState.getMoves().get(gameState.getMoves().size()-1);

            move.undo();
            gameState.removeMove(move);
        }
    }

    public GameStatus getStatus(){
        return gameState.getStatus();
    }
    public void setStatus(GameStatus status){
        gameState.setStatus(status);
    }
    public Integer getCurrentPlayerId(){
        return gameState.getCurrentPlayerId();
    }
    public List<Move> getMoves(){
        return gameState.getMoves();
    }
    public List<User> getPlayers(){
        return gameState.getPlayers();
    }
    public RaceTrack getRaceTrack(){
        return gameState.getRaceTrack();
    }
    public DiceArea getDiceArea(){
        return gameState.getDiceArea();
    }
    public LegBettingArea getLegBettingArea(){
        return gameState.getLegBettingArea();
    }
    public RaceBettingArea getRaceBettingArea(){
        return gameState.getRaceBettingArea();
    }
    public Boolean isInFastMode(){
        return gameState.getIsInFastMode();
    }
    public void setIsInFastMode(Boolean isInFastMode){
        gameState.setIsInFastMode(isInFastMode);
    }

    public void addPlayer(User player){
        gameState.addPlayer(player);
    }
    public void removePlayer(User player){
        gameState.removePlayer(player);
    }
    public void addMove(Move move){
        gameState.addMove(move);
    }
    public void nextPlayer() {
        gameState.nextPlayer();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }
}
