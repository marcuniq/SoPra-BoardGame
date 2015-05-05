package ch.uzh.ifi.seal.soprafs15.model.game;

import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameStatus;
import ch.uzh.ifi.seal.soprafs15.model.User;
import ch.uzh.ifi.seal.soprafs15.model.move.Move;
import ch.uzh.ifi.seal.soprafs15.model.StateManager;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity
public class Game implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(nullable = false) 
	private String name;
	
	@Column(nullable = false) 
	private String owner;

    @OneToOne(cascade=CascadeType.ALL)
    private StateManager stateManager;

    @Column
    private String pusherChannelName;


    public Game(){
        init();
    }

    private void init() {
        stateManager = new StateManager(this);

        pusherChannelName = UUID.randomUUID().toString();
    }

    public void initForGamePlay() {
        stateManager.getRaceTrack().initForGamePlay();

        for(User p : stateManager.getPlayers())
            p.initForGamePlay();
    }


    public void addMove(Move move) {
        stateManager.addMove(move);
    }

    public void addPlayer(User player){
        stateManager.addPlayer(player);
    }

    public void removePlayer(User player){
        stateManager.removePlayer(player);
    }

    public List<Move> getMoves() {
        return stateManager.getMoves();
    }

    public List<User> getPlayers() {
        return stateManager.getPlayers();
    }

    public GameStatus getStatus() {
        return stateManager.getStatus();
    }

    public void setStatus(GameStatus status) {
        stateManager.setStatus(status);
    }

    public Integer getCurrentPlayerId() {
        return stateManager.getCurrentPlayerId();
    }

    public void nextPlayer(){ stateManager.nextPlayer();}

    public RaceTrack getRaceTrack() {
        return stateManager.getRaceTrack();
    }

    public LegBettingArea getLegBettingArea() {
        return stateManager.getLegBettingArea();
    }

    public RaceBettingArea getRaceBettingArea() {
        return stateManager.getRaceBettingArea();
    }

    public DiceArea getDiceArea() {
        return stateManager.getDiceArea();
    }

    public Boolean isInFastMode(){
        return stateManager.isInFastMode();
    }

    public void setIsInFastMode(Boolean isInFastMode){
        stateManager.setIsInFastMode(isInFastMode);
    }


    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

    public String getPusherChannelName() {
        return pusherChannelName;
    }

    public void setPusherChannelName(String pusherChannelName) {
        this.pusherChannelName = pusherChannelName;
    }

    public StateManager getStateManager() {
        return stateManager;
    }

    public void setStateManager(StateManager stateManager) {
        this.stateManager = stateManager;
    }
}
