package ch.uzh.ifi.seal.soprafs15.service;

import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameMoveRequestBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameMoveResponseBean;
import ch.uzh.ifi.seal.soprafs15.model.User;
import ch.uzh.ifi.seal.soprafs15.model.game.Game;
import ch.uzh.ifi.seal.soprafs15.model.move.Move;
import ch.uzh.ifi.seal.soprafs15.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs15.model.repositories.MoveRepository;
import ch.uzh.ifi.seal.soprafs15.model.repositories.UserRepository;
import ch.uzh.ifi.seal.soprafs15.service.exceptions.GameNotFoundException;
import ch.uzh.ifi.seal.soprafs15.service.exceptions.InvalidMoveException;
import ch.uzh.ifi.seal.soprafs15.service.exceptions.PlayerTurnException;
import ch.uzh.ifi.seal.soprafs15.service.exceptions.UserNotFoundException;
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
@Service("gameMoveService")
public class GameMoveServiceImpl extends GameMoveService {

    Logger logger = LoggerFactory.getLogger(GameMoveServiceImpl.class);

    protected MoveRepository moveRepository;
    protected GameRepository gameRepository;
    protected UserRepository userRepository;

    protected GameMapperService gameMapperService;
    protected GameLogicService gameLogicService;

    @Autowired
    public GameMoveServiceImpl(MoveRepository moveRepository, GameRepository gameRepository,
                               UserRepository userRepository,
                               GameMapperService gameMapperService, GameLogicService gameLogicService){
        this.moveRepository = moveRepository;
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.gameMapperService = gameMapperService;
        this.gameLogicService = gameLogicService;
    }

    @Override
    public List<GameMoveResponseBean> listMoves(Long gameId) throws GameNotFoundException {
        logger.debug("list moves");
        Game game = gameRepository.findOne(gameId);

        if(game == null)
            throw new GameNotFoundException(game);

        return gameMapperService.toGameMoveResponseBean(game.getMoves());
    }

    @Override
    public GameMoveResponseBean addMove(Long gameId, GameMoveRequestBean bean) throws PlayerTurnException, GameNotFoundException, UserNotFoundException, InvalidMoveException {
        logger.debug("add move, gameId: " + gameId);

        // find game and player, map bean to move
        Game game = gameRepository.findOne(gameId);
        User player = userRepository.findByToken(bean.getToken());
        Move move = gameMapperService.toMove(game, player, bean);

        if(game == null)
            throw new GameNotFoundException(game);

        if(player == null)
            throw new UserNotFoundException(player);

        if(move == null)
            throw new InvalidMoveException(move);

        // quick hack to initialize player, only temporary for testing
        //player.initForGamePlay();

        // execute game logic with move
        move = gameLogicService.processMove(game, player, move);

        // save move to repo and add to game
        move = (Move) moveRepository.save(move);
        game.addMove(move);

        return getMove(move.getId());
    }

    @Override
    public GameMoveResponseBean getMove(Long moveId) {
        Move move = (Move) moveRepository.findOne(moveId);

        if(move != null) {
            return gameMapperService.toGameMoveResponseBean(move);
        }
        return null;
    }
}
