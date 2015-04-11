package ch.uzh.ifi.seal.soprafs15.service.exceptions;

import ch.uzh.ifi.seal.soprafs15.model.User;
import ch.uzh.ifi.seal.soprafs15.model.game.Game;

/**
 * @author Marco
 */
public class PlayerTurnException extends Exception {
    public PlayerTurnException(Game game, User player) {

    }

    public String toString(){
        return "its not your turn";
    }
}
