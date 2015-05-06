package ch.uzh.ifi.seal.soprafs15.model.game;

import ch.uzh.ifi.seal.soprafs15.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Hakuna on 30.03.2015.
 */
@Entity
public class RaceBettingCard implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    @Column
    @Enumerated(EnumType.STRING)
    private Color color;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name ="USER_ID")
    @JsonIgnore
    private User user;


    public RaceBettingCard(){}

    public RaceBettingCard(User user, Color color){
        this.user = user;
        this.color = color;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
