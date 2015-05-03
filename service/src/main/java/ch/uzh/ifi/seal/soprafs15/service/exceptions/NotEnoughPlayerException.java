package ch.uzh.ifi.seal.soprafs15.service.exceptions;

import ch.uzh.ifi.seal.soprafs15.model.game.Game;
import org.springframework.http.HttpStatus;

/**
 * Created by Hakuna on 13.04.2015.
 *
 * If suddenly all players left at the same time when the owner wants to start the game this exception is thrown.
 */
public class NotEnoughPlayerException extends CheckedException {

    public NotEnoughPlayerException(Game game, Class invoker) {
        super("At least 2 player required to start a game. Not enough player to start the game with id " + game.getId().toString(), invoker, HttpStatus.GONE);
    }
}
