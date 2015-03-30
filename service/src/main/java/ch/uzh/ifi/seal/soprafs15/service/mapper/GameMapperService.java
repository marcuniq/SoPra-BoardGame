package ch.uzh.ifi.seal.soprafs15.service.mapper;

import ch.uzh.ifi.seal.soprafs15.controller.GenericService;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.*;
import ch.uzh.ifi.seal.soprafs15.model.Game;
import ch.uzh.ifi.seal.soprafs15.model.Move;
import ch.uzh.ifi.seal.soprafs15.model.User;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Marco
 */
public abstract class GameMapperService extends GenericService {

    public abstract Game toGame(GameRequestBean bean);
    public abstract GameResponseBean toGameResponseBean(Game game);
    public abstract List<GameResponseBean> toGameResponseBean(List<Game> games);

    public abstract User toUser(GamePlayerRequestBean bean);
    public abstract GamePlayerResponseBean toGamePlayerResponseBean(User user);
    public abstract List<GamePlayerResponseBean> toGamePlayerResponseBean(List<User> users);

    public abstract Move toMove(GameMoveRequestBean bean);
    public abstract GameMoveResponseBean toGameMoveResponseBean(Move move);
    public abstract List<GameMoveResponseBean> toGameMoveResponseBean(List<Move> moves);

}
