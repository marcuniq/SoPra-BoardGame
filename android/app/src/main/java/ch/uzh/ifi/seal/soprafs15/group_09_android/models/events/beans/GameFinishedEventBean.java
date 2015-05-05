package ch.uzh.ifi.seal.soprafs15.group_09_android.models.events.beans;

import android.os.Parcelable;

import auto.parcel.AutoParcel;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.events.PushEventNameEnum;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.gson.AutoGson;

/**
 * @author Josua
 */
@AutoGson @AutoParcel
public abstract class GameFinishedEventBean implements Parcelable {

    public abstract PushEventNameEnum pushEventNameEnum();


    public static GameFinishedEventBean create(PushEventNameEnum pushEventNameEnum){
        return new AutoParcel_GameFinishedEventBean(pushEventNameEnum);
    }
}
