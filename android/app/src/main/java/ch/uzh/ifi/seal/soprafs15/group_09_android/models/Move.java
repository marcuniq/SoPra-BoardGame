package ch.uzh.ifi.seal.soprafs15.group_09_android.models;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import auto.parcel.AutoParcel;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.gson.AutoGson;
import ch.uzh.ifi.seal.soprafs15.group_09_android.utils.GameColors;
import ch.uzh.ifi.seal.soprafs15.group_09_android.utils.Moves;

@AutoParcel @AutoGson
public abstract class Move implements Parcelable {

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
    public abstract Boolean desertTileAsOasis();
    @Nullable
    public abstract Integer desertTilePosition();

    public static Move create( String token,
                               Moves move,
                               GameColors legBettingTileColor,
                               Boolean raceBettingOnWinner,
                               Boolean desertTileAsOasis,
                               Integer desertTilePosition ) {
        return new AutoParcel_Move(
                null,
                null,
                null,
                null,
                token,
                move,
                legBettingTileColor,
                raceBettingOnWinner,
                desertTileAsOasis,
                desertTilePosition );
    }
}

