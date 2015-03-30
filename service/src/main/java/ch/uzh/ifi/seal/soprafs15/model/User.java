package ch.uzh.ifi.seal.soprafs15.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserStatus;
import ch.uzh.ifi.seal.soprafs15.model.game.LegBettingTile;
import ch.uzh.ifi.seal.soprafs15.model.game.RaceBettingCard;

@Entity
public class User implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;
	
	@Column(nullable = false) 
	private Integer age;
	
	@Column(nullable = false, unique = true) 
	private String username;
	
	@Column(nullable = false, unique = true) 
	private String token;
	
	@Column(nullable = false) 
	private UserStatus status;

    @ManyToMany
    private List<Game> games;
	
    @OneToMany(mappedBy="user")
    private List<Move> moves;

    @Column
    private Integer money;

    @Column
    private List<RaceBettingCard> raceBettingCards;

    @Column
    private List<LegBettingTile> legBettingTiles;

    private void init() {

    }

    public List<RaceBettingCard> getRaceBettingCards() {
        return raceBettingCards;
    }

    public void setRaceBettingCards(List<RaceBettingCard> raceBettingCards) {
        this.raceBettingCards = raceBettingCards;
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    public List<LegBettingTile> getLegBettingTiles() {
        return legBettingTiles;
    }

    public void setLegBettingTiles(List<LegBettingTile> legBettingTiles) {
        this.legBettingTiles = legBettingTiles;
    }

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<Game> getGames() {
		return games;
	}

	public void setGames(List<Game> games) {
		this.games = games;
	}

	public List<Move> getMoves() {
		return moves;
	}

	public void setMoves(List<Move> moves) {
		this.moves = moves;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public UserStatus getStatus() {
		return status;
	}

	public void setStatus(UserStatus status) {
		this.status = status;
	}
}
