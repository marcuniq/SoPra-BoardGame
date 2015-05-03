package ch.uzh.ifi.seal.soprafs15.group_09_android.service;

import android.content.Context;

import ch.uzh.ifi.seal.soprafs15.group_09_android.models.enums.AreaName;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.DiceArea;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.LegBettingArea;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.RaceBettingArea;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.RaceTrack;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.beans.DiceAreaBean;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.beans.LegBettingAreaBean;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.beans.RaceBettingAreaBean;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.beans.RaceTrackBean;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * @author Marco
 *
 * Pattern: Singleton, Facade
 */
public class AreaService {

    private static AreaService instance;

    private Context context;

    private AreaService(Context context){
        this.context = context;
    }

    public static AreaService getInstance(Context context){
        if(instance == null)
            instance = new AreaService(context);

        return instance;
    }

    public void getAreasAndNotifySubscriber(Long gameId){
        getRaceTrackAndNotifySubscriber(gameId);
        getDiceAreaAndNotifySubscriber(gameId);
        getLegBettingAreaAndNotifySubscriber(gameId);
        getRaceBettingAreaAndNotifySubscriber(gameId);
    }

    public void getRaceTrackAndNotifySubscriber(Long gameId) {

        // get racetrack from server and notify subscriber
        RestService.getInstance(context).getRacetrack(gameId, new Callback<RaceTrackBean>() {
            @Override
            public void success(RaceTrackBean raceTrackBean, Response response) {
                RaceTrack raceTrack = new RaceTrack(raceTrackBean);

                AreaUpdateSubscriberService.getInstance()
                        .notifySubscriber(AreaName.RACE_TRACK, raceTrack);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                AreaUpdateSubscriberService.getInstance()
                        .notifySubscriberOnError(AreaName.RACE_TRACK, "retrofit failure");
            }
        });

    }

    public void getDiceAreaAndNotifySubscriber(Long gameId) {

        // get dice area from server and notify subscriber
        RestService.getInstance(context).getDiceArea(gameId, new Callback<DiceAreaBean>() {
            @Override
            public void success(DiceAreaBean diceAreaBean, Response response) {
                DiceArea diceArea = new DiceArea(diceAreaBean);

                AreaUpdateSubscriberService.getInstance()
                        .notifySubscriber(AreaName.DICE_AREA, diceArea);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                AreaUpdateSubscriberService.getInstance()
                        .notifySubscriberOnError(AreaName.DICE_AREA, "retrofit failure: " + retrofitError.getMessage());
            }
        });
    }

    public void getLegBettingAreaAndNotifySubscriber(Long gameId) {

        // get leg betting area from server and notify subscriber
        RestService.getInstance(context).getLegBettingArea(gameId, new Callback<LegBettingAreaBean>() {
            @Override
            public void success(LegBettingAreaBean legBettingAreaBean, Response response) {
                LegBettingArea legBettingArea = new LegBettingArea(legBettingAreaBean);

                AreaUpdateSubscriberService.getInstance()
                        .notifySubscriber(AreaName.LEG_BETTING_AREA, legBettingArea);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                AreaUpdateSubscriberService.getInstance()
                        .notifySubscriberOnError(AreaName.LEG_BETTING_AREA, "retrofit failure: " + retrofitError.getMessage());
            }
        });

    }
    public void getRaceBettingAreaAndNotifySubscriber(Long gameId){

        // get race betting area from server and notify subscriber
        RestService.getInstance(context).getRaceBettingArea(gameId, new Callback<RaceBettingAreaBean>() {
            @Override
            public void success(RaceBettingAreaBean raceBettingAreaBean, Response response) {
                RaceBettingArea raceBettingArea = new RaceBettingArea(raceBettingAreaBean);

                AreaUpdateSubscriberService.getInstance()
                        .notifySubscriber(AreaName.RACE_BETTING_AREA, raceBettingArea);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                AreaUpdateSubscriberService.getInstance()
                        .notifySubscriberOnError(AreaName.RACE_BETTING_AREA, "retrofit failure: " + retrofitError.getMessage());
            }
        });
    }

    public void addSubscriber(AreaName area, AreaUpdateSubscriber areaUpdateSubscriberSubscriber){
        AreaUpdateSubscriberService.getInstance().addSubscriber(area, areaUpdateSubscriberSubscriber);
    }

    public void removeSubscriber(AreaName area, AreaUpdateSubscriber areaUpdateSubscriberSubscriber){
        AreaUpdateSubscriberService.getInstance().removeSubscriber(area, areaUpdateSubscriberSubscriber);
    }

}
