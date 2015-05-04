package ch.uzh.ifi.seal.soprafs15.model.game;

import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameCamelStackResponseBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameRaceTrackObjectResponseBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameRaceTrackResponseBean;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.annotation.DirtiesContext;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)

public class CamelStackUT {

    //declare 2 camels that are in the stack from the beginning
    private Camel testCamel1 = new Camel(Color.BLUE);
    private Camel testCamel2 = new Camel(Color.GREEN);
    List<Camel> camelList = new ArrayList();
    private CamelStack testCamelStack = new CamelStack();


    @Before
    public void setUp() throws Exception {
        //add camels onto the stack
        camelList.add(testCamel1);
        camelList.add(testCamel2);
        testCamelStack.setStack(camelList);
        List<Integer> previousPositionsList = new ArrayList<>();
        //testCamelStack.setPreviousPositions(previousPositionsList);
        //testCamelStack.addPreviousPosition(0);
    }

    @Test
    public void testPush() throws Exception {

        //declare and initialize another camel
        Camel testCamel3 = new Camel(Color.ORANGE);

        //call method to be tested
        testCamelStack.push(testCamel3);

        //Assertions
        assertTrue(testCamelStack.getStack().contains(testCamel3));
        assertEquals(Color.ORANGE, testCamelStack.getStack().get(2).getColor());

    }

    @Test
    public void testPop() throws Exception {

        //call method to be tested
        Camel result = testCamelStack.pop();

        //Assertions
        assertEquals(1,testCamelStack.getStack().size());
        assertFalse(testCamelStack.getStack().contains(testCamel2));
        assertEquals(testCamel2.getColor(),result.getColor());
        assertEquals(testCamel2,result);

    }

    @Test
    public void testPeek() throws Exception {

        //call method to be tested
        Camel result = testCamelStack.peek();

        //Assertions
        assertEquals(testCamel2.getColor(),result.getColor());
        assertEquals(testCamel2,result);

    }

    @Test
    public void testHasCamel() throws Exception {

        //call method to be tested
        boolean result = testCamelStack.hasCamel(Color.BLUE);

        //Assertions
        assertTrue(result);

    }

    @Test
    public void testSplitOrGetCamelStack_noSplit() throws Exception {

        //call method to be tested with ground color, so that no split should occur
        CamelStackBooleanPair result = testCamelStack.splitOrGetCamelStack(Color.BLUE);

        //Assertions
        assertEquals(testCamelStack,result.getStack());
        assertEquals(false, result.getSplitOccurred());

    }

    @Test
    public void testSplitOrGetCamelStack_Split() throws Exception{

        //call method to be tested
        CamelStackBooleanPair result = testCamelStack.splitOrGetCamelStack(Color.GREEN);

        //Assertions
        assertEquals(Color.GREEN,result.getStack().getStack().get(0).getColor());
        assertTrue(result.getStack().getStack().contains(testCamel2));
        assertTrue(result.getSplitOccurred());

    }


/*    @Test
    public void testMerge() throws Exception {

        //set up a new stack to pass on as a argument
        Camel argumentCamel = new Camel(Color.WHITE);
        List<Camel> argumentList = new ArrayList<>();
        argumentList.add(argumentCamel);
        CamelStack argumentStack = new CamelStack(argumentList);

        //call method to be tested
        //testCamelStack.merge(argumentStack);

        //Assertions
        assertTrue(testCamelStack.getStack().contains(argumentCamel));

    }*/

    @Test
    public void testAddPreviousPosition() throws Exception {

        //trivial, omitted for time being
    }

    @Test
    public void testToBean() throws Exception {

        //trivial, omitted for time being

    }
}