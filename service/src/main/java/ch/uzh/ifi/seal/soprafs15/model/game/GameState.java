package ch.uzh.ifi.seal.soprafs15.model.game;

import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameStatus;
import ch.uzh.ifi.seal.soprafs15.model.User;
import ch.uzh.ifi.seal.soprafs15.model.move.Move;
import ch.uzh.ifi.seal.soprafs15.model.StateManager;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Marco
 */
@Entity
public class GameState implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    private StateManager stateManager;

    @Column
    private GameStatus status;

    @Column
    private Integer currentPlayerId = 1;

    @OneToMany(mappedBy="gameState", cascade = CascadeType.ALL) //, fetch=FetchType.EAGER)
    private List<Move> moves = new ArrayList<Move>();

    @OneToMany(mappedBy="gameState", cascade = CascadeType.ALL) //, fetch=FetchType.EAGER)
    //@OrderColumn
    private List<User> players = new ArrayList<User>();

    @OneToOne(mappedBy = "gameState", cascade=CascadeType.ALL)
    private RaceTrack raceTrack = new RaceTrack();

    @OneToOne(mappedBy = "gameState", cascade=CascadeType.ALL)
    private LegBettingArea legBettingArea = new LegBettingArea();

    @OneToOne(mappedBy = "gameState", cascade=CascadeType.ALL)
    private RaceBettingArea raceBettingArea = new RaceBettingArea();

    @OneToOne(mappedBy = "gameState", cascade=CascadeType.ALL)
    private DiceArea diceArea = new DiceArea();

    @Column
    private Boolean isInFastMode;


    public GameState(StateManager stateManager){
        this.stateManager = stateManager;
        init();
    }

    public GameState(){
        init();
    }

    private void init(){
        raceTrack.setGameState(this);
        legBettingArea.setGameState(this);
        raceBettingArea.setGameState(this);
        diceArea.setGameState(this);

        status = GameStatus.OPEN;

        currentPlayerId = 1;

        isInFastMode = false;
    }

    public void addPlayer(User player){
        if(!players.contains(player)){
            players.add(player);
            player.setGameState(this);
        }
    }

    public void removePlayer(User player){
        if(players.contains(player)){
            players.remove(player);
            //player.setGameState(null);
        }
    }

    public void addMove(Move move) {
        if(!moves.contains(move)){
            moves.add(move);
            move.setGameState(this);
        }
    }

    public void removeMove(Move move){
        if(moves.contains(move)){
            moves.remove(move);
        }
    }
    public void nextPlayer() {
        Optional<User> playerWithNextHigherId = players.stream().filter(p -> p.getPlayerId() > currentPlayerId)
                .min((p1, p2) -> Integer.compare(p1.getPlayerId(), p2.getPlayerId()));

        if(playerWithNextHigherId.isPresent()) {
            currentPlayerId = playerWithNextHigherId.get().getPlayerId();
        } else {
            User playerWithSmallestId = players.stream().min((p1, p2) -> Integer.compare(p1.getPlayerId(), p2.getPlayerId())).get();
            currentPlayerId = playerWithSmallestId.getPlayerId();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public Integer getCurrentPlayerId() {
        return currentPlayerId;
    }

    public void setCurrentPlayerId(Integer currentPlayerId) {
        this.currentPlayerId = currentPlayerId;
    }

    public List<Move> getMoves() {
        return moves;
    }

    public void setMoves(List<Move> moves) {
        this.moves = moves;
    }

    public List<User> getPlayers() {
        return players;
    }

    public void setPlayers(List<User> players) {
        this.players = players;
    }

    public RaceTrack getRaceTrack() {
        return raceTrack;
    }

    public void setRaceTrack(RaceTrack raceTrack) {
        this.raceTrack = raceTrack;
    }

    public LegBettingArea getLegBettingArea() {
        return legBettingArea;
    }

    public void setLegBettingArea(LegBettingArea legBettingArea) {
        this.legBettingArea = legBettingArea;
    }

    public RaceBettingArea getRaceBettingArea() {
        return raceBettingArea;
    }

    public void setRaceBettingArea(RaceBettingArea raceBettingArea) {
        this.raceBettingArea = raceBettingArea;
    }

    public DiceArea getDiceArea() {
        return diceArea;
    }

    public void setDiceArea(DiceArea diceArea) {
        this.diceArea = diceArea;
    }

    public StateManager getStateManager() {
        return stateManager;
    }

    public void setStateManager(StateManager stateManager) {
        this.stateManager = stateManager;
    }


    public Boolean getIsInFastMode() {
        return isInFastMode;
    }

    public void setIsInFastMode(Boolean isInFastMode) {
        this.isInFastMode = isInFastMode;
    }
}
