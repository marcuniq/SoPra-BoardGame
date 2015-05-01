package ch.uzh.ifi.seal.soprafs15.model.game;

import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameCamelResponseBean;
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

    @Id
    @GeneratedValue
    private Long id;

    @ElementCollection
    @Column
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


    @Override
    public GameRaceTrackObjectResponseBean toBean() {
        GameCamelStackResponseBean bean = new GameCamelStackResponseBean();
        //bean.setPosition(id);
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


}
