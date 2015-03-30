package ch.uzh.ifi.seal.soprafs15.model.game;

import javax.persistence.Column;
import java.io.Serializable;

/**
 * Created by Hakuna on 30.03.2015.
 */
public abstract class Card implements Serializable{

    @Column
    private Camel camel;

    public Camel getCamel() {
        return camel;
    }

    public void setCamel(Camel camel) {
        this.camel = camel;
    }
}
