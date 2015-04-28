package ch.uzh.ifi.seal.soprafs15.model.game;

import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameCamelResponseBean;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created by Hakuna on 30.03.2015.
 */

@Embeddable
public class Camel extends RaceTrackObject implements Serializable {

    private static final long serialVersionUID = 1L;

    private Color color;

    public Camel(){}

    public Camel(Color color){
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public GameCamelResponseBean toBean() {
        GameCamelResponseBean bean = new GameCamelResponseBean();
        //bean.setId(id);
        bean.setColor(color);

        return bean;
    }
}
