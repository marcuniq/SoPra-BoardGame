package ch.uzh.ifi.seal.soprafs15.service;

import java.util.List;

import ch.uzh.ifi.seal.soprafs15.controller.GenericService;
import ch.uzh.ifi.seal.soprafs15.model.Game;
import ch.uzh.ifi.seal.soprafs15.model.User;

/**
 * @author Marco
 */
public abstract class GameService extends GenericService {

    public abstract List<Game> listGames();
    public abstract Game addGame(Game game);
    public abstract Game getGame(Long gameId);
    public abstract void deleteGame(Long gameId, User user);
}
