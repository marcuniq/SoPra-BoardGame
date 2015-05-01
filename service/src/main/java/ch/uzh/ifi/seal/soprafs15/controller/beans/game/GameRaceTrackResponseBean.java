package ch.uzh.ifi.seal.soprafs15.controller.beans.game;

import java.util.List;

/**
 * @author Marco
 */
public class GameRaceTrackResponseBean {

    private Long id;
    private List<GameRaceTrackObjectResponseBean> fields;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<GameRaceTrackObjectResponseBean> getFields() {
        return fields;
    }

    public void setFields(List<GameRaceTrackObjectResponseBean> fields) {
        this.fields = fields;
    }
}
