package ch.uzh.ifi.seal.soprafs15.model.move;

import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameMoveResponseBean;
import ch.uzh.ifi.seal.soprafs15.model.User;
import ch.uzh.ifi.seal.soprafs15.model.game.Game;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
public abstract class Move implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	protected Long id;
	
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "GAME_ID")
    protected Game game;

    
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn(name = "USER_ID")
    protected User user;

	@Transient
	protected List<String> invalidReasons = new ArrayList<>();

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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<String> getInvalidReasons() {
		return invalidReasons;
	}

	public void setInvalidReasons(List<String> invalidReasons) {
		this.invalidReasons = invalidReasons;
	}

	public void addInvalidReason(String reason){
		invalidReasons.add(reason);
	}


    public GameMoveResponseBean toGameMoveResponseBean(){
		GameMoveResponseBean bean = new GameMoveResponseBean();
		bean.setId(id);
		bean.setGameId(game.getId());
		bean.setUserId(user.getId());
		bean.setPlayerId(user.getPlayerId());
		return bean;
	}

    public abstract Boolean isValid();

    public abstract Move execute();

    public abstract void undo();
}
