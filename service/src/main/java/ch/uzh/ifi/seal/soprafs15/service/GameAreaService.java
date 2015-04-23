package ch.uzh.ifi.seal.soprafs15.service;

import ch.uzh.ifi.seal.soprafs15.controller.GenericService;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameDiceAreaResponseBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameLegBettingAreaResponseBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameRaceBettingAreaResponseBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameRaceTrackResponseBean;
import ch.uzh.ifi.seal.soprafs15.model.game.DiceArea;
import ch.uzh.ifi.seal.soprafs15.model.game.LegBettingArea;
import ch.uzh.ifi.seal.soprafs15.model.game.RaceBettingArea;
import ch.uzh.ifi.seal.soprafs15.model.game.RaceTrack;

/**
 * @author Marco
 */
public abstract class GameAreaService extends GenericService {

    public abstract GameRaceTrackResponseBean getRaceTrack(Long gameId);
    public abstract GameLegBettingAreaResponseBean getLegBettingArea(Long gameId);
    public abstract GameRaceBettingAreaResponseBean getRaceBettingArea(Long gameId);
    public abstract GameDiceAreaResponseBean getDiceArea(Long gameId);
}
