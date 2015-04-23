package ch.uzh.ifi.seal.soprafs15.service;

import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GamePlayerRequestBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameResponseBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameStatus;
import ch.uzh.ifi.seal.soprafs15.model.User;
import ch.uzh.ifi.seal.soprafs15.model.game.Game;
import ch.uzh.ifi.seal.soprafs15.model.repositories.GameRepository;
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
@Service("gameActionService")
public class GameActionServiceImpl extends GameActionService {

    private final Logger logger = LoggerFactory.getLogger(GameActionServiceImpl.class);

    protected GameRepository gameRepository;
    protected GameMapperService gameMapperService;

    @Autowired
    public GameActionServiceImpl(GameRepository gameRepository, GameMapperService gameMapperService){
        this.gameRepository = gameRepository;
        this.gameMapperService = gameMapperService;
    }

    @Override
    public GameResponseBean startGame(Long gameId, GamePlayerRequestBean bean) {
        Game game = gameRepository.findOne(gameId);
        User owner = gameMapperService.toUser(bean);

        // check player amount constraint


        if(owner != null && game != null && game.getOwner().equals(owner.getUsername())) {
            //TODO: Start game

            game.initForGamePlay();

            game.setStatus(GameStatus.RUNNING);

        }

        return gameMapperService.toGameResponseBean(game);
    }

    @Override
    public GameResponseBean stopGame(Long gameId, GamePlayerRequestBean bean) {
        Game game = gameRepository.findOne(gameId);
        User owner = gameMapperService.toUser(bean);

        if(owner != null && game != null && game.getOwner().equals(owner.getUsername())) {
            //TODO: Stop game

        }

        return gameMapperService.toGameResponseBean(game);
    }

    @Override
    public GameResponseBean startFastMode(Long gameId, GamePlayerRequestBean bean) {
        Game game = gameRepository.findOne(gameId);
        User owner = gameMapperService.toUser(bean);

        if(owner != null && game != null && game.getOwner().equals(owner.getUsername())) {
            //TODO: Start fast mode

        }

        return gameMapperService.toGameResponseBean(game);
    }
}
