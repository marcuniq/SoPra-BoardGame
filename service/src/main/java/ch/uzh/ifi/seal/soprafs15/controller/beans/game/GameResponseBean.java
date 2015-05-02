package ch.uzh.ifi.seal.soprafs15.controller.beans.game;

public class GameResponseBean {

	protected Long id;
	protected String name;
	protected String owner;
	protected GameStatus status;
	protected Integer numberOfMoves;
	protected Integer numberOfPlayers;
	protected Integer currentPlayerId;
	
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
	public GameStatus getStatus() {
		return status;
	}
	public void setStatus(GameStatus status) {
		this.status = status;
	}
	public Integer getNumberOfMoves() {
		return numberOfMoves;
	}
	public void setNumberOfMoves(Integer numberOfMoves) {
		this.numberOfMoves = numberOfMoves;
	}
	public Integer getNumberOfPlayers() {
		return numberOfPlayers;
	}
	public void setNumberOfPlayers(Integer numberOfPlayers) {
		this.numberOfPlayers = numberOfPlayers;
	}
	public Integer getCurrentPlayerId() {
		return currentPlayerId;
	}
	public void setCurrentPlayerId(Integer currentPlayerId) {
		this.currentPlayerId = currentPlayerId;
	}
}