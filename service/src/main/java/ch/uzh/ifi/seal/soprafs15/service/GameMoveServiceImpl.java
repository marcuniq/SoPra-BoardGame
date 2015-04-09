package ch.uzh.ifi.seal.soprafs15.service;

import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameMoveRequestBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameMoveResponseBean;
import ch.uzh.ifi.seal.soprafs15.model.game.Game;
import ch.uzh.ifi.seal.soprafs15.model.move.Move;
import ch.uzh.ifi.seal.soprafs15.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs15.model.repositories.MoveRepository;
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
    protected GameMapperService gameMapperService;

    @Autowired
    public GameMoveServiceImpl(MoveRepository moveRepository, GameRepository gameRepository, GameMapperService gameMapperService){
        this.moveRepository = moveRepository;
        this.gameRepository = gameRepository;
        this.gameMapperService = gameMapperService;
    }

    @Override
    public List<GameMoveResponseBean> listMoves(Long gameId) {
        Game game = gameRepository.findOne(gameId);

        if(game != null) {
            return gameMapperService.toGameMoveResponseBean(game.getMoves());
        }
        return null;
    }

    @Override
    public GameMoveResponseBean addMove(Long gameId, GameMoveRequestBean bean) {
        Game game = gameRepository.findOne(gameId);
        //Move move = gameMapperService.toMove(game, bean);
        Move move = null;

        if(game != null && move != null) {
            game.addMove(move);

            //gameRepository.save(game);

            //return gameMapperService.toGameMoveResponseBean(move);
        }
        return null;
    }

    @Override
    public GameMoveResponseBean getMove(Long gameId, Long moveId) {
        Move move = moveRepository.findOne(moveId);

        if(move != null) {
            return gameMapperService.toGameMoveResponseBean(move);
        }
        return null;
    }
}
