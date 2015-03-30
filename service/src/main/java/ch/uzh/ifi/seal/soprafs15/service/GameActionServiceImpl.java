package ch.uzh.ifi.seal.soprafs15.service;

import ch.uzh.ifi.seal.soprafs15.model.Game;
import ch.uzh.ifi.seal.soprafs15.model.User;
import ch.uzh.ifi.seal.soprafs15.model.repositories.GameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Marco
 */
@Component("gameActionService")
public class GameActionServiceImpl extends GameActionService {

    Logger logger = LoggerFactory.getLogger(GameActionServiceImpl.class);

    protected GameRepository gameRepository;

    @Autowired
    public GameActionServiceImpl(GameRepository gameRepository){
        this.gameRepository = gameRepository;
    }

    @Override
    public Game startGame(Long id, User user) {
        return null;
    }

    @Override
    public Game stopGame(Long id, User user) {
        return null;
    }

    @Override
    public Game startFastMode(Long id, User user) {
        return null;
    }
}
