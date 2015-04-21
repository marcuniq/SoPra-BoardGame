package ch.uzh.ifi.seal.soprafs15.group_09_android.service;

import com.pusher.client.Pusher;
import com.pusher.client.channel.Channel;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionStateChange;

import ch.uzh.ifi.seal.soprafs15.group_09_android.models.Game;

/**
 * @author Marco
 */
public class PusherService {

    private static PusherService instance;

    public PusherApi pusherApi;

    private PusherService(){
        pusherApi = new PusherApi();
    }

    public static PusherApi getInstance(){
        if(instance == null)
            instance = new PusherService();

        return instance.pusherApi;
    }
}
