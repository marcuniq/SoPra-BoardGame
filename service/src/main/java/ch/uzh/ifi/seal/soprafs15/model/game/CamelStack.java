package ch.uzh.ifi.seal.soprafs15.model.game;

import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameCamelResponseBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameCamelStackResponseBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameRaceTrackObjectResponseBean;
import ch.uzh.ifi.seal.soprafs15.model.Stack;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Hakuna on 30.03.2015.
 */
@Entity
public class CamelStack extends RaceTrackObject implements Serializable, Stack<Camel>{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @ElementCollection
    @Column
    private List<Camel> stack;

    @Column
    private Integer previousPosition;

    public CamelStack(){
    }
    public CamelStack(List<Camel> camels){
        this.stack = camels;
    }
    public CamelStack(Integer position, List<Camel> camels) {
        this.position = position;
        this.stack = camels;
    }

    @Override
    public void push(Camel camel) {
        if(stack == null)
            stack = new ArrayList<>();

        stack.add(camel);
    }

    @Override
    public Camel pop() {
        Camel camel = null;
        if (stack.size() > 0)
            camel = stack.remove(stack.size() - 1);
        return camel;
    }

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

    public CamelStack splitOrGetCamelStack(Color color){

        if(!stack.isEmpty() && stack.get(0).getColor() == color)
            // Ground camel has that color
            return this;
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

            return new CamelStack(newStack);
        }
    }

    public Camel getGroundCamel(){
        return stack.isEmpty() ? null : stack.get(0);
    }

    public Camel getSecondCamel(){
        return stack.isEmpty() ? null : stack.size() < 2 ? null : stack.get(stack.size() - 2);
    }


    @Override
    public GameRaceTrackObjectResponseBean toBean() {
        GameCamelStackResponseBean bean = new GameCamelStackResponseBean();
        bean.setPosition(position);

        List<GameCamelResponseBean> beanStack = new ArrayList<>();
        for(Camel c : stack)
            beanStack.add(c.toBean());

        bean.setStack(beanStack);

        return bean;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Camel> getStack() {
        return stack;
    }

    public void setStack(List<Camel> stack) {
        this.stack = stack;
    }


    public Integer getPreviousPosition() {
        return previousPosition;
    }

    public void setPreviousPosition(Integer previousPosition) {
        this.previousPosition = previousPosition;
    }
}
