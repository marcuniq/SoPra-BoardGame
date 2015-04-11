package ch.uzh.ifi.seal.soprafs15.model.move;

import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameMoveResponseBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.MoveEnum;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created by Hakuna on 30.03.2015.
 */
@Entity
public class RaceBetting extends Move {

    @Column
    private Boolean betOnWinner;

    public Boolean getBetOnWinner() {
        return betOnWinner;
    }

    public void setBetOnWinner(Boolean betOnWinner) {
        this.betOnWinner = betOnWinner;
    }

    @Override
    public GameMoveResponseBean toGameMoveResponseBean() {
        GameMoveResponseBean bean = new GameMoveResponseBean();
        bean.setId(getId());
        bean.setGameId(getGame().getId());
        bean.setUserId(getUser().getId());
        bean.setMove(MoveEnum.RACE_BETTING);
        bean.setRaceBettingOnWinner(betOnWinner);

        return bean;
    }
}