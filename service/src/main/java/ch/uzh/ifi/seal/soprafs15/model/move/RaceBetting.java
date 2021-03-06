package ch.uzh.ifi.seal.soprafs15.model.move;

import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameMoveResponseBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.MoveEnum;
import ch.uzh.ifi.seal.soprafs15.model.game.Color;
import ch.uzh.ifi.seal.soprafs15.model.game.RaceBettingArea;
import ch.uzh.ifi.seal.soprafs15.model.game.RaceBettingCard;
import ch.uzh.ifi.seal.soprafs15.service.exceptions.MoveUndoException;

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
        GameMoveResponseBean bean = super.toGameMoveResponseBean();
        bean.setMove(MoveEnum.RACE_BETTING);
        bean.setRaceBettingOnWinner(betOnWinner);

        return bean;
    }

    @Override
    public Boolean isValid() {

        Boolean hasRaceBettingCardColor = user.hasRaceBettingCard(color);

        // add reason for being invalid move
        if(!hasRaceBettingCardColor)
            addInvalidReason("You already have placed the race betting card of color " + color.toString());

        return hasRaceBettingCardColor;
    }

    /**
     * Game logic for race betting
     */
    @Override
    public Move execute() {
        RaceBettingArea raceBettingArea = game.getRaceBettingArea();

        RaceBettingCard raceBettingCard = user.getRaceBettingCard(color);

        raceBettingArea.bet(raceBettingCard, betOnWinner);

        return this;
    }

    /**
     * Undo action for fast mode
     */
    @Override
    public void undo() {
        RaceBettingArea raceBettingArea = game.getRaceBettingArea();

        RaceBettingCard raceBettingCard = raceBettingArea.undoBet(betOnWinner);

        if(raceBettingCard.getUser().getId() != user.getId())
            throw new MoveUndoException("undoing race betting failed, not same user", RaceBetting.class);

        user.putRaceBettingCardBack(raceBettingCard);
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