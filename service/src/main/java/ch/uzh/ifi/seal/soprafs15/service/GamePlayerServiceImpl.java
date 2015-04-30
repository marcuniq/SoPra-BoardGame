package ch.uzh.ifi.seal.soprafs15.service;

import ch.uzh.ifi.seal.soprafs15.GameConstants;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameAddPlayerResponseBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GamePlayerRequestBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GamePlayerResponseBean;
import ch.uzh.ifi.seal.soprafs15.model.User;
import ch.uzh.ifi.seal.soprafs15.model.game.Game;
import ch.uzh.ifi.seal.soprafs15.model.game.RaceBettingCard;
import ch.uzh.ifi.seal.soprafs15.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs15.model.repositories.UserRepository;
import ch.uzh.ifi.seal.soprafs15.service.exceptions.GameFullException;
import ch.uzh.ifi.seal.soprafs15.service.exceptions.GameNotFoundException;
import ch.uzh.ifi.seal.soprafs15.service.exceptions.UserNotFoundException;
import ch.uzh.ifi.seal.soprafs15.service.mapper.GameMapperService;
import ch.uzh.ifi.seal.soprafs15.service.pusher.PusherService;
import ch.uzh.ifi.seal.soprafs15.service.pusher.events.PlayerJoinedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
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
    protected PusherService pusherService;

    @Autowired
    public GamePlayerServiceImpl(GameRepository gameRepository, UserRepository userRepository,
                                 GameMapperService gameMapperService, PusherService pusherService){
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.gameMapperService = gameMapperService;
        this.pusherService = pusherService;
    }

    @Override
    @Transactional
    public List<GamePlayerResponseBean> listPlayer(Long gameId) {
        List<User> players = gameRepository.findOne(gameId).getPlayers();

        return gameMapperService.toGamePlayerResponseBean(players);
    }

    @Override
    @Transactional
    public GameAddPlayerResponseBean addPlayer(Long gameId, GamePlayerRequestBean bean) {
        User player = gameMapperService.toUser(bean);

        // find game
        Game game = gameRepository.findOne(gameId);

        if (player == null){
            throw new UserNotFoundException(bean.getToken(), true, UserServiceImpl.class);
        }
        if(game == null) {
            throw new GameNotFoundException(gameId, GamePlayerServiceImpl.class);
        }

        if(game.getPlayers().size() >= GameConstants.MAX_PLAYERS) {
            throw new GameFullException(game, GamePlayerServiceImpl.class);
        }

        // initialize player for game play & save
        //player.initForGamePlay();
        game.addPlayer(player);

        // notify players in lobby
        pusherService.pushToSubscribers(new PlayerJoinedEvent(player.getId()), game);

        return gameMapperService.toGameAddPlayerResponseBean(player, game);
    }

    @Override
    @Transactional
    public GamePlayerResponseBean getPlayer(Long gameId, Integer playerId) {
        Game game = gameRepository.findOne(gameId);

        if(game == null) {
            throw new GameNotFoundException(gameId, GamePlayerServiceImpl.class);
        }

        User player = game.getPlayers().stream().filter(p -> p.getPlayerId() == playerId).findFirst().get();

        return gameMapperService.toGamePlayerResponseBean(player);
    }

    @Override
    public List<RaceBettingCard> getRaceBettingCards(Long gameId, Integer playerId) {
        Game game = gameRepository.findOne(gameId);

        if(game == null) {
            throw new GameNotFoundException(gameId, GamePlayerServiceImpl.class);
        }

        User player = game.getPlayers().stream().filter(p -> p.getPlayerId() == playerId).findFirst().get();

        return new ArrayList<>(player.getRaceBettingCards().values());
    }
}
