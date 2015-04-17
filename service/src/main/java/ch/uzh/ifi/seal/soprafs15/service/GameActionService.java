package ch.uzh.ifi.seal.soprafs15.service;

import ch.uzh.ifi.seal.soprafs15.controller.GenericService;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GamePlayerRequestBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameResponseBean;
import ch.uzh.ifi.seal.soprafs15.model.User;
import ch.uzh.ifi.seal.soprafs15.model.game.Game;

/**
 * @author Marco
 */
public abstract class GameActionService extends GenericService {

    public abstract GameResponseBean startGame(Long gameId, GamePlayerRequestBean bean);
    public abstract GameResponseBean stopGame(Long gameId, GamePlayerRequestBean bean);
    public abstract GameResponseBean startFastMode(Long gameId, GamePlayerRequestBean bean);
}