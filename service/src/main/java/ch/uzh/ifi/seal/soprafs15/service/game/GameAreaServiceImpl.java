package ch.uzh.ifi.seal.soprafs15.service.game;

import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameDiceAreaResponseBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameLegBettingAreaResponseBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameRaceBettingAreaResponseBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameRaceTrackResponseBean;
import ch.uzh.ifi.seal.soprafs15.model.game.Game;
import ch.uzh.ifi.seal.soprafs15.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs15.service.exceptions.GameNotFoundException;
import ch.uzh.ifi.seal.soprafs15.service.mapper.GameMapperService;
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

    private final Logger logger = LoggerFactory.getLogger(GameAreaServiceImpl.class);

    protected GameRepository gameRepository;
    protected GameMapperService gameMapperService;


    @Autowired
    public GameAreaServiceImpl(GameRepository gameRepository, GameMapperService gameMapperService){
        this.gameRepository = gameRepository;
        this.gameMapperService = gameMapperService;
    }

    @Override
    public GameRaceTrackResponseBean getRaceTrack(Long gameId) {
        Game game = gameRepository.findOne(gameId);

        if(game == null) {
            throw new GameNotFoundException(gameId, GameAreaServiceImpl.class);
        }
        GameRaceTrackResponseBean bean = gameMapperService.toRaceTrackResponseBean(game.getRaceTrack());
        return bean;
    }

    @Override
    public GameLegBettingAreaResponseBean getLegBettingArea(Long gameId) {
        Game game = gameRepository.findOne(gameId);

        if(game == null) {
            throw new GameNotFoundException(gameId, GameAreaServiceImpl.class);
        }

        GameLegBettingAreaResponseBean bean = gameMapperService.toGameLegBettingAreaResponseBean(game.getLegBettingArea());
        return bean;
    }

    @Override
    public GameRaceBettingAreaResponseBean getRaceBettingArea(Long gameId) {
        Game game = gameRepository.findOne(gameId);

        if(game == null) {
            throw new GameNotFoundException(gameId, GameAreaServiceImpl.class);
        }

        GameRaceBettingAreaResponseBean bean = gameMapperService.toGameRaceBettingAreaResponseBean(game.getRaceBettingArea());
        return bean;
    }

    @Override
    public GameDiceAreaResponseBean getDiceArea(Long gameId) {
        Game game = gameRepository.findOne(gameId);

        if(game == null) {
            throw new GameNotFoundException(gameId, GameAreaServiceImpl.class);
        }

        GameDiceAreaResponseBean bean = gameMapperService.toGameDiceAreaResponseBean(game.getDiceArea());
        return bean;
    }
}
