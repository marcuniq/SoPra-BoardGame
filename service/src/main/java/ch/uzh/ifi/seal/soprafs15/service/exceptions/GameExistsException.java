package ch.uzh.ifi.seal.soprafs15.service.exceptions;

import ch.uzh.ifi.seal.soprafs15.model.game.Game;
import org.springframework.http.HttpStatus;

/**
 * Created by Hakuna on 13.04.2015.
 *
 * That means, we are trying to add a game with an id that is already in the repository.
 *
 * */
public class GameExistsException extends UncheckedException {

    public GameExistsException(String message, Class invoker) {
        super(message, invoker, HttpStatus.CONFLICT);
    }

    public GameExistsException(Game game, Class invoker) {
        this("A game with the same id already exists " + game.getId().toString(), invoker);
    }
}
