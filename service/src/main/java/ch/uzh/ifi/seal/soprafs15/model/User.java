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

    @OneToOne(mappedBy = "owner", cascade = CascadeType.ALL)
    private Game game;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name="GAMESTATE_ID")
    private GameState gameState;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Move> moves = new ArrayList<Move>();

    @Column
    private Integer money;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @MapKeyColumn(name = "color", length = 50, nullable = false)
    @MapKeyEnumerated(EnumType.STRING)
    private Map<Color, RaceBettingCard> raceBettingCards = new HashMap<>();

    @OneToMany( mappedBy = "user",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER)
    @Column(columnDefinition = "BLOB")
    private List<LegBettingTile> legBettingTiles = new ArrayList<>();

    @Column
    private Integer playerId;

    @OneToOne(mappedBy = "owner", cascade = CascadeType.ALL)
    private DesertTile desertTile;

    @Column
    private Boolean hasDesertTile;

    public User(){}

    /**
     * Initialization of user with game related objects
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

        hasDesertTile = true;
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
     * Remove leg betting tile for undoing move
     * @param tile
     */
    public void removeLegBettingTile(LegBettingTile tile){
        if(legBettingTiles.contains(tile)){
            legBettingTiles.remove(tile);
            tile.setUser(null);
        }
    }

    /**
     * Remove all tiles when leg is over
     */
    public void removeAllLegBettingTiles(){
        for(LegBettingTile t : legBettingTiles)
            t.setUser(null);
        legBettingTiles.clear();
    }

    /**
     * Remove RaceBettingCard from User to place it on race betting stack
     * @return RaceBettingCard
     */
    public RaceBettingCard getRaceBettingCard(Color color){
        return raceBettingCards.remove(color);
    }

    /**
     * Helper method to find out if player still has the race betting card
     * with the given color
     * @param color
     * @return
     */
    public Boolean hasRaceBettingCard(Color color){
        return raceBettingCards.get(color) != null;
    }

    /**
     * Helper method for undoing move
     * @param raceBettingCard
     */
    public void putRaceBettingCardBack(RaceBettingCard raceBettingCard){
        raceBettingCards.put(raceBettingCard.getColor(), raceBettingCard);
    }

    /**
     * When player chooses to place the desert tile, it must be removed from his posession
     * @return DesertTile
     */
    public DesertTile removeDesertTile(){
        hasDesertTile = false;
        return desertTile;
    }

    /**
     * Helper method to check if placing the desert tile is a valid move
     * Invalid move if player doesn't have it anymore
     * @return
     */
    public Boolean hasDesertTile(){
        return hasDesertTile;
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

    public Integer getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }

    public DesertTile getDesertTile() {
        return desertTile;
    }

    public void setDesertTile(DesertTile desertTile) {
        this.desertTile = desertTile;
    }

    public Boolean getHasDesertTile() {
        return hasDesertTile;
    }

    public void setHasDesertTile(Boolean hasDesertTile) {
        this.hasDesertTile = hasDesertTile;
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
