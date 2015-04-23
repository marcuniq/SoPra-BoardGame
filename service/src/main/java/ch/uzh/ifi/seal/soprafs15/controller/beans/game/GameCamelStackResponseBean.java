package ch.uzh.ifi.seal.soprafs15.controller.beans.game;

import java.util.List;

/**
 * @author Marco
 */
public class GameCamelStackResponseBean extends GameRaceTrackObjectResponseBean {

    private List<GameCamelResponseBean> stack;

    public List<GameCamelResponseBean> getStack() {
        return stack;
    }

    public void setStack(List<GameCamelResponseBean> stack) {
        this.stack = stack;
    }
}
