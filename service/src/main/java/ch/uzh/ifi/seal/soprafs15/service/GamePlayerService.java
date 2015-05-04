package ch.uzh.ifi.seal.soprafs15.service;

import ch.uzh.ifi.seal.soprafs15.controller.GenericService;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameAddPlayerResponseBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GamePlayerRequestBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GamePlayerResponseBean;
import ch.uzh.ifi.seal.soprafs15.model.game.RaceBettingCard;

import java.util.List;

/**
 * @author Marco
 */
public abstract class GamePlayerService extends GenericService {

    public abstract List<GamePlayerResponseBean> listPlayer(Long gameId);
    public abstract GameAddPlayerResponseBean addPlayer(Long gameId, GamePlayerRequestBean bean);
    public abstract GamePlayerResponseBean getPlayer(Long gameId, Integer playerId);
    public abstract List<RaceBettingCard> getRaceBettingCards(Long gameId, Integer playerId, GamePlayerRequestBean gamePlayerRequestBean);
}
