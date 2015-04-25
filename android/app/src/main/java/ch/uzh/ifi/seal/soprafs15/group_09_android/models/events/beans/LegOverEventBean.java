package ch.uzh.ifi.seal.soprafs15.group_09_android.models.events.beans;

import android.os.Parcelable;

import auto.parcel.AutoParcel;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.events.PushEventNameEnum;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.gson.AutoGson;

/**
 * @author Marco
 */
@AutoGson @AutoParcel
public abstract class LegOverEventBean implements Parcelable {

    public abstract PushEventNameEnum pushEventNameEnum();


    public static LegOverEventBean create(PushEventNameEnum pushEventNameEnum){
        return new AutoParcel_LegOverEventBean(pushEventNameEnum);
    }
}
