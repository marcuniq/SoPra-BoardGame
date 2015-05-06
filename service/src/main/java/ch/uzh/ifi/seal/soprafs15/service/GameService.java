package ch.uzh.ifi.seal.soprafs15.service;

import ch.uzh.ifi.seal.soprafs15.controller.GenericService;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.*;

import java.util.List;

/**
 * @author Marco
 */
public abstract class GameService extends GenericService {

    public abstract List<GameResponseBean> listGames(String status);

    public abstract GameCreateResponseBean addGame(GameRequestBean bean);

    public abstract GameResponseBean getGame(Long gameId);

    public abstract void deleteGame(Long gameId, GamePlayerRequestBean bean);
}
