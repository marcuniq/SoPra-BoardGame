package ch.uzh.ifi.seal.soprafs15.model.move;

import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameMoveResponseBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.MoveEnum;
import ch.uzh.ifi.seal.soprafs15.model.game.LegBettingArea;
import ch.uzh.ifi.seal.soprafs15.model.game.LegBettingTile;
import ch.uzh.ifi.seal.soprafs15.service.GameLogicService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created by Hakuna on 30.03.2015.
 */
@Entity
public class LegBetting extends Move {

    @Column(columnDefinition = "BLOB")
    private LegBettingTile legBettingTile;

    public LegBettingTile getLegBettingTile() {
        return legBettingTile;
    }

    public void setLegBettingTile(LegBettingTile legBettingTile) {
        this.legBettingTile = legBettingTile;
    }

    /**
     * Mapping from Move to Bean
     */
    @Override
    public GameMoveResponseBean toGameMoveResponseBean() {
        GameMoveResponseBean bean = super.toGameMoveResponseBean();
        bean.setMove(MoveEnum.LEG_BETTING);
        bean.setLegBettingTile(legBettingTile);

        return bean;
    }

    @Override
    public Boolean isValid() {
        LegBettingArea legBettingArea = game.getLegBettingArea();

        return legBettingArea.peekLegBettingTile(legBettingTile.getColor()) != null;
    }

    /**
     * Game logic for leg betting
     */
    @Override
    public Move execute() {
        LegBettingArea legBettingArea = game.getLegBettingArea();
        legBettingTile = legBettingArea.popLegBettingTile(legBettingTile.getColor());

        // add tile to player
        user.addLegBettingTile(legBettingTile);

        return this;
    }

    /**
     * Undo action for fast mode
     */
    @Override
    public void undo() {
        LegBettingArea legBettingArea = game.getLegBettingArea();
        legBettingArea.pushLegBettingTile(legBettingTile);

        user.removeLegBettingTile(legBettingTile);
    }
}