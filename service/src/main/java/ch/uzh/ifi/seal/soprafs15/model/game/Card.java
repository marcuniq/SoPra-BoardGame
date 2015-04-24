package ch.uzh.ifi.seal.soprafs15.model.game;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Hakuna on 30.03.2015.
 */
@Entity
public abstract class Card implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    protected Long id;

    @Column
    @Enumerated(EnumType.STRING)
    protected Color color;


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
}
