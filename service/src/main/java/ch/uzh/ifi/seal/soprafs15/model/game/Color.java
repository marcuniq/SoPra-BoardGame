package ch.uzh.ifi.seal.soprafs15.model.game;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by Hakuna on 30.03.2015.
 */
public enum Color {
    WHITE, YELLOW, ORANGE, GREEN, BLUE;

    private static final List<Color> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random(System.currentTimeMillis());

    public static Color randomColor()  {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
}
