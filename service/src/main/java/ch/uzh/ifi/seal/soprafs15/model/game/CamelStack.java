package ch.uzh.ifi.seal.soprafs15.model.game;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Hakuna on 30.03.2015.
 */
public class CamelStack extends RaceTrackObject implements Serializable{

    private static final long serialVersionUID = 1L;

    @ElementCollection
    @Column
    private List<Camel> stack;

    public CamelStack(){

    }
    public CamelStack(List<Camel> camels) {
        this.stack = camels;
    }

    public List<Camel> getStack() {
        return stack;
    }

    public void setStack(List<Camel> stack) {
        this.stack = stack;
    }
}
