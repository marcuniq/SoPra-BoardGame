package ch.uzh.ifi.seal.soprafs15.model.game;

import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameStatus;
import ch.uzh.ifi.seal.soprafs15.model.User;
import ch.uzh.ifi.seal.soprafs15.model.move.Move;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
	private Integer currentPlayer = 0;

    @OneToMany(mappedBy="game", cascade = CascadeType.ALL) //, fetch=FetchType.EAGER)
    private List<Move> moves = new ArrayList<Move>();
    
    @OneToMany(mappedBy="game", cascade = CascadeType.ALL) //, fetch=FetchType.EAGER)
    //@OrderColumn
    private List<User> players = new ArrayList<User>();

    @OneToOne(mappedBy = "game", cascade=CascadeType.ALL)
    private RaceTrack raceTrack = new RaceTrack();

    @OneToOne(mappedBy = "game", cascade=CascadeType.ALL)
    private LegBettingArea legBettingArea = new LegBettingArea();

    @OneToOne(mappedBy = "game", cascade=CascadeType.ALL)
    private RaceBettingArea raceBettingArea = new RaceBettingArea();

    @OneToOne(mappedBy = "game", cascade=CascadeType.ALL)
    private DiceArea diceArea = new DiceArea();


    public Game(){
        init();
    }

    private void init() {
        raceTrack.setGame(this);
        legBettingArea.setGame(this);
        raceBettingArea.setGame(this);
        diceArea.setGame(this);

        status = GameStatus.OPEN;
    }

    public void addMove(Move move) {
        if(!moves.contains(move)) {
            moves.add(move);
            //move.setGame(this);
        }

    }

    public void addPlayer(User player){
        if(!players.contains(player)) {
            players.add(player);
            player.setGame(this);
        }
    }

    public User getNextPlayer() {
        return getPlayers().get((getCurrentPlayer() + 1) % getPlayers().size());
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


}
