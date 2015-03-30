package ch.uzh.ifi.seal.soprafs15.service;

import ch.uzh.ifi.seal.soprafs15.controller.GenericService;
import ch.uzh.ifi.seal.soprafs15.model.Game;
import ch.uzh.ifi.seal.soprafs15.model.User;

/**
 * @author Marco
 */
public abstract class GameActionService extends GenericService {

    public abstract Game startGame(Long id, User user);
    public abstract Game stopGame(Long id, User user);
    public abstract Game startFastMode(Long id, User user);
}
