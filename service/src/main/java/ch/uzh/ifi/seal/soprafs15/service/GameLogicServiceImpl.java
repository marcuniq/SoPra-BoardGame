package ch.uzh.ifi.seal.soprafs15.service;

import ch.uzh.ifi.seal.soprafs15.model.User;
import ch.uzh.ifi.seal.soprafs15.model.game.Game;
import ch.uzh.ifi.seal.soprafs15.model.move.Move;
import ch.uzh.ifi.seal.soprafs15.service.exceptions.InvalidMoveException;
import ch.uzh.ifi.seal.soprafs15.service.exceptions.NotYourTurnException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Marco
 */
@Transactional
@Service("gameLogicService")
public class GameLogicServiceImpl extends GameLogicService {

    Logger logger = LoggerFactory.getLogger(GameLogicServiceImpl.class);

    @Override
    public List<User> playerSequence(Game game) {
        List<User> players = game.getPlayers();
        List<User> sequence = new ArrayList<User>();
        Integer index;

        for(int i = 0; i < players.size(); i++) {
            index = (int) Math.random() % players.size();
            sequence.add(players.get(index));
            players.remove(index);
        }
        return sequence;
    }

    @Override
    public Move processMove(Game game, User player, Move move) {
        // is it player's turn?
        User currentPlayer = game.getPlayers().get(game.getCurrentPlayer());

        if(!currentPlayer.getUsername().equals(player.getUsername())) {
            throw new NotYourTurnException(GameLogicServiceImpl.class);
        }

        // valid move? else throw exception
        if(false) {
            throw new InvalidMoveException("Invalid Move", GameLogicServiceImpl.class);
        }

        // execute move
        move.execute();

        return move;
    }

}
