package ch.uzh.ifi.seal.soprafs15.controller.beans.game;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum MoveEnum {
    DICE_ROLLING, LEG_BETTING, RACE_BETTING, DESERT_TILE_PLACING;

    private static final List<MoveEnum> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random(System.currentTimeMillis());

    public static MoveEnum randomMove()  {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
}
