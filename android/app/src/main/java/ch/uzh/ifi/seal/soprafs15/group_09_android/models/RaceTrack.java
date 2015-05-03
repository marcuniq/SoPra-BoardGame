package ch.uzh.ifi.seal.soprafs15.group_09_android.models;


import java.util.List;

import ch.uzh.ifi.seal.soprafs15.group_09_android.models.beans.RaceTrackBean;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.beans.RaceTrackObjectBean;

/**
 * @author Marco
 */
public class RaceTrack extends AbstractArea {

    private Long id;
    private List<RaceTrackObjectBean> fields;

    public RaceTrack(RaceTrackBean bean){
        this.id = bean.id();
        this.fields = bean.fields();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<RaceTrackObjectBean> getFields() {
        return fields;
    }

    public void setFields(List<RaceTrackObjectBean> fields) {
        this.fields = fields;
    }
}
