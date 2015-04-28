package ch.uzh.ifi.seal.soprafs15.service;

import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameCreateResponseBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GamePlayerRequestBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameRequestBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameResponseBean;
import ch.uzh.ifi.seal.soprafs15.model.User;
import ch.uzh.ifi.seal.soprafs15.model.game.Game;
import ch.uzh.ifi.seal.soprafs15.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs15.model.repositories.UserRepository;
import ch.uzh.ifi.seal.soprafs15.service.exceptions.GameNotFoundException;
import ch.uzh.ifi.seal.soprafs15.service.mapper.GameMapperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Marco
 */

@Transactional
@Service("gameService")
public class GameServiceImpl extends GameService {

    Logger logger = LoggerFactory.getLogger(GameServiceImpl.class);

    protected GameRepository gameRepository;
    protected UserRepository userRepository;
    protected GameMapperService gameMapperService;

    @Autowired
    public GameServiceImpl(GameRepository gameRepository, UserRepository userRepository, GameMapperService gameMapperService){
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.gameMapperService = gameMapperService;
    }

    @Override
    public List<GameResponseBean> listGames() {
        List<Game> games = (List<Game>) gameRepository.findAll();
        return gameMapperService.toGameResponseBean(games);
    }

    @Override
    public GameCreateResponseBean addGame(GameRequestBean bean) {
        Game game = gameMapperService.toGame(bean);

        // add owner to player list
        String username = game.getOwner();
        User owner = userRepository.findByUsername(username);
        owner.initForGamePlay();
        game.addPlayer(owner);

        game = gameRepository.save(game);
        return gameMapperService.toGameCreateResponseBean(game);
    }

    @Override
    public GameResponseBean getGame(Long gameId) {
        Game game = gameRepository.findOne(gameId);

        if(game == null) {
            throw new GameNotFoundException(gameId, GameServiceImpl.class);
        }

        return gameMapperService.toGameResponseBean(game);
    }

    @Override
    public void deleteGame(Long gameId, GamePlayerRequestBean bean) {
        User owner = gameMapperService.toUser(bean);
        Game game = gameRepository.findOne(gameId);

        if(game == null) {
            throw new GameNotFoundException(gameId, GameServiceImpl.class);
        }

        if(game != null && game.getOwner().equals(owner.getUsername()))
            gameRepository.delete(gameId);
    }
}
