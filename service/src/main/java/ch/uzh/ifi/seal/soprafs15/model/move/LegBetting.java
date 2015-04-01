package ch.uzh.ifi.seal.soprafs15.model.move;

import ch.uzh.ifi.seal.soprafs15.model.game.LegBettingTile;

import javax.persistence.Column;

/**
 * Created by Hakuna on 30.03.2015.
 */
public class LegBetting extends Move {

    @Column
    private LegBettingTile legBettingTile;

    public LegBettingTile getLegBettingTile() {
        return legBettingTile;
    }

    public void setLegBettingTile(LegBettingTile legBettingTile) {
        this.legBettingTile = legBettingTile;
    }
}