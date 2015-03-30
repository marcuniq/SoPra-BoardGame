package ch.uzh.ifi.seal.soprafs15.model.game;

import ch.uzh.ifi.seal.soprafs15.model.User;

import javax.persistence.Column;

/**
 * Created by Hakuna on 30.03.2015.
 */
public class RaceBettingCard extends Card {

    @Column
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
