package ch.uzh.ifi.seal.soprafs15.service;

import ch.uzh.ifi.seal.soprafs15.controller.GenericService;
import ch.uzh.ifi.seal.soprafs15.model.User;
import ch.uzh.ifi.seal.soprafs15.model.game.Game;
import ch.uzh.ifi.seal.soprafs15.model.move.Move;
import ch.uzh.ifi.seal.soprafs15.service.exceptions.PlayerTurnException;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Marco
 */
public abstract class GameLogicService extends GenericService {
    public abstract Move processMove(Game game, User player, Move move) throws PlayerTurnException;

}
