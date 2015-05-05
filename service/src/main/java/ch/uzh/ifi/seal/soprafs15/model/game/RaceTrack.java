package ch.uzh.ifi.seal.soprafs15.model.game;

import ch.uzh.ifi.seal.soprafs15.GameConstants;
import ch.uzh.ifi.seal.soprafs15.model.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Hakuna on 30.03.2015.
 */
@Entity
public class RaceTrack implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "raceTrack", cascade=CascadeType.ALL)
    @OrderBy("position ASC")
    private List<RaceTrackObject> fields = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)//(fetch = FetchType.EAGER)
    private GameState gameState;

    public RaceTrack(){
    }

    /**
     * Initialization of race track
     */
    public void initForGamePlay() {

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
        raceTrackObject.setRaceTrack(this);
        fields.add(raceTrackObject);
        sortByPosition();
    }

    private void sortByPosition(){
        fields = fields.stream().sorted((rto1, rto2) -> Integer.compare(rto1.getPosition(), rto2.getPosition()))
                .collect(Collectors.toList());
    }

    /**
     * Undo action for fast mode
     */
    public void removeRaceTrackObject(Integer position) {
        RaceTrackObject rto = getRaceTrackObject(position);
        rto.setRaceTrack(null);
        fields.remove(rto);
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

    private List<CamelStack> findCamelStacks(){
        List<RaceTrackObject> list = fields.stream().filter(rto -> rto.getClass() == CamelStack.class).collect(Collectors.toList());

        return (List<CamelStack>)(List<?>) list;
    }

    private List<DesertTile> findDesertTiles(){
        List<RaceTrackObject> list = fields.stream().filter(rto -> rto.getClass() == DesertTile.class).collect(Collectors.toList());

        return (List<DesertTile>)(List<?>) list;
    }

    public void moveCamelStack(Color color, Integer nrOfFieldsToAdvance){

        // locate Camel with color
        CamelStack camelStackWithWantedColor = findCamelStacks().stream().filter(cs -> cs.hasCamel(color)).findAny().get();

        CamelStackBooleanPair pair = camelStackWithWantedColor.splitOrGetCamelStack(color);
        CamelStack topSplitCamelStack = pair.getStack();
        Boolean splitOccurred = pair.getSplitOccurred();


        // moving a camel stack means temporarily removing it from the race track and put it back on a new position
        if(!splitOccurred)
            removeRaceTrackObject(camelStackWithWantedColor.getPosition());

        // find new position
        Integer newPosition = camelStackWithWantedColor.getPosition() + nrOfFieldsToAdvance;

        Boolean mergeOccurred =  new Boolean(false);
        while(getRaceTrackObject(newPosition) != null){
            // is at that position another camel stack or a desert tile?

            if(getRaceTrackObject(newPosition) instanceof CamelStack){

                ((CamelStack) getRaceTrackObject(newPosition)).push(topSplitCamelStack);
                mergeOccurred = Boolean.TRUE;
                break;

            } else if(getRaceTrackObject(newPosition) instanceof DesertTile){
                DesertTile desertTile = ((DesertTile)getRaceTrackObject(newPosition));

                User owner = desertTile.getOwner();
                owner.setMoney(owner.getMoney() + GameConstants.DESERT_TILE_PAYOUT);

                if(desertTile.getIsOasis())
                    newPosition += GameConstants.DESERT_TILE_MOVE_SPACE;
                else
                    newPosition -= GameConstants.DESERT_TILE_MOVE_SPACE;
            }
        }

        if(!mergeOccurred) {
            topSplitCamelStack.setPosition(newPosition);
            addRaceTrackObject(topSplitCamelStack);
        }

        sortByPosition();
    }

    public void undoMoveCamelStack(Color color, Integer faceValue) {

    }

    public void removeDesertTiles(){
        List<RaceTrackObject> desertTiles = fields.stream().filter(rto -> rto.getClass() == DesertTile.class).collect(Collectors.toList());
        fields.removeAll(desertTiles);
    }

    public void removePlayersDesertTile(Long userId){
        Optional<DesertTile> playersDesertTile = findDesertTiles().stream().filter(desertTile -> desertTile.getOwner().getId() == userId).findFirst();
        if(playersDesertTile.isPresent())
            fields.remove(playersDesertTile.get());
    }

    public Map<Color, Ranking> getRanking(){
        Map<Color, Ranking> rankingMap = new HashMap<>();

        sortByPosition();

        // leading camel stack
        CamelStack leadingCamelStack = (CamelStack) fields.stream().filter(rto -> rto.getClass() == CamelStack.class)
                .max((rto1, rto2) -> Integer.compare(rto1.position, rto2.position)).get();

        // top camel is first
        Camel firstCamel = leadingCamelStack.peek();
        rankingMap.put(firstCamel.getColor(), Ranking.FIRST);


        // second camel
        Camel secondCamel = null;
        if(leadingCamelStack.getStack().size() > 1){
            // second camel is on the same camel stack
            secondCamel = leadingCamelStack.getSecondCamel();
        } else {
            // find second camel stack


            CamelStack secondCamelStack = (CamelStack) fields.stream().filter(rto -> rto.getClass() == CamelStack.class)
                    .filter(cs -> cs.position != leadingCamelStack.position)
                    .max((cs1, cs2) -> Integer.compare(cs1.position, cs2.position)).get();

            secondCamel = secondCamelStack.peek();
        }
        rankingMap.put(secondCamel.getColor(), Ranking.SECOND);


        // last camel stack
        CamelStack lastCamelStack = (CamelStack) fields.stream().filter(rto -> rto.getClass() == CamelStack.class)
                .min((rto1, rto2) -> Integer.compare(rto1.position, rto2.position)).get();

        rankingMap.put(lastCamelStack.getGroundCamel().getColor(), Ranking.LAST);


        // for every other camel
        for(Color c : Color.values()){
            rankingMap.putIfAbsent(c, Ranking.SOMEWHERE_IN_THE_MIDDLE);
        }

        return rankingMap;
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
