package ch.uzh.ifi.seal.soprafs15.service.exceptions;

import ch.uzh.ifi.seal.soprafs15.model.game.Game;
import org.springframework.http.HttpStatus;

/**
 * Created by Hakuna on 13.04.2015.
 *
 * This handles the case where a user tries to join a game that already has the maximum # of players.
 * Use cas: Two players try to join at the same time.
 * The HttpStatus is GONE, because we're too late with joining.
 */
public class GameFullException extends CheckedException {

    public GameFullException(String message, Class invoker){
        super(message, invoker, HttpStatus.GONE);
    }

    public GameFullException(Game game, Class invoker) {
        this("The maximum number of players in game with id " + game.getId().toString() + " is reached.", invoker);
    }
}
