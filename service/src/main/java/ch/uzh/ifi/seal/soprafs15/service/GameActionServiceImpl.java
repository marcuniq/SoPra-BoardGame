package ch.uzh.ifi.seal.soprafs15.service;

import ch.uzh.ifi.seal.soprafs15.model.User;
import ch.uzh.ifi.seal.soprafs15.model.game.Game;
import ch.uzh.ifi.seal.soprafs15.model.repositories.GameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Marco
 */
@Service("gameActionService")
public class GameActionServiceImpl extends GameActionService {

    Logger logger = LoggerFactory.getLogger(GameActionServiceImpl.class);

    protected GameRepository gameRepository;

    @Autowired
    public GameActionServiceImpl(GameRepository gameRepository){
        this.gameRepository = gameRepository;
    }

    @Override
    public Game startGame(Long gameId, User user) {
        Game game = gameRepository.findOne(gameId);

        if(user != null && game != null && game.getOwner().equals(user.getUsername())) {
            //TODO: Start game

        }

        return game;
    }

    @Override
    public Game stopGame(Long gameId, User user) {
        Game game = gameRepository.findOne(gameId);

        if(user != null && game != null && game.getOwner().equals(user.getUsername())) {
            //TODO: Stop game

        }

        return game;
    }

    @Override
    public Game startFastMode(Long gameId, User user) {
        Game game = gameRepository.findOne(gameId);

        if(user != null && game != null && game.getOwner().equals(user.getUsername())) {
            //TODO: Start fast mode

        }

        return game;
    }
}
