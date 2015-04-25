package ch.uzh.ifi.seal.soprafs15.group_09_android.models.beans;

import android.os.Parcelable;

import auto.parcel.AutoParcel;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.gson.AutoGson;

/**
 * @author Marco
 */
@AutoParcel @AutoGson
public abstract class DieBean implements Parcelable {

    public abstract Color color();
    public abstract Integer faceValue();

    public static DieBean create(Color color, Integer faceValue) {
        return new AutoParcel_DieBean(color, faceValue);
    }
}
