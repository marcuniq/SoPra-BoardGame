package ch.uzh.ifi.seal.soprafs15.service;

import ch.uzh.ifi.seal.soprafs15.model.game.Game;
import ch.uzh.ifi.seal.soprafs15.model.move.Move;
import ch.uzh.ifi.seal.soprafs15.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs15.model.repositories.MoveRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Marco
 */

@Service("gameMoveService")
public class GameMoveServiceImpl extends GameMoveService {

    Logger logger = LoggerFactory.getLogger(GameMoveServiceImpl.class);

    protected MoveRepository moveRepository;
    protected GameRepository gameRepository;

    @Autowired
    public GameMoveServiceImpl(MoveRepository moveRepository){
        this.moveRepository = moveRepository;
    }

    @Override
    public List<Move> listMoves(Long gameId) {
        Game game = gameRepository.findOne(gameId);

        if(game != null) {
            return game.getMoves();
        }
        return null;
    }

    @Override
    public Move addMove(Long gameId, Move move) {
        Game game = gameRepository.findOne(gameId);
        //TODO addMove

        return null;
    }

    @Override
    public Move getMove(Long gameId, Long moveId) {
        return null;
    }
}
