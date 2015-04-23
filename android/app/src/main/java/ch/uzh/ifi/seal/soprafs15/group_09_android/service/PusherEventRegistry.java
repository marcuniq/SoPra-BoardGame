package ch.uzh.ifi.seal.soprafs15.group_09_android.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pusher.client.channel.SubscriptionEventListener;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;

import ch.uzh.ifi.seal.soprafs15.group_09_android.models.Game;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.events.LegOverEvent;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.events.MoveEvent;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.events.PlayerLeftEvent;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.events.PushEventNameEnum;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.gson.AutoValueAdapterFactory;

/**
 * @author Marco
 */
public class PusherEventRegistry {

    public static void register(Game game){

        // connect
        PusherService.getInstance().connect(new ConnectionEventListener() {
            @Override
            public void onConnectionStateChange(ConnectionStateChange change) {
                System.out.println("State changed to " + change.getCurrentState() +
                        " from " + change.getPreviousState());
            }

            @Override
            public void onError(String message, String code, Exception e) {
                System.out.println("There was a problem connecting!");
                System.out.println("message: " + message);
                System.out.println("code: " + code);
                //System.out.println("exception: " + e.toString());
            }
        }, ConnectionState.ALL);

        // subscribe to channel
        //PusherService.getInstance().subscribe("test_channel");
        PusherService.getInstance().subscribe(game);

        // bind to certain events
        PusherService.getInstance().bind(PushEventNameEnum.MOVE_EVENT.toString(), new SubscriptionEventListener() {
            @Override
            public void onEvent(String channel, String event, String data) {
                System.out.println("Received move event with data: " + data);
                Gson gson = new GsonBuilder().registerTypeAdapterFactory(new AutoValueAdapterFactory()).create();
                MoveEvent e = gson.fromJson(data, MoveEvent.class);

                // TODO get move to show player

                getAreasAndUpdateViews();
            }
        });

        PusherService.getInstance().bind(PushEventNameEnum.PLAYER_LEFT_EVENT.toString(), new SubscriptionEventListener() {
            @Override
            public void onEvent(String channel, String event, String data) {
                System.out.println("Received player left event with data: " + data);
                Gson gson = new GsonBuilder().registerTypeAdapterFactory(new AutoValueAdapterFactory()).create();
                PlayerLeftEvent e = gson.fromJson(data, PlayerLeftEvent.class);

                // TODO notify player that another player left

                getAreasAndUpdateViews();
            }
        });

        PusherService.getInstance().bind(PushEventNameEnum.LEG_OVER_EVENT.toString(), new SubscriptionEventListener() {
            @Override
            public void onEvent(String channel, String event, String data) {
                System.out.println("Received leg over event with data: " + data);
                Gson gson = new GsonBuilder().registerTypeAdapterFactory(new AutoValueAdapterFactory()).create();
                LegOverEvent e = gson.fromJson(data, LegOverEvent.class);

                // TODO get intermediate scores from server and show them

                getAreasAndUpdateViews();
            }
        });
    }

    private static void getAreasAndUpdateViews(){
        // TODO get racetrack from server and update view


        // TODO get dice area from server and update view


        // TODO get leg betting area from server and update view


        // TODO get race betting area from server and update view


    }

}
