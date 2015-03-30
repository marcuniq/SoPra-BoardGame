package ch.uzh.ifi.seal.soprafs15.service;

import ch.uzh.ifi.seal.soprafs15.model.Game;
import ch.uzh.ifi.seal.soprafs15.model.User;
import ch.uzh.ifi.seal.soprafs15.model.repositories.GameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Marco
 */

@Component("gameService")
public class GameServiceImpl extends GameService {

    Logger logger = LoggerFactory.getLogger(GameServiceImpl.class);

    protected GameRepository gameRepository;

    @Autowired
    public GameServiceImpl(GameRepository gameRepository){
        this.gameRepository = gameRepository;
    }

    @Override
    public List<Game> listGames() {
        return (List<Game>) gameRepository.findAll();
    }

    @Override
    public Game addGame(Game game) {
        return gameRepository.save(game);
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
