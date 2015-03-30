package ch.uzh.ifi.seal.soprafs15.service;

import ch.uzh.ifi.seal.soprafs15.model.Move;
import ch.uzh.ifi.seal.soprafs15.model.repositories.MoveRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Marco
 */

@Component("gameMoveService")
public class GameMoveServiceImpl extends GameMoveService {

    Logger logger = LoggerFactory.getLogger(GameMoveServiceImpl.class);

    protected MoveRepository moveRepository;

    @Autowired
    public GameMoveServiceImpl(MoveRepository moveRepository){
        this.moveRepository = moveRepository;
    }

    @Override
    public List<Move> listMoves() {
        return null;
    }

    @Override
    public Move addMove(Long gameId, Move move) {
        return null;
    }

    @Override
    public Move getMove(Long gameId, Long moveId) {
        return null;
    }
}