package ch.uzh.ifi.seal.soprafs15.service.exceptions;

import ch.uzh.ifi.seal.soprafs15.model.game.Game;

/**
 * @author Marco
 */
public class GameNotFoundException extends Exception {
    public GameNotFoundException(Game game) {
    }
}
