package ch.uzh.ifi.seal.soprafs15.group_09_android.models.beans;

import android.os.Parcelable;

import auto.parcel.AutoParcel;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.gson.AutoGson;
import ch.uzh.ifi.seal.soprafs15.group_09_android.utils.GameColors;

/**
 * @author Marco
 */
@AutoParcel
@AutoGson
public abstract class CamelBean implements Parcelable {

    public abstract GameColors color();

    public static CamelBean create(GameColors color){
        return new AutoParcel_CamelBean(color);
    }
}
