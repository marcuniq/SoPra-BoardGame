package ch.uzh.ifi.seal.soprafs15.model.game;

import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameStatus;
import ch.uzh.ifi.seal.soprafs15.model.User;
import ch.uzh.ifi.seal.soprafs15.model.move.Move;
import ch.uzh.ifi.seal.soprafs15.model.StateManager;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "STATEMANAGER_ID")
    private StateManager stateManager;

    @Column
    private GameStatus status;

    @Column
    private Integer currentPlayerId = 1;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "GAMESTATE_ID")
    private List<Move> moves = new ArrayList<Move>();

    @OneToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "USER_ID")
    private List<User> players = new ArrayList<User>();

    @OneToOne(mappedBy = "gameState", cascade = CascadeType.ALL)
    private RaceTrack raceTrack = new RaceTrack();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "LEGBETTINGAREA_ID")
    private LegBettingArea legBettingArea = new LegBettingArea();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "RACEBETTINGAREA_ID")
    private RaceBettingArea raceBettingArea = new RaceBettingArea();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "DICEAREA_ID")
    private DiceArea diceArea = new DiceArea();

    @Column
    private Boolean isInFastMode;

    @Column
    private LocalDateTime startTime;


    public GameState(StateManager stateManager){
        this.stateManager = stateManager;
        init();
    }

    public GameState(){
        init();
    }

    private void init(){
        raceTrack.setGameState(this);

        status = GameStatus.OPEN;

        currentPlayerId = 1;

        isInFastMode = false;
    }

    public void addPlayer(User player){
        if(!players.contains(player)){
            players.add(player);
            //player.setGameState(this);
        }
    }

    public void removePlayer(User player){
        if(players.contains(player)){
            players.remove(player);
            //player.setGameState(null);

            // remove all player related things from game

            // remove desert tile from racetrack
            raceTrack.removePlayersDesertTile(player.getId());
            player.setDesertTile(null);

            // remove race betting cards
            raceBettingArea.removePlayersBet(player.getId());
            player.setRaceBettingCards(null);

            // put back leg betting tiles
            List<LegBettingTile> tiles = player.getLegBettingTiles();
            for(LegBettingTile t : tiles)
                t.setUser(null);

            player.setLegBettingTiles(null);
            legBettingArea.pushAndSort(tiles);

            // remove money
            player.setMoney(0);
        }
    }

    public void addMove(Move move) {
        if(!moves.contains(move)){
            moves.add(move);
            //move.setGameState(this);
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

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
}
