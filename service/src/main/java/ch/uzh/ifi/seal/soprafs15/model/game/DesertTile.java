package ch.uzh.ifi.seal.soprafs15.model.game;

import ch.uzh.ifi.seal.soprafs15.model.User;

import javax.persistence.Column;

/**
 * Created by Hakuna on 30.03.2015.
 */
public class DesertTile extends RaceTrackObject {

    @Column
    private User owner;

    @Column
    private Boolean isOasis;

    public void useAsOasis() {

    }

    public void useAsMirage() {

    }

    public Boolean getIsOasis() {
        return isOasis;
    }

    public void setIsOasis(Boolean isOasis) {
        this.isOasis = isOasis;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
