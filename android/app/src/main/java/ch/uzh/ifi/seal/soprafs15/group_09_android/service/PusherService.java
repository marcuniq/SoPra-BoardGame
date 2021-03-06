package ch.uzh.ifi.seal.soprafs15.group_09_android.service;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pusher.client.channel.SubscriptionEventListener;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;

import ch.uzh.ifi.seal.soprafs15.group_09_android.models.events.AbstractPusherEvent;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.events.GameFinishedEvent;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.events.GameStartEvent;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.events.LegOverEvent;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.events.MoveEvent;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.events.PlayerJoinedEvent;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.events.PlayerLeftEvent;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.events.PlayerTurnEvent;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.events.PushEventNameEnum;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.events.beans.GameFinishedEventBean;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.events.beans.GameStartEventBean;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.events.beans.LegOverEventBean;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.events.beans.MoveEventBean;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.events.beans.PlayerJoinedEventBean;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.events.beans.PlayerLeftEventBean;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.events.beans.PlayerTurnEventBean;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.gson.AutoValueAdapterFactory;

/**
 * @author Marco
 *
 * Pattern: Singleton, Facade
 *
 */
public class PusherService {

    private static PusherService instance;

    private Context context;

    private PusherService(Context context){
        this.context = context;
    }

    public static PusherService getInstance(Context context){
        if(instance == null)
            instance = new PusherService(context);

        return instance;
    }

    public void register(final Long gameId, final String channelName){

        // connect
        PusherAPIService.getInstance().connect(new ConnectionEventListener() {
            @Override
            public void onConnectionStateChange(ConnectionStateChange change) {
                Log.i("PusherService", "State changed to " + change.getCurrentState() + " from " + change.getPreviousState());
            }

            @Override
            public void onError(String message, String code, Exception e) {
                    Log.e("PusherService", "There was a problem connecting!" + "\nmessage: " + message + "\ncode:" + code);
            }
        }, ConnectionState.ALL);

        // subscribe to channel
        PusherAPIService.getInstance().subscribe(channelName);

        final Gson gson = new GsonBuilder().registerTypeAdapterFactory(new AutoValueAdapterFactory()).create();

        // bind to certain events
        PusherAPIService.getInstance().bind(PushEventNameEnum.MOVE_EVENT.toString(), new SubscriptionEventListener() {
            @Override
            public void onEvent(String channel, String event, String data) {
                Log.d("PusherService", "Received MOVE_EVENT with data: " + data);

                MoveEventBean bean = gson.fromJson(data, MoveEventBean.class);

                // notify subscriber
                PusherEventSubscriberService.getInstance()
                        .notifySubscriber(PushEventNameEnum.MOVE_EVENT, new MoveEvent(bean));
            }
        });

        PusherAPIService.getInstance().bind(PushEventNameEnum.PLAYER_LEFT_EVENT.toString(), new SubscriptionEventListener() {
            @Override
            public void onEvent(String channel, String event, String data) {
                Log.d("PusherService", "Received PLAYER_LEFT_EVENT with data: " + data);

                        PlayerLeftEventBean bean = gson.fromJson(data, PlayerLeftEventBean.class);

                // notify subscriber
                PusherEventSubscriberService.getInstance()
                        .notifySubscriber(PushEventNameEnum.PLAYER_LEFT_EVENT, new PlayerLeftEvent(bean));
            }
        });

        PusherAPIService.getInstance().bind(PushEventNameEnum.LEG_OVER_EVENT.toString(), new SubscriptionEventListener() {
            @Override
            public void onEvent(String channel, String event, String data) {
                Log.d("PusherService", "Received LEG_OVER_EVENT with data: " + data);

                LegOverEventBean bean = gson.fromJson(data, LegOverEventBean.class);

                // notify subscriber
                PusherEventSubscriberService.getInstance()
                        .notifySubscriber(PushEventNameEnum.LEG_OVER_EVENT, new LegOverEvent(bean));
            }
        });

        PusherAPIService.getInstance().bind(PushEventNameEnum.GAME_FINISHED_EVENT.toString(), new SubscriptionEventListener() {
            @Override
            public void onEvent(String channel, String event, String data) {
                Log.d("PusherService", "Received GAME_FINISHED_EVENT with data: " + data);

                GameFinishedEventBean bean = gson.fromJson(data, GameFinishedEventBean.class);

                // notify subscriber
                PusherEventSubscriberService.getInstance()
                        .notifySubscriber(PushEventNameEnum.GAME_FINISHED_EVENT, new GameFinishedEvent(bean));
            }
        });

        PusherAPIService.getInstance().bind(PushEventNameEnum.GAME_START_EVENT.toString(), new SubscriptionEventListener() {
            @Override
            public void onEvent(String channel, String event, String data) {
                Log.d("PusherService", "Received GAME_START_EVENT with data: " + data);

                GameStartEventBean bean = gson.fromJson(data, GameStartEventBean.class);

                // notify subscriber
                PusherEventSubscriberService.getInstance()
                        .notifySubscriber(PushEventNameEnum.GAME_START_EVENT, new GameStartEvent(bean));
            }
        });

        PusherAPIService.getInstance().bind(PushEventNameEnum.PLAYER_TURN_EVENT.toString(), new SubscriptionEventListener() {
            @Override
            public void onEvent(String channel, String event, String data) {
                Log.d("PusherService", "Received PLAYER_TURN_EVENT with data: " + data);

                PlayerTurnEventBean bean = gson.fromJson(data, PlayerTurnEventBean.class);

                // notify subscriber
                PusherEventSubscriberService.getInstance()
                        .notifySubscriber(PushEventNameEnum.PLAYER_TURN_EVENT, new PlayerTurnEvent(bean));
            }
        });

        PusherAPIService.getInstance().bind(PushEventNameEnum.PLAYER_JOINED_EVENT.toString(), new SubscriptionEventListener() {
            @Override
            public void onEvent(String channel, String event, String data) {
                Log.d("PusherService", "Received PLAYER_JOINED_EVENT with data: " + data);

                PlayerJoinedEventBean bean = gson.fromJson(data, PlayerJoinedEventBean.class);

                // notify subscriber
                PusherEventSubscriberService.getInstance()
                        .notifySubscriber(PushEventNameEnum.PLAYER_JOINED_EVENT, new PlayerJoinedEvent(bean));
            }
        });


        // retrieve areas after each update, subscribe to push events
        registerAreaServiceAsSubscriber(gameId);
    }

