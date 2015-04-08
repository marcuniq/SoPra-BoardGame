package ch.uzh.ifi.seal.soprafs15.service;

import ch.uzh.ifi.seal.soprafs15.GameConstants;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GamePlayerRequestBean;
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

@Service("gamePlayerService")
public class GamePlayerServiceImpl extends GamePlayerService {

    protected Logger logger = LoggerFactory.getLogger(GamePlayerServiceImpl.class);

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
    @Transactional
    public User addPlayer(Long gameId, GamePlayerRequestBean bean) {

        // find player
        User player = userRepository.findByToken(bean.getToken());

        // find game
        Game game = gameRepository.findOne(gameId);

        if(game != null && game.getPlayers().size() < GameConstants.MAX_PLAYERS) {

            // initialize player for game play & save
            player.init();
            game.addPlayer(player);

            logger.debug("Game: " + game.getName() + " - player added: " + player.getUsername());

            return getPlayer(gameId, player.getId());
        } else {
            logger.error("Error adding player with token: " + player.getToken());
        }
        return null;
    }

    @Override
    public User getPlayer(Long gameId, Long playerId) {
        Game game = gameRepository.findOne(gameId);
        User player = game.getPlayers().stream().filter(p -> p.getId() == playerId).findFirst().get();

        return player;
    }

    private static int safeLongToInt(long l) {
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
            throw new IllegalArgumentException
                    (l + " cannot be cast to int without changing its value.");
        }
        return (int) l;
    }
}
