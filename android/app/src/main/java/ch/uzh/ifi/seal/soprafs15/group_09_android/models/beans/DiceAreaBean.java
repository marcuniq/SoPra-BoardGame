package ch.uzh.ifi.seal.soprafs15.group_09_android.models.beans;

import android.os.Parcelable;

import java.util.List;

import auto.parcel.AutoParcel;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.gson.AutoGson;

@AutoParcel @AutoGson
public abstract class DiceAreaBean implements Parcelable {
    public abstract Long id();
    public abstract List<DieBean> rolledDice();

    public static DiceAreaBean create(Long id, List<DieBean> rolledDice) {
        return new AutoParcel_DiceAreaBean(id, rolledDice);
    }
}

