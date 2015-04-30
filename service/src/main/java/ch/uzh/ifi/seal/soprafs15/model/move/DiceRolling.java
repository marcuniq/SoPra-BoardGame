package ch.uzh.ifi.seal.soprafs15.model.move;

import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameMoveResponseBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.MoveEnum;
import ch.uzh.ifi.seal.soprafs15.model.game.DiceArea;
import ch.uzh.ifi.seal.soprafs15.model.game.Die;

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
        return null;
    }

    /**
     * Game logic for dice rolling
     */
    @Override
    public Move execute() {
        DiceArea diceArea = game.getDiceArea();
        die = diceArea.rollDice();

        return this;
    }
}