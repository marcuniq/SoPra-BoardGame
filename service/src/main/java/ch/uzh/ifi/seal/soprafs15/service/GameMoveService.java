package ch.uzh.ifi.seal.soprafs15.service;

import ch.uzh.ifi.seal.soprafs15.controller.GenericService;
import ch.uzh.ifi.seal.soprafs15.model.Move;

import java.util.List;

/**
 * @author Marco
 */
public abstract class GameMoveService extends GenericService {

    public abstract List<Move> listMoves();
    public abstract Move addMove(Long gameId,Move move);
    public abstract Move getMove(Long gameId, Long moveId);
}
