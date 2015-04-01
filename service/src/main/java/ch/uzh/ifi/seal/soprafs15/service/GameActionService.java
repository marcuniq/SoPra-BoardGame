package ch.uzh.ifi.seal.soprafs15.service;

import ch.uzh.ifi.seal.soprafs15.controller.GenericService;
import ch.uzh.ifi.seal.soprafs15.model.User;
import ch.uzh.ifi.seal.soprafs15.model.game.Game;

/**
 * @author Marco
 */
public abstract class GameActionService extends GenericService {

    public abstract Game startGame(Long gameId, User user);
    public abstract Game stopGame(Long gameId, User user);
    public abstract Game startFastMode(Long gameId, User user);
}
