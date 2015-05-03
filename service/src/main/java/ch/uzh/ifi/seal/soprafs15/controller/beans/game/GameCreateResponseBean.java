package ch.uzh.ifi.seal.soprafs15.controller.beans.game;

/**
 * @author Marco
 */
public class GameCreateResponseBean extends GameResponseBean {

    private String channelName;

    public GameCreateResponseBean(GameResponseBean bean){
        this.id = bean.id;
        this.name = bean.name;
        this.owner = bean.owner;
        this.status = bean.status;
        this.numberOfMoves = bean.numberOfMoves;
        this.numberOfPlayers = bean.numberOfPlayers;
        this.currentPlayerId = bean.currentPlayerId;
    }

    public GameCreateResponseBean(){

    }


    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }
}
