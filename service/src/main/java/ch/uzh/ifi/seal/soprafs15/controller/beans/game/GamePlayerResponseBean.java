package ch.uzh.ifi.seal.soprafs15.controller.beans.game;

import ch.uzh.ifi.seal.soprafs15.model.game.LegBettingTile;

import java.util.List;

public class GamePlayerResponseBean {

	protected Long id;
    protected Integer playerId;
    protected String username;
    protected Integer money;
    protected List<LegBettingTile> legBettingTiles;
    protected Integer numberOfMoves;
	

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}


	public Integer getNumberOfMoves() {
		return numberOfMoves;
	}
	public void setNumberOfMoves(Integer numberOfMoves) {
		this.numberOfMoves = numberOfMoves;
	}

    public Integer getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
}