package ch.uzh.ifi.seal.soprafs15.controller.beans.game;


public class GameAddPlayerResponseBean extends GamePlayerResponseBean {

    private String channelName;

    public GameAddPlayerResponseBean(){    }

    public GameAddPlayerResponseBean(GamePlayerResponseBean bean){
        this.id = bean.getId();
        this.playerId = bean.getPlayerId();
        this.username = bean.getUsername();
        this.money = bean.getMoney();
        this.legBettingTiles = bean.getLegBettingTiles();
        this.numberOfMoves = bean.getNumberOfMoves();
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }
}
