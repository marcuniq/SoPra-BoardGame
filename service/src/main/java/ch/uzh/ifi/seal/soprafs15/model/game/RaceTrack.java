package ch.uzh.ifi.seal.soprafs15.model.game;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

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

    @ElementCollection
    @Column
    private List<RaceTrackObject> fields;


    public RaceTrack(){

    }

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

    public List<RaceTrackObject> getFields() {
        return fields;
    }

    public void setFields(List<RaceTrackObject> fields) {
        this.fields = fields;
    }
}
