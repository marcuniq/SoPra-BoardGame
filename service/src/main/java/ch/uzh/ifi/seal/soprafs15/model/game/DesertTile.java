package ch.uzh.ifi.seal.soprafs15.model.game;

import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameDesertTileResponseBean;
import ch.uzh.ifi.seal.soprafs15.model.User;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Hakuna on 30.03.2015.
 */
@Entity
public class DesertTile extends RaceTrackObject implements Serializable {

    private static final long serialVersionUID = 1L;

    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name="USER_ID")
    private User owner;

    private Boolean isOasis;


    public DesertTile(){}

    public DesertTile(User owner){
        this.owner = owner;
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

    @Override
    public GameDesertTileResponseBean toBean() {
        GameDesertTileResponseBean bean = new GameDesertTileResponseBean();
        bean.setPosition(position);
        bean.setPlayerId(owner.getPlayerId());
        bean.setIsOasis(isOasis);

        return bean;
    }
}
