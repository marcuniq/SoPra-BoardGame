package ch.uzh.ifi.seal.soprafs15.model.game;

import ch.uzh.ifi.seal.soprafs15.model.User;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * Created by Hakuna on 30.03.2015.
 */
@Entity
public class RaceBettingCard extends Card implements Serializable {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    private User user;

    public RaceBettingCard(){}


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
