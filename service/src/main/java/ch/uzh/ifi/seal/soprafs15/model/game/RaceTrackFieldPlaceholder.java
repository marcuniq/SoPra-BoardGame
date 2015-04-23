package ch.uzh.ifi.seal.soprafs15.model.game;

import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameRaceTrackFieldPlaceholderResponseBean;

/**
 * @author Marco
 */
public class RaceTrackFieldPlaceholder extends RaceTrackObject {

    @Override
    public GameRaceTrackFieldPlaceholderResponseBean toBean() {
        return new GameRaceTrackFieldPlaceholderResponseBean();
    }
}
