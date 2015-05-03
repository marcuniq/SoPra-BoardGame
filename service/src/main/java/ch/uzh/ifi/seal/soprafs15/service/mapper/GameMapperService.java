package ch.uzh.ifi.seal.soprafs15.service.mapper;

import ch.uzh.ifi.seal.soprafs15.controller.GenericService;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.*;
import ch.uzh.ifi.seal.soprafs15.model.User;
import ch.uzh.ifi.seal.soprafs15.model.game.*;
import ch.uzh.ifi.seal.soprafs15.model.move.Move;

import java.util.List;

/**
 * @author Marco
 */
public abstract class GameMapperService extends GenericService {

    public abstract Game toGame(GameRequestBean bean);
    public abstract GameResponseBean toGameResponseBean(Game game);
    public abstract GameCreateResponseBean toGameCreateResponseBean(Game game);
    public abstract List<GameResponseBean> toGameResponseBean(List<Game> games);

    public abstract User toUser(GamePlayerRequestBean bean);
    public abstract GamePlayerResponseBean toGamePlayerResponseBean(User player);
    public abstract List<GamePlayerResponseBean> toGamePlayerResponseBean(List<User> players);

    public abstract Move toMove(Game game, User player, GameMoveRequestBean bean);
    public abstract GameMoveResponseBean toGameMoveResponseBean(Move move);
    public abstract List<GameMoveResponseBean> toGameMoveResponseBean(List<Move> moves);
    public abstract GameAddPlayerResponseBean toGameAddPlayerResponseBean(User player, Game game);

    public abstract GameRaceTrackResponseBean toRaceTrackResponseBean(RaceTrack raceTrack);
    public abstract GameLegBettingAreaResponseBean toGameLegBettingAreaResponseBean(LegBettingArea legBettingArea);
    public abstract GameRaceBettingAreaResponseBean toGameRaceBettingAreaResponseBean(RaceBettingArea raceBettingArea);
    public abstract GameDiceAreaResponseBean toGameDiceAreaResponseBean(DiceArea diceArea);
}
