package ch.uzh.ifi.seal.soprafs15.service.game;

import ch.uzh.ifi.seal.soprafs15.GameConstants;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameMoveResponseBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GamePlayerRequestBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameResponseBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameStatus;
import ch.uzh.ifi.seal.soprafs15.model.User;
import ch.uzh.ifi.seal.soprafs15.model.game.Game;
import ch.uzh.ifi.seal.soprafs15.model.move.Move;
import ch.uzh.ifi.seal.soprafs15.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs15.service.exceptions.*;
import ch.uzh.ifi.seal.soprafs15.service.mapper.GameMapperService;
import ch.uzh.ifi.seal.soprafs15.service.pusher.PusherService;
import ch.uzh.ifi.seal.soprafs15.service.pusher.events.GameStartEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
            throw new UserNotFoundException(bean.getToken(), GameActionServiceImpl.class);
        }

        if(owner.getId() != game.getOwner().getId()){
            throw new NotAuthorizedException("You can't start the game, you are not the owner",GameActionServiceImpl.class);
        }

        if(game.getPlayers().size() < GameConstants.MIN_PLAYERS) {
            throw new NotEnoughPlayerException(game, GameActionServiceImpl.class);
        }

        // initialize game for playing
        game.initForGamePlay();

        // create player sequence
        Map<Long, Integer> userIdToPlayerIdMap = gameLogicService.createPlayerSequence(game);

        // send game start event
        pusherService.pushToSubscribers(new GameStartEvent(userIdToPlayerIdMap), game);

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
            throw new UserNotFoundException(bean.getToken(), GameActionServiceImpl.class);
        }

        if(owner.getId() != game.getOwner().getId()){
            throw new NotAuthorizedException("You can't start the game, you are not the owner",GameActionServiceImpl.class);
        }

        //TODO: Stop game


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
            throw new UserNotFoundException(bean.getToken(), GameActionServiceImpl.class);
        }

        if(owner.getId() != game.getOwner().getId()){
            throw new NotAuthorizedException("You can't start the game, you are not the owner",GameActionServiceImpl.class);
        }

        // start fast mode loop
        gameLogicService.startFastMode(game);

        // send game start event
        pusherService.pushToSubscribers(new GameStartEvent(), game);

        return gameMapperService.toGameResponseBean(game);
    }

    @Override
    public GameMoveResponseBean triggerMoveInFastMode(Long gameId, GamePlayerRequestBean bean) {
        // find game and owner
        Game game = gameRepository.findOne(gameId);
        User owner = gameMapperService.toUser(bean);

        if(game == null) {
            throw new GameNotFoundException(gameId, GameActionServiceImpl.class);
        }

        if(owner == null) {
            throw new UserNotFoundException(bean.getToken(), GameActionServiceImpl.class);
        }

        if(owner.getId() != game.getOwner().getId()){
            throw new NotAuthorizedException("You can't trigger a move, you are not the owner",GameActionServiceImpl.class);
        }

        // trigger move
        Move move = gameLogicService.triggerMoveInFastMode(game);

        return gameMapperService.toGameMoveResponseBean(move);
    }
}
