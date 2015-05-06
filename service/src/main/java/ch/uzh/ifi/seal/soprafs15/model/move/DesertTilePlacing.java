package ch.uzh.ifi.seal.soprafs15.model.move;

import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameMoveResponseBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.MoveEnum;
import ch.uzh.ifi.seal.soprafs15.model.game.DesertTile;
import ch.uzh.ifi.seal.soprafs15.model.game.RaceTrack;
import ch.uzh.ifi.seal.soprafs15.service.GameLogicService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created by Hakuna on 30.03.2015.
 */
@Entity
public class DesertTilePlacing extends Move {

    @Column
    private Boolean isOasis;

    @Column
    private Integer position;

    public Boolean getIsOasis() {
        return isOasis;
    }

    public void setIsOasis(Boolean isOasis) {
        this.isOasis = isOasis;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    /**
     * Mapping from Move to Bean
     */
    @Override
    public GameMoveResponseBean toGameMoveResponseBean() {
        GameMoveResponseBean bean = super.toGameMoveResponseBean();
        bean.setMove(MoveEnum.DESERT_TILE_PLACING);
        bean.setDesertTileAsOasis(isOasis);
        bean.setDesertTilePosition(position);

        return bean;
    }

    @Override
    public Boolean isValid() {
        RaceTrack raceTrack = game.getRaceTrack();

        // constraints
        Boolean notPosition1 = position != 1;
        Boolean emptySpace = raceTrack.getRaceTrackObject(position) == null;
        Boolean notAfterAnotherDesertTile = raceTrack.getRaceTrackObject(position - 1) != null ?
                raceTrack.getRaceTrackObject(position - 1).getClass() != DesertTile.class : true;
        Boolean notBeforeAnotherDesertTile = raceTrack.getRaceTrackObject(position + 1) != null ?
                raceTrack.getRaceTrackObject(position + 1).getClass() != DesertTile.class : true;
        Boolean userHasDesertTile = user.hasDesertTile();

        return  notPosition1 && emptySpace && notAfterAnotherDesertTile && notBeforeAnotherDesertTile && userHasDesertTile;
    }

    /**
     * Game logic for dice rolling
     */
    @Override
    public Move execute() {
        DesertTile desertTile = user.removeDesertTile();
        desertTile.setIsOasis(isOasis);
        desertTile.setPosition(position);

        game.getRaceTrack().addRaceTrackObject(desertTile);

        return this;
    }

    /**
     * Undo action for fast mode
     */
    @Override
    public void undo() {
        game.getRaceTrack().removeRaceTrackObject(position);
    }
}
