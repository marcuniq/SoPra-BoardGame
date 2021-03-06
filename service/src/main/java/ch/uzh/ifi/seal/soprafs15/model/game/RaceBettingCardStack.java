package ch.uzh.ifi.seal.soprafs15.model.game;

import ch.uzh.ifi.seal.soprafs15.model.Stack;
import ch.uzh.ifi.seal.soprafs15.model.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Marco
 */
@Entity
public class RaceBettingCardStack implements Serializable, Stack<RaceBettingCard> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private Boolean betOnWinner;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "RACEBETTINGCARDSTACK_ID")
    private List<RaceBettingCard> stack;


    public RaceBettingCardStack(){
        init();
    }
    public RaceBettingCardStack(Boolean betOnWinner){
        this.betOnWinner = betOnWinner;
        init();
    }

    private void init(){
        stack = new ArrayList<>();
    }


    @Override
    public void push(RaceBettingCard raceBettingCard) {
        if(stack == null)
            stack = new ArrayList<>();

        stack.add(raceBettingCard);
    }

    @Override
    public RaceBettingCard pop() {
        RaceBettingCard result = null;
        if(stack.size() > 0)
            result = stack.remove(stack.size()-1);

        return result;
    }

    @Override
    public RaceBettingCard peek() {
        RaceBettingCard result = null;
        if(stack.size() > 0)
            result = stack.get(stack.size() - 1);

        return result;
    }



    public Integer size(){
        return stack.size();
    }

    public void removePlayersBet(Long userId){
        List<RaceBettingCard> playersBet = stack.stream()
                .filter(raceBettingCard -> raceBettingCard.getUser().getId() == userId)
                .collect(Collectors.toList());

        //playersBet.stream().forEach(raceBettingCard -> raceBettingCard.setStack(null));

        stack.removeAll(playersBet);
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<RaceBettingCard> getStack() {
        return stack;
    }

    public void setStack(List<RaceBettingCard> stack) {
        this.stack = stack;
    }

    public Boolean getBetOnWinner() {
        return betOnWinner;
    }

    public void setBetOnWinner(Boolean betOnWinner) {
        this.betOnWinner = betOnWinner;
    }
}
