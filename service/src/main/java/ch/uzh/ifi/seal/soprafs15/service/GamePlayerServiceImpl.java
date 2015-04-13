package ch.uzh.ifi.seal.soprafs15.service;

import ch.uzh.ifi.seal.soprafs15.GameConstants;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GamePlayerRequestBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GamePlayerResponseBean;
import ch.uzh.ifi.seal.soprafs15.model.User;
import ch.uzh.ifi.seal.soprafs15.model.game.Game;
import ch.uzh.ifi.seal.soprafs15.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs15.model.repositories.UserRepository;
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
@Service("gamePlayerService")
public class GamePlayerServiceImpl extends GamePlayerService {

    protected Logger logger = LoggerFactory.getLogger(GamePlayerServiceImpl.class);

    protected GameRepository gameRepository;
    protected UserRepository userRepository;
    protected GameMapperService gameMapperService;

    @Autowired
    public GamePlayerServiceImpl(GameRepository gameRepository, UserRepository userRepository, GameMapperService gameMapperService){
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.gameMapperService = gameMapperService;
    }

    @Override
    public List<GamePlayerResponseBean> listPlayer(Long gameId) {
        List<User> players = gameRepository.findOne(gameId).getPlayers();

        return gameMapperService.toGamePlayerResponseBean(players);
    }

    @Override
    @Transactional
    public GamePlayerResponseBean addPlayer(Long gameId, GamePlayerRequestBean bean) {
        User player = gameMapperService.toUser(bean);

        // find game
        Game game = gameRepository.findOne(gameId);

        if(game != null && game.getPlayers().size() < GameConstants.MAX_PLAYERS) {

            // initialize player for game play & save
            player.initForGamePlay();
            game.addPlayer(player);

            logger.debug("Game: " + game.getName() + " - player added: " + player.getUsername());

            return getPlayer(gameId, player.getId());
        } else {
            logger.error("Error adding player with token: " + player.getToken());
        }
        return null;
    }

    @Override
    public GamePlayerResponseBean getPlayer(Long gameId, Long playerId) {
        Game game = gameRepository.findOne(gameId);
        User player = game.getPlayers().stream().filter(p -> p.getId() == playerId).findFirst().get();

        return gameMapperService.toGamePlayerResponseBean(player);
    }

    private static int safeLongToInt(long l) {
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
            throw new IllegalArgumentException
                    (l + " cannot be cast to int without changing its value.");
        }
        return (int) l;
    }
}
