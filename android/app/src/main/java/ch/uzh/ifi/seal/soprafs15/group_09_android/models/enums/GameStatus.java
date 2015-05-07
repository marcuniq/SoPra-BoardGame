package ch.uzh.ifi.seal.soprafs15.group_09_android.models.enums;

public enum GameStatus{
    PENDING,    // waiting in the lobby for players
    BUSY,       // game remaining in the lobby but no new players can join
    RUNNING,    // currently running
    FINISHED,    // game has finished
    OPEN
}
