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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Marco
 */
@Transactional
@Service("gameLogicService")
public class GameLogicServiceImpl extends GameLogicService {

    Logger logger = LoggerFactory.getLogger(GameLogicServiceImpl.class);


    /**
     * Define sequence of players (should be a circular list) and distribute playerId accordingly
     * @param game
     */
    @Override
    public  Map<Long, Integer> createPlayerSequence(Game game) {
        Map<Long, Integer> userIdToPlayerIdMap = new HashMap<>();

        List<User> players = game.getPlayers();

        Collections.shuffle(players);

        for(int i = 0; i < players.size(); i++){
            User player = players.get(i);
            player.setPlayerId(i+1);

            userIdToPlayerIdMap.put(player.getId(), player.getPlayerId());
        }

        return userIdToPlayerIdMap;
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
