package ch.uzh.ifi.seal.soprafs15.model.game;

import javax.persistence.Column;
import java.util.Stack;

/**
 * Created by Hakuna on 30.03.2015.
 */
public class CamelStack extends RaceTrackObject {

    @Column
    private Stack<Camel> stack;

    public Stack<Camel> getStack() {
        return stack;
    }

    public void setStack(Stack<Camel> stack) {
        this.stack = stack;
    }
}
