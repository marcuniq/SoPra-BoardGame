package ch.uzh.ifi.seal.soprafs15.controller.beans.game;

import ch.uzh.ifi.seal.soprafs15.model.game.Color;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

public class GameMoveRequestBean {

    @NotEmpty
    private String token;

    @NotNull
    private MoveEnum move;

    private Color legBettingTileColor;
    private Boolean raceBettingOnWinner;
    private Color raceBettingColor;
    private Boolean desertTileAsOasis;
    private Integer desertTilePosition;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public MoveEnum getMove() {
        return move;
    }

    public void setMove(MoveEnum move) {
        this.move = move;
    }

    public Color getLegBettingTileColor() {
        return legBettingTileColor;
    }

    public void setLegBettingTileColor(Color legBettingTileColor) {
        this.legBettingTileColor = legBettingTileColor;
    }

    public Boolean getRaceBettingOnWinner() {
        return raceBettingOnWinner;
    }

    public void setRaceBettingOnWinner(Boolean raceBettingOnWinner) {
        this.raceBettingOnWinner = raceBettingOnWinner;
    }

    public Boolean getDesertTileAsOasis() {
        return desertTileAsOasis;
    }

    public void setDesertTileAsOasis(Boolean desertTileAsOasis) {
        this.desertTileAsOasis = desertTileAsOasis;
    }

    public Integer getDesertTilePosition() {
        return desertTilePosition;
    }

    public void setDesertTilePosition(Integer desertTilePosition) {
        this.desertTilePosition = desertTilePosition;
    }

    public Color getRaceBettingColor() {
        return raceBettingColor;
    }

    public void setRaceBettingColor(Color raceBettingColor) {
        this.raceBettingColor = raceBettingColor;
    }
}