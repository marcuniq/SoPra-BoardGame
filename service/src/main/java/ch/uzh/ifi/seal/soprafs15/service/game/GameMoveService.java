package ch.uzh.ifi.seal.soprafs15.service.game;

import ch.uzh.ifi.seal.soprafs15.controller.GenericService;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameMoveRequestBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameMoveResponseBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.MoveEnum;

import java.util.List;

/**
 * @author Marco
 */
public abstract class GameMoveService extends GenericService {

    public abstract List<GameMoveResponseBean> listMoves(Long gameId);
    public abstract GameMoveResponseBean addMove(Long gameId, GameMoveRequestBean bean);
    public abstract GameMoveResponseBean getMove(Long moveId);
}
