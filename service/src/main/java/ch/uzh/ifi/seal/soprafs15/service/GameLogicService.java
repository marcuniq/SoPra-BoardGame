package ch.uzh.ifi.seal.soprafs15.service;

import ch.uzh.ifi.seal.soprafs15.controller.GenericService;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.MoveEnum;
import ch.uzh.ifi.seal.soprafs15.model.User;
import ch.uzh.ifi.seal.soprafs15.model.game.Game;
import ch.uzh.ifi.seal.soprafs15.model.move.Move;

import java.util.Map;

/**
 * @author Marco
 */
public abstract class GameLogicService extends GenericService {

    public abstract Map<Long, Integer> createPlayerSequence(Game game);
    public abstract Move processMove(Game game, User player, Move move);
    public abstract void startFastMode(Game game);
    public abstract void stopFastMode(Game game);

}
