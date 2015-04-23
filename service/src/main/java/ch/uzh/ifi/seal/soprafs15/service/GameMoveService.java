package ch.uzh.ifi.seal.soprafs15.service;

import ch.uzh.ifi.seal.soprafs15.controller.GenericService;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameMoveRequestBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameMoveResponseBean;
import ch.uzh.ifi.seal.soprafs15.model.move.Move;
import ch.uzh.ifi.seal.soprafs15.service.exceptions.GameNotFoundException;
import ch.uzh.ifi.seal.soprafs15.service.exceptions.InvalidMoveException;
import ch.uzh.ifi.seal.soprafs15.service.exceptions.PlayerTurnException;
import ch.uzh.ifi.seal.soprafs15.service.exceptions.UserNotFoundException;

import java.util.List;

/**
 * @author Marco
 */
public abstract class GameMoveService extends GenericService {

    public abstract List<GameMoveResponseBean> listMoves(Long gameId) throws GameNotFoundException;
    public abstract GameMoveResponseBean addMove(Long gameId, GameMoveRequestBean bean) throws PlayerTurnException, GameNotFoundException, UserNotFoundException, InvalidMoveException;
    public abstract GameMoveResponseBean getMove(Long moveId);
}
