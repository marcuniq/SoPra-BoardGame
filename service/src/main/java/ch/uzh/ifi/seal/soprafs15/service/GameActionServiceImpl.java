package ch.uzh.ifi.seal.soprafs15.service;

import ch.uzh.ifi.seal.soprafs15.GameConstants;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GamePlayerRequestBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameResponseBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameStatus;
import ch.uzh.ifi.seal.soprafs15.model.User;
import ch.uzh.ifi.seal.soprafs15.model.game.Game;
import ch.uzh.ifi.seal.soprafs15.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs15.service.exceptions.GameNotFoundException;
import ch.uzh.ifi.seal.soprafs15.service.exceptions.NotEnoughPlayerException;
import ch.uzh.ifi.seal.soprafs15.service.exceptions.OwnerNotFoundException;
import ch.uzh.ifi.seal.soprafs15.service.mapper.GameMapperService;
import ch.uzh.ifi.seal.soprafs15.service.pusher.PusherService;
import ch.uzh.ifi.seal.soprafs15.service.pusher.events.GameStartEvent;
import ch.uzh.ifi.seal.soprafs15.service.pusher.events.PlayerSequenceEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * @author Marco
 */
@Transactional
@Service("gameActionService")
public class GameActionServiceImpl extends GameActionService {

    private final Logger logger = LoggerFactory.getLogger(GameActionServiceImpl.class);

    protected GameRepository gameRepository;
    protected GameMapperService gameMapperService;
    protected GameLogicService gameLogicService;
    protected PusherService pusherService;

    @Autowired
    public GameActionServiceImpl(GameRepository gameRepository, GameMapperService gameMapperService,
                                 GameLogicService gameLogicService, PusherService pusherService){
        this.gameRepository = gameRepository;
        this.gameMapperService = gameMapperService;
        this.gameLogicService = gameLogicService;
        this.pusherService = pusherService;
    }

    @Override
    public GameResponseBean startGame(Long gameId, GamePlayerRequestBean bean) {
        Game game = gameRepository.findOne(gameId);
        User owner = gameMapperService.toUser(bean);


        if(game == null) {
            throw new GameNotFoundException(gameId, GameActionServiceImpl.class);
        }

        if(owner == null) {
            throw new OwnerNotFoundException(owner.getId(), GameActionServiceImpl.class);
        }

        if(game.getPlayers().size() < GameConstants.MIN_PLAYERS) {
            throw new NotEnoughPlayerException(game, GameActionServiceImpl.class);
        }



        //TODO: Start game

        game.initForGamePlay();
        game.setStatus(GameStatus.RUNNING);

        // create player sequence
        Map<Long, Integer> userIdToPlayerIdMap = gameLogicService.createPlayerSequence(game);

        // let players know about the player id's of all players
        pusherService.pushToSubscribers(new PlayerSequenceEvent(userIdToPlayerIdMap), game);

        // send game start event
        pusherService.pushToSubscribers(new GameStartEvent(), game);

        return gameMapperService.toGameResponseBean(game);
    }

    @Override
    public GameResponseBean stopGame(Long gameId, GamePlayerRequestBean bean) {
        Game game = gameRepository.findOne(gameId);
        User owner = gameMapperService.toUser(bean);

        if(game == null) {
            throw new GameNotFoundException(gameId, GameActionServiceImpl.class);
        }

        if(owner == null) {
            throw new OwnerNotFoundException(owner.getId(), GameActionServiceImpl.class);
        }

        if(owner != null && game != null && game.getOwner().equals(owner.getUsername())) {
            //TODO: Stop game

        }

        return gameMapperService.toGameResponseBean(game);
    }

    @Override
    public GameResponseBean startFastMode(Long gameId, GamePlayerRequestBean bean) {
        Game game = gameRepository.findOne(gameId);
        User owner = gameMapperService.toUser(bean);

        if(game == null) {
            throw new GameNotFoundException(gameId, GameActionServiceImpl.class);
        }

        if(owner == null) {
            throw new OwnerNotFoundException(owner.getId(), GameActionServiceImpl.class);
        }

        if(owner != null && game != null && game.getOwner().equals(owner.getUsername())) {
            //TODO: Start fast mode

        }

        return gameMapperService.toGameResponseBean(game);
    }
}
