package ch.uzh.ifi.seal.soprafs15.controller.beans.game;

/**
 * @author Marco
 */
public class GameCreateResponseBean extends GameResponseBean {

    private String channelName;


    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }
}
