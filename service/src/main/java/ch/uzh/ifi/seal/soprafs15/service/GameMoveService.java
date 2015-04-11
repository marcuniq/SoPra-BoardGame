package ch.uzh.ifi.seal.soprafs15.service;

import ch.uzh.ifi.seal.soprafs15.controller.GenericService;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameMoveRequestBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameMoveResponseBean;
import ch.uzh.ifi.seal.soprafs15.model.move.Move;
import ch.uzh.ifi.seal.soprafs15.service.exceptions.PlayerTurnException;

import java.util.List;

/**
 * @author Marco
 */
public abstract class GameMoveService extends GenericService {

    public abstract List<GameMoveResponseBean> listMoves(Long gameId);
    public abstract GameMoveResponseBean addMove(Long gameId, GameMoveRequestBean bean) throws PlayerTurnException;
    public abstract GameMoveResponseBean getMove(Long gameId, Long moveId);
}
