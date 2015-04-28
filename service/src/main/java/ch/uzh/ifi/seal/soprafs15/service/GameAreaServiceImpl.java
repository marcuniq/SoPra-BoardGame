package ch.uzh.ifi.seal.soprafs15.service;

import ch.uzh.ifi.seal.soprafs15.model.game.*;
import ch.uzh.ifi.seal.soprafs15.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs15.service.exceptions.GameNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Marco
 */
@Transactional
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

        if(game == null) {
            throw new GameNotFoundException(gameId, GameAreaServiceImpl.class);
        }

        return game.getRaceTrack();
    }

    @Override
    public LegBettingArea getLegBettingArea(Long gameId) {
        Game game = gameRepository.findOne(gameId);

        if(game == null) {
            throw new GameNotFoundException(gameId, GameAreaServiceImpl.class);
        }

        return game.getLegBettingArea();
    }

    @Override
    public RaceBettingArea getRaceBettingArea(Long gameId) {
        Game game = gameRepository.findOne(gameId);

        if(game == null) {
            throw new GameNotFoundException(gameId, GameAreaServiceImpl.class);
        }

        return game.getRaceBettingArea();
    }

    @Override
    public DiceArea getDiceArea(Long gameId) {
        Game game = gameRepository.findOne(gameId);

        if(game == null) {
            throw new GameNotFoundException(gameId, GameAreaServiceImpl.class);
        }

        return game.getDiceArea();
    }
}
