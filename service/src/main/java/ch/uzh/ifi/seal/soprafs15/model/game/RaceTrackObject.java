package ch.uzh.ifi.seal.soprafs15.model.game;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by Hakuna on 30.03.2015.
 */
@Entity
public abstract class RaceTrackObject implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    protected Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}