    private void registerAreaServiceAsSubscriber(final Long gameId){

        for(PushEventNameEnum e : PushEventNameEnum.values()){
            addSubscriber(e, new PusherEventSubscriber() {
                @Override
                public void onNewEvent(AbstractPusherEvent event) {
                    AreaService.getInstance(context).getAreasAndNotifySubscriber(gameId);
                }
            });
         }
    }

    public void unregister(Long gameId, String channelName){
        unsubscribeFromChannel(channelName);
        removeAllSubscriber();
    }

    public void disconnect(){
        Log.i("PusherService", "disconnected");
        PusherAPIService.getInstance().disconnect();
    }

    public void unsubscribeFromChannel(String channelName){
        Log.i("PusherService", "unsubscribed from channel " + channelName);
        PusherAPIService.getInstance().unsubscribe(channelName);
    }

    public void addSubscriber(PushEventNameEnum event, PusherEventSubscriber eventSubscriber){
        Log.i("PusherService", "subscribed to new event: " + event);
        PusherEventSubscriberService.getInstance().addSubscriber(event, eventSubscriber);
    }

    public void removeSubscriber(PushEventNameEnum event, PusherEventSubscriber eventSubscriber){
        Log.i("PusherService", "removed subscriber to event: " + eventSubscriber);
        PusherEventSubscriberService.getInstance().removeSubscriber(event, eventSubscriber);
    }

    public void removeAllSubscriber(){
        PusherEventSubscriberService.getInstance().removeAllSubscriber();
    }
}
