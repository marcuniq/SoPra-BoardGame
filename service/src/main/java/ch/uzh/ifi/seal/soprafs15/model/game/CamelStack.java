package ch.uzh.ifi.seal.soprafs15.model.game;

import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameCamelStackResponseBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameRaceTrackObjectResponseBean;
import ch.uzh.ifi.seal.soprafs15.model.Stack;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hakuna on 30.03.2015.
 */
@Entity
public class CamelStack extends RaceTrackObject implements Serializable, Stack<Camel>{

    private static final long serialVersionUID = 1L;


    @ElementCollection(fetch = FetchType.EAGER)
    @Column
    @Embedded
    private List<Camel> stack;


    public CamelStack(){
    }
    public CamelStack(List<Camel> camels){
        this.stack = camels;
    }
    public CamelStack(Integer position, List<Camel> camels) {
        this.position = position;
        this.stack = camels;
    }

    /**
     * Push camel onto stack
     * @param camel
     */
    @Override
    public void push(Camel camel) {
        if(stack == null)
            stack = new ArrayList<>();

        stack.add(camel);
    }

    /**
     * Pop top camel
     * @return top camel or null
     */
    @Override
    public Camel pop() {
        Camel camel = null;
        if (stack.size() > 0)
            camel = stack.remove(stack.size() - 1);
        return camel;
    }

    /**
     * Peek
     * @return top camel or null
     */
    @Override
    public Camel peek() {
        Camel camel = null;
        if (stack.size() > 0)
            camel = stack.get(stack.size() - 1);
        return camel;
    }


    public Boolean hasCamel(Color color){
        return stack.stream().anyMatch(camel -> camel.getColor() == color);
    }

    /**
     * Needed for moving camel stacks in RaceTrack
     *
     * A split occurs if the camel to be moved is not the ground camel,
     * so just the camel with the respective color and all above will be moved.
     *
     * @param color of camel to be moved
     * @return Pair of (new or old) camel stack and if split occurred
     */
    public CamelStackBooleanPair splitOrGetCamelStack(Color color){
        Boolean splitOccurred = !stack.isEmpty() && getGroundCamel().getColor() != color;
        if(!splitOccurred)
            // Ground camel has that color
            return new CamelStackBooleanPair(this, splitOccurred);
        else {
            // Camel with color is somewhere on stack

            List<Camel> newStack = new ArrayList<>();

            // add camel with color and all above to newStack
            for(int i = 0; i < stack.size(); i++){
                if(stack.get(i).getColor() == color){
                    for(int j = i; j < stack.size(); j++){
                        newStack.add(stack.get(j));
                    }
                    break;
                }
            }

            // remove camels from stack
            stack.removeAll(newStack);

            return  new CamelStackBooleanPair(new CamelStack(position, newStack), splitOccurred);
        }
    }

    /**
     * Needed for moving camel stacks in RaceTrack
     *
     * Camel stacks are merged, if a camel stack ends at a position, where another camel stack is
     *
     * @param other camel stack to be pushed onto this camel stack
     */
    public void push(CamelStack other){
        stack.addAll(other.getStack());
    }


    public Camel getGroundCamel(){
        return stack.isEmpty() ? null : stack.get(0);
    }

    public Camel getSecondCamel(){
        return stack.isEmpty() ? null : stack.size() < 2 ? null : stack.get(stack.size() - 2);
    }


    public List<Camel> getStack() {
        return stack;
    }

    public void setStack(List<Camel> stack) {
        this.stack = stack;
    }


    @Override
    public GameRaceTrackObjectResponseBean toBean() {
        GameCamelStackResponseBean bean = new GameCamelStackResponseBean();
        bean.setPosition(position);
        bean.setStack(stack);

        return bean;
    }
}
