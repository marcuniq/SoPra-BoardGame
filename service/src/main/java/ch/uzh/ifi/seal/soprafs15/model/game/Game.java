package ch.uzh.ifi.seal.soprafs15.model.game;

import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameStatus;
import ch.uzh.ifi.seal.soprafs15.model.User;
import ch.uzh.ifi.seal.soprafs15.model.move.Move;
import ch.uzh.ifi.seal.soprafs15.model.StateManager;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
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

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH})
    @JoinColumn(name = "USER_ID")
    private User owner;

    @OneToOne(mappedBy = "game", cascade = CascadeType.ALL)
    private StateManager stateManager;

    @Column
    private String pusherChannelName;

    @Column
    private LocalDateTime creationTime;


    public Game(){
        init();
    }

    private void init() {
        stateManager = new StateManager(this);

        pusherChannelName = UUID.randomUUID().toString();

        creationTime = LocalDateTime.now();
    }

    public void initForGamePlay() {
        setStatus(GameStatus.RUNNING);
        setStartTime(LocalDateTime.now());

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

    public void setStartTime(LocalDateTime startTime){
        stateManager.setStartTime(startTime);
    }

    public LocalDateTime getStartTime(){
        return stateManager.getStartTime();
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

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
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

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }
}
