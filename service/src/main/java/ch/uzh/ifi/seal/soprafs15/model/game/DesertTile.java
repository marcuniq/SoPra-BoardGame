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

    @Id
    @GeneratedValue
    private Long id;


    @OneToOne(cascade= CascadeType.ALL)
    @JoinColumn(name="USER_ID")
    private User owner;

    private Boolean isOasis;

    public DesertTile(){}

    public DesertTile(User owner){
        this.owner = owner;
    }

    public void useAsOasis() {
        isOasis = Boolean.TRUE;
    }

    public void useAsMirage() {
        isOasis = Boolean.FALSE;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
