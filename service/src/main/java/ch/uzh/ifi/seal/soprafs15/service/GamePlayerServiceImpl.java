package ch.uzh.ifi.seal.soprafs15.service;

import ch.uzh.ifi.seal.soprafs15.model.Game;
import ch.uzh.ifi.seal.soprafs15.model.User;
import ch.uzh.ifi.seal.soprafs15.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs15.model.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Marco
 */

@Component("gamePlayerService")
public class GamePlayerServiceImpl extends GamePlayerService {

    Logger logger = LoggerFactory.getLogger(GamePlayerServiceImpl.class);

    protected GameRepository gameRepository;
    protected UserRepository userRepository;

    @Autowired
    public GamePlayerServiceImpl(GameRepository gameRepository, UserRepository userRepository){
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<User> listPlayer(Long gameId) {
        return gameRepository.findOne(gameId).getPlayers();
    }

    @Override
    public User addPlayer(Long gameId, User user) {
        Game game = gameRepository.findOne(gameId);
        
        return null;
    }

    @Override
    public User getPlayer(Long gameId, User user) {
        return null;
    }
}
