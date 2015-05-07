package ch.uzh.ifi.seal.soprafs15.service;

import ch.uzh.ifi.seal.soprafs15.controller.beans.game.*;
import ch.uzh.ifi.seal.soprafs15.model.User;
import ch.uzh.ifi.seal.soprafs15.model.game.Game;
import ch.uzh.ifi.seal.soprafs15.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs15.model.repositories.UserRepository;
import ch.uzh.ifi.seal.soprafs15.service.exceptions.GameNotFoundException;
import ch.uzh.ifi.seal.soprafs15.service.exceptions.UserNotFoundException;
import ch.uzh.ifi.seal.soprafs15.service.mapper.GameMapperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Marco
 */
@Transactional
@Service("gameService")
public class GameServiceImpl extends GameService {

    private final Logger logger = LoggerFactory.getLogger(GameServiceImpl.class);

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
    public List<GameResponseBean> listGames(String status) {
        List<Game> games = (List<Game>) gameRepository.findAll();

        if(status != null)
            games = games.stream().filter(game -> game.getStatus() == GameStatus.valueOf(status)).collect(Collectors.toList());

        return gameMapperService.toGameResponseBean(games);
    }

    @Override
    public GameCreateResponseBean addGame(GameRequestBean bean) {
        Game game = gameMapperService.toGame(bean);

        // add owner to player list
        User owner = game.getOwner();
        //owner.initForGamePlay();
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

        if (owner == null){
            throw new UserNotFoundException(bean.getToken(), UserServiceImpl.class);
        }

        if(game.getOwner().getId().equals(owner.getId())) {
            gameRepository.delete(game);
            //gameRepository.deleteAll();
        }
    }
}
