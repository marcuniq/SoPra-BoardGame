package ch.uzh.ifi.seal.soprafs15.model.move;

import ch.uzh.ifi.seal.soprafs15.model.Move;

import javax.persistence.Column;

/**
 * Created by Hakuna on 30.03.2015.
 */
public class RaceBetting extends Move {

    @Column
    private Boolean betOnWinner;

    public Boolean getBetOnWinner() {
        return betOnWinner;
    }

    public void setBetOnWinner(Boolean betOnWinner) {
        this.betOnWinner = betOnWinner;
    }
}
