package ch.uzh.ifi.seal.soprafs15.service;

import ch.uzh.ifi.seal.soprafs15.model.User;
import ch.uzh.ifi.seal.soprafs15.model.game.Game;
import ch.uzh.ifi.seal.soprafs15.model.move.Move;
import ch.uzh.ifi.seal.soprafs15.service.exceptions.NotYourTurnException;
import ch.uzh.ifi.seal.soprafs15.service.exceptions.PlayerTurnException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Marco
 */
@Transactional
@Service("gameLogicService")
public class GameLogicServiceImpl extends GameLogicService {

    Logger logger = LoggerFactory.getLogger(GameLogicServiceImpl.class);


    @Override
    public Move processMove(Game game, User player, Move move) {
        // is it player's turn?
        User currentPlayer = game.getPlayers().get(game.getCurrentPlayer());
        if(!currentPlayer.getUsername().equals(player.getUsername()))
            throw new NotYourTurnException(GameLogicServiceImpl.class);

        // valid move? else throw exception


        // execute move
        move.execute();

        return move;
    }
}
