package ch.uzh.ifi.seal.soprafs15.model.move;

import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameMoveResponseBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.MoveEnum;
import ch.uzh.ifi.seal.soprafs15.model.game.LegBettingTile;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created by Hakuna on 30.03.2015.
 */
@Entity
public class LegBetting extends Move {

    @Column
    private LegBettingTile legBettingTile;

    public LegBettingTile getLegBettingTile() {
        return legBettingTile;
    }

    public void setLegBettingTile(LegBettingTile legBettingTile) {
        this.legBettingTile = legBettingTile;
    }

    @Override
    public GameMoveResponseBean toGameMoveResponseBean() {
        GameMoveResponseBean bean = new GameMoveResponseBean();
        bean.setId(getId());
        bean.setGameId(getGame().getId());
        bean.setUserId(getUser().getId());
        bean.setMove(MoveEnum.LEG_BETTING);
        bean.setLegBettingTile(legBettingTile);

        return bean;
    }
}