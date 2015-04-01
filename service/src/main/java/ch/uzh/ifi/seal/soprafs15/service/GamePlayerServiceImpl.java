package ch.uzh.ifi.seal.soprafs15.service;

import ch.uzh.ifi.seal.soprafs15.GameConstants;
import ch.uzh.ifi.seal.soprafs15.model.User;
import ch.uzh.ifi.seal.soprafs15.model.game.Game;
import ch.uzh.ifi.seal.soprafs15.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs15.model.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Marco
 */

@Service("gamePlayerService")
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
    public User addPlayer(Long gameId, User player) {
        Game game = gameRepository.findOne(gameId);

        if(game != null && player != null && game.getPlayers().size() < GameConstants.MAX_PLAYERS) {
            game.addPlayer(player);

            logger.debug("Game: " + game.getName() + " - player added: " + player.getUsername());

            return player;
        } else {
            logger.error("Error adding player with token: " + player.getToken());
        }
        return null;
    }

    @Override
    public User getPlayer(Long gameId, Integer playerId) {
        Game game = gameRepository.findOne(gameId);
        User player = game.getPlayers().get(playerId);

        return player;
    }
}
