package ch.uzh.ifi.seal.soprafs15.model.move;

import ch.uzh.ifi.seal.soprafs15.model.Move;
import com.sun.org.apache.xpath.internal.operations.Bool;

import javax.persistence.Column;

/**
 * Created by Hakuna on 30.03.2015.
 */
public class DesertTilePlacing extends Move {

    @Column
    private Boolean asOasis;

    @Column
    private Integer position;

    public Boolean getAsOasis() {
        return asOasis;
    }

    public void setAsOasis(Boolean asOasis) {
        this.asOasis = asOasis;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }
}
