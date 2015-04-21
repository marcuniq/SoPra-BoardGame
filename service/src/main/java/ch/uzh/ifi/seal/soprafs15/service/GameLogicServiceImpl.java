package ch.uzh.ifi.seal.soprafs15.service;

import ch.uzh.ifi.seal.soprafs15.model.User;
import ch.uzh.ifi.seal.soprafs15.model.game.Game;
import ch.uzh.ifi.seal.soprafs15.model.move.Move;
import ch.uzh.ifi.seal.soprafs15.service.exceptions.PlayerTurnException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Marco
 */
@Transactional
@Service("gameLogicService")
public class GameLogicServiceImpl extends GameLogicService {

    Logger logger = LoggerFactory.getLogger(GameLogicServiceImpl.class);


    @Override
    public Move processMove(Game game, User player, Move move) throws PlayerTurnException {
        // is it player's turn?
        User currentPlayer = game.getPlayers().get(game.getCurrentPlayer());
        //if(!currentPlayer.getUsername().equals(player.getUsername()))
          //  throw new PlayerTurnException(game, player);

        // valid move? else throw exception


        // execute move
        move.execute();

        return move;
    }

}
