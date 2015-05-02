package ch.uzh.ifi.seal.soprafs15.group_09_android.models.beans;

import android.os.Parcelable;

import auto.parcel.AutoParcel;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.gson.AutoGson;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.enums.GameColors;

/**
 * @author Marco
 */
@AutoParcel @AutoGson
public abstract class DieBean implements Parcelable {

    public abstract GameColors color();
    public abstract Integer faceValue();

    public static DieBean create(GameColors color, Integer faceValue) {
        return new AutoParcel_DieBean(color, faceValue);
    }
}
