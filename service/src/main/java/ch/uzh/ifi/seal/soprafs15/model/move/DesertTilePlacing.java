package ch.uzh.ifi.seal.soprafs15.model.move;

import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameMoveResponseBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.MoveEnum;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created by Hakuna on 30.03.2015.
 */
@Entity
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

    @Override
    public GameMoveResponseBean toGameMoveResponseBean() {
        GameMoveResponseBean bean = new GameMoveResponseBean();
        bean.setId(getId());
        bean.setGameId(getGame().getId());
        bean.setUserId(getUser().getId());
        bean.setMove(MoveEnum.DESERT_TILE_PLACING);
        bean.setDesertTileAsOasis(asOasis);
        bean.setDesertTilePosition(position);

        return bean;
    }
}
