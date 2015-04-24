package ch.uzh.ifi.seal.soprafs15.controller.beans.game;

import ch.uzh.ifi.seal.soprafs15.model.game.Color;
import ch.uzh.ifi.seal.soprafs15.model.game.Die;
import ch.uzh.ifi.seal.soprafs15.model.game.LegBettingTile;

public class GameMoveResponseBean {
    private Long id;
    private Long gameId;
    private Long userId;
    private MoveEnum move;
    private LegBettingTile legBettingTile;
    private Boolean raceBettingOnWinner;
    private Boolean desertTileAsOasis;
    private Integer desertTilePosition;
    private Die die;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public MoveEnum getMove() {
        return move;
    }

    public void setMove(MoveEnum move) {
        this.move = move;
    }

    public LegBettingTile getLegBettingTile() {
        return legBettingTile;
    }

    public void setLegBettingTile(LegBettingTile legBettingTile) {
        this.legBettingTile = legBettingTile;
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

    public Die getDie() {
        return die;
    }

    public void setDie(Die die) {
        this.die = die;
    }


}