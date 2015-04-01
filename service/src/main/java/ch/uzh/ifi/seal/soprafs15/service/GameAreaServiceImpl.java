package ch.uzh.ifi.seal.soprafs15.service;

import ch.uzh.ifi.seal.soprafs15.model.game.*;
import ch.uzh.ifi.seal.soprafs15.model.repositories.GameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Marco
 */
@Service("gameAreaService")
public class GameAreaServiceImpl extends GameAreaService {

    Logger logger = LoggerFactory.getLogger(GameAreaServiceImpl.class);

    protected GameRepository gameRepository;

    @Autowired
    public GameAreaServiceImpl(GameRepository gameRepository){
        this.gameRepository = gameRepository;
    }

    @Override
    public RaceTrack getRaceTrack(Long gameId) {
        Game game = gameRepository.findOne(gameId);

        if(game != null) {
            return game.getRaceTrack();
        }
        return null;
    }

    @Override
    public LegBettingArea getLegBettingArea(Long gameId) {
        Game game = gameRepository.findOne(gameId);

        if(game != null) {
            return game.getLegBettingArea();
        }
        return null;
    }

    @Override
    public RaceBettingArea getRaceBettingArea(Long gameId) {
        Game game = gameRepository.findOne(gameId);

        if(game != null) {
            return game.getRaceBettingArea();
        }
        return null;
    }

    @Override
    public DiceArea getDiceArea(Long gameId) {
        Game game = gameRepository.findOne(gameId);

        if(game != null) {
            return game.getDiceArea();
        }
        return null;
    }
}
