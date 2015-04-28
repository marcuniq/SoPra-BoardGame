package ch.uzh.ifi.seal.soprafs15.group_09_android.models;


import java.util.List;

import ch.uzh.ifi.seal.soprafs15.group_09_android.models.beans.RaceTrackBean;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.beans.RaceTrackObjectBean;

/**
 * @author Marco
 */
public class RaceTrack extends AbstractArea {

    private Long id;
    private Long gameId;
    private List<RaceTrackObjectBean> fields;

    public RaceTrack(RaceTrackBean bean){
        this.id = bean.id();
        this.gameId = bean.gameId();
        this.fields = bean.fields();
    }

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

    public List<RaceTrackObjectBean> getFields() {
        return fields;
    }

    public void setFields(List<RaceTrackObjectBean> fields) {
        this.fields = fields;
    }
}
