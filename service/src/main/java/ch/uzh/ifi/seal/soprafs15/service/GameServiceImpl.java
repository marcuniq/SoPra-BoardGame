package ch.uzh.ifi.seal.soprafs15.service;

import ch.uzh.ifi.seal.soprafs15.model.User;
import ch.uzh.ifi.seal.soprafs15.model.game.Game;
import ch.uzh.ifi.seal.soprafs15.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs15.model.repositories.UserRepository;
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

    @Autowired
    public GameServiceImpl(GameRepository gameRepository, UserRepository userRepository){
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Game> listGames() {
        return (List<Game>) gameRepository.findAll();
    }

    @Override
    public Game addGame(Game game) {

        // add owner to player list
        String username = game.getOwner();
        User owner = userRepository.findByUsername(username);
        game.addPlayer(owner);

        // save game
        game = gameRepository.save(game);

        return game;
    }

    @Override
    public Game getGame(Long gameId) {
        return gameRepository.findOne(gameId);
    }

    @Override
    public void deleteGame(Long gameId, User user) {

        Game game = gameRepository.findOne(gameId);

        if(game.getOwner().equals(user.getUsername()))
            gameRepository.delete(gameId);
    }
}
