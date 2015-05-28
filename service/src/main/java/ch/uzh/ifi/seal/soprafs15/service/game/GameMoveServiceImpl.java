package ch.uzh.ifi.seal.soprafs15.service.game;

import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameMoveRequestBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameMoveResponseBean;
import ch.uzh.ifi.seal.soprafs15.model.User;
import ch.uzh.ifi.seal.soprafs15.model.game.Game;
import ch.uzh.ifi.seal.soprafs15.model.move.Move;
import ch.uzh.ifi.seal.soprafs15.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs15.model.repositories.MoveRepository;
import ch.uzh.ifi.seal.soprafs15.model.repositories.UserRepository;
import ch.uzh.ifi.seal.soprafs15.service.exceptions.GameNotFoundException;
import ch.uzh.ifi.seal.soprafs15.service.exceptions.MoveMappingException;
import ch.uzh.ifi.seal.soprafs15.service.exceptions.MoveNotFoundException;
import ch.uzh.ifi.seal.soprafs15.service.exceptions.UserNotFoundException;
import ch.uzh.ifi.seal.soprafs15.service.mapper.GameMapperService;
import ch.uzh.ifi.seal.soprafs15.service.pusher.PusherService;
import ch.uzh.ifi.seal.soprafs15.service.pusher.events.MoveEvent;
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
@Service("gameMoveService")
public class GameMoveServiceImpl extends GameMoveService {

    private final Logger logger = LoggerFactory.getLogger(GameMoveServiceImpl.class);

    protected MoveRepository moveRepository;
    protected GameRepository gameRepository;
    protected UserRepository userRepository;

    protected GameMapperService gameMapperService;

    protected GameLogicService gameLogicService;

    protected PusherService pusherService;

    @Autowired
    public GameMoveServiceImpl(MoveRepository moveRepository, GameRepository gameRepository,
                               UserRepository userRepository,
                               GameMapperService gameMapperService, GameLogicService gameLogicService,
                               PusherService pusherService){
        this.moveRepository = moveRepository;
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.gameMapperService = gameMapperService;
        this.gameLogicService = gameLogicService;
        this.pusherService = pusherService;
    }

    @Override
    public List<GameMoveResponseBean> listMoves(Long gameId) throws GameNotFoundException {
        logger.debug("list moves");
        Game game = gameRepository.findOne(gameId);

        if(game == null) {
            throw new GameNotFoundException(gameId, GameMoveServiceImpl.class);
        }

        List<Move> moves = moveRepository.findByGame(game);

        return gameMapperService.toGameMoveResponseBean(moves);
    }

    @Override
    public GameMoveResponseBean addMove(Long gameId, GameMoveRequestBean bean) {
        logger.debug("add move, gameId: " + gameId);

        // find game and player, map bean to move
        Game game = gameRepository.findOne(gameId);
        User player = userRepository.findByToken(bean.getToken());
        Move move = gameMapperService.toMove(game, player, bean);

        if(game == null) {
            throw new GameNotFoundException(gameId, GameMoveServiceImpl.class);
        }
        if(player == null) {
            throw new UserNotFoundException(bean.getToken(), GameMoveServiceImpl.class);
        }
        if(move == null) {
            throw new MoveMappingException(GameMoveServiceImpl.class);
        }

        // execute game logic with move
        move = gameLogicService.processMove(game, player, move);

        // save move to repo and add to game
        move = (Move) moveRepository.save(move);
        game.addMove(move);

        // notify all players about move
        MoveEvent moveEvent = new MoveEvent(move.getId());
        pusherService.pushToSubscribers(moveEvent, game);

        return getMove(move.getId());
    }

    @Override
    public GameMoveResponseBean getMove(Long moveId) {
        Move move = (Move) moveRepository.findOne(moveId);

        if(move == null) {
            throw new MoveNotFoundException(moveId, GameMoveServiceImpl.class);
        }

        return gameMapperService.toGameMoveResponseBean(move);
    }
}
