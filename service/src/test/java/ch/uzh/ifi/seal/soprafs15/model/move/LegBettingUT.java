package ch.uzh.ifi.seal.soprafs15.model.move;

import ch.uzh.ifi.seal.soprafs15.model.StateManager;
import ch.uzh.ifi.seal.soprafs15.model.User;
import ch.uzh.ifi.seal.soprafs15.model.game.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LegBettingUT {

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testExecute() throws Exception {

        //set up a test User
        User testUser = new User();
        testUser.setId((long)1);
        testUser.setAge(45);
        testUser.setUsername("Paula");

        //set up a test LegBettingTile
        LegBettingTile testTile = new LegBettingTile();
        testTile.setColor(Color.BLUE);
        testTile.setLeadingPositionGain(5);
        testTile.setSecondPositionGain(1);
        testTile.setOtherPositionLoss(1);
        testTile.setId((long) 1);

        //set up a test LegBettingArea
        LegBettingArea testArea = new LegBettingArea();
        testArea.setId((long)1);

        //set up a test StateManager
        StateManager testManager = new StateManager();

        GameState testState = new GameState(testManager);
        testState.setLegBettingArea(testArea);
        testManager.setGameState(testState);


        //set up a test Game
        Game testGame = new Game();
        testGame.setName("testGame");
        testGame.setId((long)1);
        testGame.setStateManager(testManager);

        //set up actual test object using previously created Tile, User and Game
        LegBetting testLegBetting = new LegBetting();
        testLegBetting.setLegBettingTile(testTile);
        testLegBetting.setUser(testUser);
        testLegBetting.setGame(testGame);

        //assert testUser's Tile list is empty
        assertEquals(0, testUser.getLegBettingTiles().size());

        //call method to be tested
        Move result = testLegBetting.execute();

        //Assertions
        assertEquals(1,testUser.getLegBettingTiles().size());
        assertTrue(testUser.getLegBettingTiles().contains(testLegBetting.getLegBettingTile()));
        assertEquals(Color.BLUE,testUser.getLegBettingTiles().get(0).getColor());
        assertEquals(5,(int) testUser.getLegBettingTiles().get(0).getLeadingPositionGain());
        assertEquals(1,(int) testUser.getLegBettingTiles().get(0).getSecondPositionGain());
        assertEquals(-1,(int) testUser.getLegBettingTiles().get(0).getOtherPositionLoss());

    }
}