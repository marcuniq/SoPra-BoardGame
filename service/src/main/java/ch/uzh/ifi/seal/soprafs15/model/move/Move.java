package ch.uzh.ifi.seal.soprafs15.model.move;

import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameMoveResponseBean;
import ch.uzh.ifi.seal.soprafs15.model.User;
import ch.uzh.ifi.seal.soprafs15.model.game.Game;
import ch.uzh.ifi.seal.soprafs15.model.game.GameState;
import ch.uzh.ifi.seal.soprafs15.service.GameLogicService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
public abstract class Move implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	protected Long id;
	
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="GAME_ID")
    protected Game game;

    @ManyToOne(cascade = CascadeType.ALL)
    protected GameState gameState;
    
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="USER_ID")
    protected User user;

    public Move(){}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

    public abstract GameMoveResponseBean toGameMoveResponseBean();

    public abstract Boolean isValid();

	@Autowired
    public abstract Move execute();

    public abstract void undo();
}
