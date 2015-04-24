package ch.uzh.ifi.seal.soprafs15.model.move;

import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameMoveResponseBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.MoveEnum;
import ch.uzh.ifi.seal.soprafs15.model.game.Color;
import ch.uzh.ifi.seal.soprafs15.model.game.Game;
import ch.uzh.ifi.seal.soprafs15.model.game.RaceBettingArea;
import ch.uzh.ifi.seal.soprafs15.model.game.RaceBettingCard;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created by Hakuna on 30.03.2015.
 */
@Entity
public class RaceBetting extends Move {

    @Column
    private Boolean betOnWinner;

    @Column
    private Color color;


    /**
     * Mapping from Move to Bean
     */
    @Override
    public GameMoveResponseBean toGameMoveResponseBean() {
        GameMoveResponseBean bean = new GameMoveResponseBean();
        bean.setId(id);
        bean.setGameId(game.getId());
        bean.setUserId(user.getId());
        bean.setMove(MoveEnum.RACE_BETTING);
        bean.setRaceBettingOnWinner(betOnWinner);

        return bean;
    }

    /**
     * Game logic for race betting
     */
    @Override
    public Move execute() {
        RaceBettingArea raceBettingArea = game.getRaceBettingArea();

        RaceBettingCard raceBettingCard = user.getRaceBettingCard(color);

        if(betOnWinner)
            raceBettingArea.betOnWinner(raceBettingCard);
        else
            raceBettingArea.betOnLoser(raceBettingCard);

        return this;
    }

    public Boolean getBetOnWinner() {
        return betOnWinner;
    }

    public void setBetOnWinner(Boolean betOnWinner) {
        this.betOnWinner = betOnWinner;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}