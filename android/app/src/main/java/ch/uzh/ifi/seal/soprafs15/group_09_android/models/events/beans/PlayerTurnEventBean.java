package ch.uzh.ifi.seal.soprafs15.group_09_android.models.events.beans;

import android.os.Parcelable;

import auto.parcel.AutoParcel;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.events.PushEventNameEnum;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.gson.AutoGson;

/**
 * @author Marco
 */
@AutoGson
@AutoParcel

public abstract class PlayerTurnEventBean implements Parcelable{

    public abstract Integer playerId();
    public abstract PushEventNameEnum pushEventNameEnum();

}
