package ch.uzh.ifi.seal.soprafs15.model.game;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;
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
    private List<RaceTrackObject> fields = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)//(fetch = FetchType.EAGER)
    private GameState gameState;

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
        List<Die> diceInPyramid = gameState.getDiceArea().getDiceInPyramid();

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
        gameState.getDiceArea().init();
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
     * Undo action for fast mode
     */
    public void removeRaceTrackObject(Integer position) {
        fields.remove(getRaceTrackObject(position));
    }

    /**
     *
     * @param position between 1 - 16
     * @return RaceTrackObject at requested position
     */
    public RaceTrackObject getRaceTrackObject(Integer position) {
        Optional<RaceTrackObject> raceTrackObject = fields.stream().filter(rto -> rto.position == position).findFirst();
        return raceTrackObject.isPresent() ? raceTrackObject.get() : null;
    }

    public void moveCamelStack(Color color, Integer nrOfFields){

        // locate Camel with color
        CamelStack camelStack = (CamelStack) fields.stream().filter(rto -> rto.getClass() == CamelStack.class)
                                            .filter(cs -> ((CamelStack) cs).hasCamel(color)).findFirst().get();

        CamelStack newCamelStack = camelStack.splitOrGetCamelStack(color);

        // advance camel stack
        newCamelStack.setPosition(camelStack.getPosition() + nrOfFields);

        // persist newCamelStack ??

        if(camelStack != newCamelStack)
            fields.add(newCamelStack);


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

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }
}
