package ch.uzh.ifi.seal.soprafs15.model.move;

import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameMoveResponseBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameStatus;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.MoveEnum;
import ch.uzh.ifi.seal.soprafs15.model.game.*;
import ch.uzh.ifi.seal.soprafs15.service.pusher.events.GameFinishedEvent;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created by Hakuna on 30.03.2015.
 */
@Entity
public class DiceRolling extends Move {

    @Column
    private Die die;

    public Die getDie() {
        return die;
    }

    public void setDie(Die die) {
        this.die = die;
    }

    /**
     * Mapping from Move to Bean
     */
    @Override
    public GameMoveResponseBean toGameMoveResponseBean() {
        GameMoveResponseBean bean = new GameMoveResponseBean();
        bean.setId(id);
        bean.setGameId(game.getId());
        bean.setUserId(user.getId());
        bean.setMove(MoveEnum.DICE_ROLLING);
        bean.setDie(die);

        return bean;
    }

    @Override
    public Boolean isValid() {
        return true;
    }

    /**
     * Game logic for dice rolling
     */
    @Override
    public Move execute() {
        DiceArea diceArea = game.getDiceArea();
        die = diceArea.rollDice();

        // move camel
        RaceTrack raceTrack = game.getRaceTrack();
        raceTrack.moveCamelStack(die.getColor(), die.getFaceValue());


        // check if camel is over finishing line
        for(int i = 16; i < 19; i++) {
            RaceTrackObject rto = game.getRaceTrack().getRaceTrackObject(i);
            if(rto != null && rto.getClass() == CamelStack.class) {
                game.setStatus(GameStatus.FINISHED);
            }
        }

        return this;
    }

    /**
     * Undo action for fast mode
     */
    @Override
    public void undo() {
        DiceArea diceArea = game.getDiceArea();
        diceArea.undoRollDice();
    }
}