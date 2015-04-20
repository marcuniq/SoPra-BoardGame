package ch.uzh.ifi.seal.soprafs15.group_09_android.service;

import com.pusher.client.Pusher;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.SubscriptionEventListener;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;

import ch.uzh.ifi.seal.soprafs15.group_09_android.models.Game;

/**
 * @author Marco
 */
public class PusherApi {

    private static final String API_KEY = "0f12b4dc1fd1743e1c87";

    private Pusher pusher;
    private Channel channel;

    public PusherApi() {
        createPusher();
    }

    private void createPusher() {
        pusher = new Pusher(API_KEY);
    }

    public void reconnect(){
        pusher.connect();
    }

    public void connect(ConnectionEventListener connectionEventListener, ConnectionState connectionState){
        pusher.connect(connectionEventListener, connectionState);
    }

    public void disconnect(){
        pusher.disconnect();
    }

    public void bind(String eventName, SubscriptionEventListener subscriptionEventListener){
        channel.bind(eventName, subscriptionEventListener);
    }

    public Channel subscribe(Game game){
        return subscribe(game.channelName());
    }

    public Channel subscribe(String channelName){
        channel = pusher.subscribe(channelName);
        return channel;
    }

    public void unsubscribe(Game game){
        unsubscribe(game.channelName());
    }
    public void unsubscribe(String channelName){
        pusher.unsubscribe(channelName);
        channel = null;
    }

    public Pusher getPusher(){
        return pusher;
    }

    public Channel getChannel(){
        return channel;
    }
}
