package ch.uzh.ifi.seal.soprafs15.service;

import ch.uzh.ifi.seal.soprafs15.model.DiceArea;
import ch.uzh.ifi.seal.soprafs15.model.LegBettingArea;
import ch.uzh.ifi.seal.soprafs15.model.RaceBettingArea;
import ch.uzh.ifi.seal.soprafs15.model.RaceTrack;
import ch.uzh.ifi.seal.soprafs15.model.repositories.GameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Marco
 */
@Component("gameAreaService")
public class GameAreaServiceImpl extends GameAreaService {

    Logger logger = LoggerFactory.getLogger(GameAreaServiceImpl.class);

    protected GameRepository gameRepository;

    @Autowired
    public GameAreaServiceImpl(GameRepository gameRepository){
        this.gameRepository = gameRepository;
    }

    @Override
    public RaceTrack getRaceTrack(Long gameId) {
        return null;
    }

    @Override
    public LegBettingArea getLegBettingArea(Long gameId) {
        return null;
    }

    @Override
    public RaceBettingArea getRaceBettingArea(Long gameId) {
        return null;
    }

    @Override
    public DiceArea getDiceArea(Long gameId) {
        return null;
    }
}
