package ch.uzh.ifi.seal.soprafs15.controller.beans.game;

import ch.uzh.ifi.seal.soprafs15.model.game.LegBettingTile;

import java.util.List;

public class GamePlayerResponseBean {

	private Long id;
    private String username;
    private Integer money;
    private List<LegBettingTile> legBettingTiles;
	private Integer numberOfMoves;
	

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
}