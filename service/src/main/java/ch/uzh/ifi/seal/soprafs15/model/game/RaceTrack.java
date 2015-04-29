package ch.uzh.ifi.seal.soprafs15.model.game;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Hakuna on 30.03.2015.
 */
@Entity
public class RaceTrack implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @ElementCollection
    @Column(columnDefinition = "BLOB")
    //@Size(max=16)
    private List<RaceTrackObject> fields = new ArrayList<>(16);

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="GAME_ID")
    private Game game;

    public RaceTrack(){
    }

    /**
     * Initialization of race track
     */
    public void initForGamePlay() {
        // set 16 placeholders
        //for(int i = 0; i < 16; i++)
        //    fields.add(new RaceTrackFieldPlaceholder());

        // put camels on race trace
        // reuse dice area for random placing
        List<Die> diceInPyramid = game.getDiceArea().getDiceInPyramid();

        // group dice by face value
        Map<Integer, List<Die>> map = diceInPyramid.stream().collect(Collectors.groupingBy(v -> v.getFaceValue()));

        for(Integer position : map.keySet()){
            List<Camel> stack = new ArrayList<>();

            for (Die d: map.get(position)){
                stack.add(new Camel(d.getColor()));
            }

            if(!stack.isEmpty())
                addRaceTrackObject(new CamelStack(position, stack));
        }

        // put dice back
        game.getDiceArea().init();
    }

    /**
     *
     * @param raceTrackObject CamelStack or DesertTile
     */
    public void addRaceTrackObject(RaceTrackObject raceTrackObject) {
        fields.add(raceTrackObject);
        fields.stream().sorted((rto1, rto2) -> Integer.compare(rto1.position, rto2.position));
    }

    /**
     *
     * @param position between 1 - 16
     * @return RaceTrackObject at requested position
     */
    public RaceTrackObject getRaceTrackObject(Integer position) {
        return fields.stream().filter(rto -> rto.position == position).findFirst().get();
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<RaceTrackObject> getFields() {
        return fields;
    }

    public void setFields(List<RaceTrackObject> fields) {
        this.fields = fields;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
