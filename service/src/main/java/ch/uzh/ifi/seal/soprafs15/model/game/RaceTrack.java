package ch.uzh.ifi.seal.soprafs15.model.game;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Hakuna on 30.03.2015.
 */
@Entity
public class RaceTrack implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private ArrayList<RaceTrackObject> fields;

    private void init() {

    }

    public void placeRaceTrackObject(RaceTrackObject raceTrackObject, Integer position) {

    }

    public RaceTrackObject getRaceTrackObject(Integer position) {
        return null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ArrayList<RaceTrackObject> getFields() {
        return fields;
    }

    public void setFields(ArrayList<RaceTrackObject> fields) {
        this.fields = fields;
    }
}
