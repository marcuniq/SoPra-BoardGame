package ch.uzh.ifi.seal.soprafs15.group_09_android.models.beans;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import auto.parcel.AutoParcel;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.LegBettingTile;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.gson.AutoGson;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.enums.GameColors;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.enums.Moves;

@AutoParcel @AutoGson
public abstract class MoveBean implements Parcelable {

    @Nullable
    public abstract Long id();
    @Nullable
    public abstract Long gameId();
    @Nullable
    public abstract Long userId();
    @Nullable
    public abstract Integer playerId();
    @Nullable
    public abstract String token();

    public abstract Moves move();

    @Nullable
    public abstract GameColors legBettingTileColor();
    @Nullable
    public abstract Boolean raceBettingOnWinner();
    @Nullable
    public abstract GameColors raceBettingColor();
    @Nullable
    public abstract Boolean desertTileAsOasis();
    @Nullable
    public abstract Integer desertTilePosition();

    @Nullable
    public abstract LegBettingTile legBettingTile();

    public static MoveBean create( String token,
                               Moves move,
                               GameColors legBettingTileColor,
                               Boolean raceBettingOnWinner,
                               GameColors raceBettingColor,
                               Boolean desertTileAsOasis,
                               Integer desertTilePosition ) {
        return new AutoParcel_MoveBean(
                null,
                null,
                null,
                null,
                token,
                move,
                legBettingTileColor,
                raceBettingOnWinner,
                raceBettingColor,
                desertTileAsOasis,
                desertTilePosition,
                null);
    }
}

