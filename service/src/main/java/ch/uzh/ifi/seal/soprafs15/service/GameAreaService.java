package ch.uzh.ifi.seal.soprafs15.service;

import ch.uzh.ifi.seal.soprafs15.controller.GenericService;
import ch.uzh.ifi.seal.soprafs15.model.game.DiceArea;
import ch.uzh.ifi.seal.soprafs15.model.game.LegBettingArea;
import ch.uzh.ifi.seal.soprafs15.model.game.RaceBettingArea;
import ch.uzh.ifi.seal.soprafs15.model.game.RaceTrack;

/**
 * @author Marco
 */
public abstract class GameAreaService extends GenericService {

    public abstract RaceTrack getRaceTrack(Long gameId);
    public abstract LegBettingArea getLegBettingArea(Long gameId);
    public abstract RaceBettingArea getRaceBettingArea(Long gameId);
    public abstract DiceArea getDiceArea(Long gameId);
}
