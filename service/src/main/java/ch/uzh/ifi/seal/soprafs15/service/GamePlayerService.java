package ch.uzh.ifi.seal.soprafs15.service;

import ch.uzh.ifi.seal.soprafs15.controller.GenericService;
import ch.uzh.ifi.seal.soprafs15.model.User;

import java.util.List;

/**
 * @author Marco
 */
public abstract class GamePlayerService extends GenericService {

    public abstract List<User> listPlayer(Long gameId);
    public abstract User addPlayer(Long gameId, User user);
    public abstract User getPlayer(Long gameId, User user);
}