package ch.uzh.ifi.seal.soprafs15.model;

import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserStatus;
import ch.uzh.ifi.seal.soprafs15.model.game.*;
import ch.uzh.ifi.seal.soprafs15.model.move.Move;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Enumerated
	@Column(nullable = false) 
	private UserStatus status;

    @ManyToOne
    private Game game;
	
    @OneToMany(mappedBy="user", cascade = CascadeType.ALL) //, fetch=FetchType.EAGER)
    private List<Move> moves = new ArrayList<Move>();

    @Column
    private Integer money;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @MapKeyColumn(name = "color", length = 50, nullable = false)
    @MapKeyEnumerated(EnumType.STRING)
    private Map<Color, RaceBettingCard> raceBettingCards = new HashMap<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @Column(columnDefinition = "BLOB")
    private List<LegBettingTile> legBettingTiles;

    @OneToOne(mappedBy = "owner", cascade = CascadeType.ALL)
    private DesertTile desertTile;

    public User(){}

    /**
     * Initialization of user
     */
    public void initForGamePlay() {
        // money
        money = 3;

        // race betting cards
        for(Color c : Color.values()){
            raceBettingCards.put(c, new RaceBettingCard(this, c));
        }

        // desert tile
        desertTile = new DesertTile();
        desertTile.setOwner(this);
    }

    /**
     * User decides to make a leg bet and takes a tile from the stack
     * @param tile
     */
    public void addLegBettingTile(LegBettingTile tile){
        if(!legBettingTiles.contains(tile)){
            legBettingTiles.add(tile);
            tile.setUser(this);
        }
    }

    /**
     * Remove RaceBettingCard from User to place it on race betting stack
     * @return RaceBettingCard
     */
    public RaceBettingCard getRaceBettingCard(Color color){
        return raceBettingCards.remove(color);
    }


    public Map<Color, RaceBettingCard> getRaceBettingCards() {
        return raceBettingCards;
    }

    public void setRaceBettingCards( Map<Color, RaceBettingCard> raceBettingCards) {
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

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
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

    public DesertTile getDesertTile() {
        return desertTile;
    }

    public void setDesertTile(DesertTile desertTile) {
        this.desertTile = desertTile;
    }
}
