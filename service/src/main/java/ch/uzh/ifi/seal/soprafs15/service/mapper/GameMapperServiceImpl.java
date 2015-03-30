package ch.uzh.ifi.seal.soprafs15.service.mapper;

import ch.uzh.ifi.seal.soprafs15.controller.beans.game.*;
import ch.uzh.ifi.seal.soprafs15.model.Game;
import ch.uzh.ifi.seal.soprafs15.model.Move;
import ch.uzh.ifi.seal.soprafs15.model.User;
import ch.uzh.ifi.seal.soprafs15.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs15.model.repositories.MoveRepository;
import ch.uzh.ifi.seal.soprafs15.model.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Marco
 */

@Component("gameMapperService")
public class GameMapperServiceImpl extends GameMapperService {

    Logger logger = LoggerFactory.getLogger(GameMapperServiceImpl.class);

    protected UserRepository userRepository;
    protected GameRepository gameRepository;
    protected MoveRepository moveRepository;

    @Autowired
    public GameMapperServiceImpl(UserRepository userRepository, GameRepository gameRepository, MoveRepository moveRepository){
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
        this.moveRepository = moveRepository;
    }

    @Override
    public Game toGame(GameRequestBean bean) {
        Game game = new Game();
        game.setName(bean.getName());
        game.setOwner("test");

        return game;
    }

    @Override
    public GameResponseBean toGameResponseBean(Game game) {
        return null;
    }

    @Override
    public List<GameResponseBean> toGameResponseBean(List<Game> games) {
        List<GameResponseBean> result = new ArrayList<>();

        GameResponseBean tmpGameResponseBean;
        for(Game game : games) {
            tmpGameResponseBean = new GameResponseBean();

            tmpGameResponseBean.setId(game.getId());
            tmpGameResponseBean.setName(game.getName());
            tmpGameResponseBean.setOwner(game.getOwner());
            //tmpGameResponseBean.setStatus(game.getStatus());
            //tmpGameResponseBean.setNumberOfMoves(game.getMoves().size());
            //tmpGameResponseBean.setNumberOfPlayers(game.getPlayers().size());
            //tmpGameResponseBean.setNextPlayer(game.getNextPlayer().getUsername());

            result.add(tmpGameResponseBean);
        }

        return result;
    }

    @Override
    public User toUser(GamePlayerRequestBean bean) {
        return null;
    }

    @Override
    public GamePlayerResponseBean toGamePlayerResponseBean(User user) {
        return null;
    }

    @Override
    public List<GamePlayerResponseBean> toGamePlayerResponseBean(List<User> users) {
        return null;
    }

    @Override
    public Move toMove(GameMoveRequestBean bean) {
        return null;
    }

    @Override
    public GameMoveResponseBean toGameMoveResponseBean(Move move) {
        return null;
    }

    @Override
    public List<GameMoveResponseBean> toGameMoveResponseBean(List<Move> moves) {
        return null;
    }
}
