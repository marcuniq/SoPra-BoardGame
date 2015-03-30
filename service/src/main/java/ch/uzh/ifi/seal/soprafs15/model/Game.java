package ch.uzh.ifi.seal.soprafs15.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameStatus;
import ch.uzh.ifi.seal.soprafs15.model.game.DiceArea;
import ch.uzh.ifi.seal.soprafs15.model.game.LegBettingArea;
import ch.uzh.ifi.seal.soprafs15.model.game.RaceBettingArea;
import ch.uzh.ifi.seal.soprafs15.model.game.RaceTrack;

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
	
	@Column 
	private GameStatus status;
	
	@Column
	private Integer currentPlayer;

    @OneToMany(mappedBy="game")
    private List<Move> moves;
    
    @ManyToMany(mappedBy="games")
    private List<User> players;

    @Column
    private RaceTrack raceTrack;

    @Column
    private LegBettingArea legBettingArea;

    @Column
    private RaceBettingArea raceBettingArea;

    @Column
    private DiceArea diceArea;

    private void init() {

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

	public GameStatus getStatus() {
		return status;
	}

	public void setStatus(GameStatus status) {
		this.status = status;
	}

	public Integer getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(Integer currentPlayer) {
		this.currentPlayer = currentPlayer;
	}
   
	public User getNextPlayer() {
		return getPlayers().get((getCurrentPlayer() + 1) % getPlayers().size());
	}
}
