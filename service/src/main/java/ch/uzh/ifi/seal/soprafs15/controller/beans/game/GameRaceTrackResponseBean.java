package ch.uzh.ifi.seal.soprafs15.controller.beans.game;

import ch.uzh.ifi.seal.soprafs15.model.game.RaceTrackObject;

import java.util.List;

/**
 * @author Marco
 */
public class GameRaceTrackResponseBean {

    private Long id;
    private Long gameId;
    private List<GameRaceTrackObjectResponseBean> fields;

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

    public List<GameRaceTrackObjectResponseBean> getFields() {
        return fields;
    }

    public void setFields(List<GameRaceTrackObjectResponseBean> fields) {
        this.fields = fields;
    }
}
