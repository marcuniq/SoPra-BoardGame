package ch.uzh.ifi.seal.soprafs15.service;

import ch.uzh.ifi.seal.soprafs15.controller.GenericService;
import ch.uzh.ifi.seal.soprafs15.model.User;
import ch.uzh.ifi.seal.soprafs15.model.game.Game;

import java.util.List;

/**
 * @author Marco
 */
public abstract class GameService extends GenericService {

    public abstract List<Game> listGames();
    public abstract Game addGame(Game game);
    public abstract Game getGame(Long gameId);
    public abstract void deleteGame(Long gameId, User user);
}
