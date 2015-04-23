package ch.uzh.ifi.seal.soprafs15.service.mapper;

import ch.uzh.ifi.seal.soprafs15.controller.beans.game.*;
import ch.uzh.ifi.seal.soprafs15.model.User;
import ch.uzh.ifi.seal.soprafs15.model.game.*;
import ch.uzh.ifi.seal.soprafs15.model.move.*;
import ch.uzh.ifi.seal.soprafs15.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs15.model.repositories.MoveRepository;
import ch.uzh.ifi.seal.soprafs15.model.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Marco
 *
 * Responsible for mapping between Beans and Domain Models of the Game
 */

@Transactional
@Service("gameMapperService")
public class GameMapperServiceImpl extends GameMapperService {

    Logger logger = LoggerFactory.getLogger(GameMapperServiceImpl.class);

    protected UserRepository userRepository;
    protected GameRepository gameRepository;
    protected MoveRepository moveRepository;

    @Autowired
    public GameMapperServiceImpl(UserRepository userRepository, GameRepository gameRepository, MoveRepository moveRepository){
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
        this.moveRepository = moveRepository;
    }

    @Override
    public Game toGame(GameRequestBean bean) {
        Game game = new Game();
        game.setName(bean.getName());

        User owner = userRepository.findByToken(bean.getToken());
        game.setOwner(owner.getUsername());

        return game;
    }

    @Override
    public GameResponseBean toGameResponseBean(Game game) {
        GameResponseBean bean = new GameResponseBean();
        bean.setId(game.getId());
        bean.setName(game.getName());
        bean.setOwner(game.getOwner());
        bean.setNumberOfPlayers(game.getPlayers().size());
        bean.setStatus(game.getStatus());

        return bean;
    }

    @Override
    public GameCreateResponseBean toGameCreateResponseBean(Game game) {
        GameCreateResponseBean bean = new GameCreateResponseBean();
        bean.setId(game.getId());
        bean.setName(game.getName());
        bean.setOwner(game.getOwner());
        bean.setNumberOfPlayers(game.getPlayers().size());
        bean.setChannelName(game.getPusherChannelName());

        return bean;
    }

    @Override
    public List<GameResponseBean> toGameResponseBean(List<Game> games) {
        List<GameResponseBean> result = new ArrayList<>();

        for(Game game : games) {
            result.add(toGameResponseBean(game));
        }

        return result;
    }

    @Override
    public User toUser(GamePlayerRequestBean bean) {
        return userRepository.findByToken(bean.getToken());
    }

    @Override
    public GamePlayerResponseBean toGamePlayerResponseBean(User player) {
        GamePlayerResponseBean gamePlayerResponseBean = new GamePlayerResponseBean();
        gamePlayerResponseBean.setId(player.getId());
        gamePlayerResponseBean.setNumberOfMoves(player.getMoves().size());

        return gamePlayerResponseBean;
    }

    @Override
    public List<GamePlayerResponseBean> toGamePlayerResponseBean(List<User> players) {
        List<GamePlayerResponseBean> result = new ArrayList<>();

        for(User player : players) {
            result.add(toGamePlayerResponseBean(player));
        }

        return result;
    }

    @Override
    public Move toMove(Game game, User player, GameMoveRequestBean bean) {

        if(bean.getMove() == MoveEnum.DESERT_TILE_PLACING) {
            DesertTilePlacing desertTilePlacing = new DesertTilePlacing();
            desertTilePlacing.setUser(player);
            desertTilePlacing.setGame(game);
            desertTilePlacing.setAsOasis(bean.getDesertTileAsOasis());
            desertTilePlacing.setPosition(bean.getDesertTilePosition());


            return desertTilePlacing;
        } else if(bean.getMove() == MoveEnum.DICE_ROLLING) {
            DiceRolling diceRolling = new DiceRolling();
            diceRolling.setUser(player);
            diceRolling.setGame(game);

            return diceRolling;
        } else if(bean.getMove() == MoveEnum.LEG_BETTING) {
            LegBetting legBetting = new LegBetting();
            legBetting.setUser((player));
            legBetting.setGame(game);

            // create dummy leg betting tile to transport color
            // real leg betting tile will be fetched in move.execute()
            LegBettingTile legBettingTile = new LegBettingTile();
            legBettingTile.setColor(bean.getLegBettingTileColor());
            legBetting.setLegBettingTile(legBettingTile);

            return legBetting;
        } else if(bean.getMove() == MoveEnum.RACE_BETTING) {
            RaceBetting raceBetting = new RaceBetting();
            raceBetting.setUser(player);
            raceBetting.setGame(game);

            if(bean.getRaceBettingOnWinner()) {
                raceBetting.setBetOnWinner(true);
            } else {
                raceBetting.setBetOnWinner(false);
            }

            return raceBetting;
        }

        return null;
    }

    @Override
    public GameMoveResponseBean toGameMoveResponseBean(Move move) {
        // instead of
        // if(move instanceof DiceRolling) ...
        // else if(move instanceof LegBetting) ...

        return move.toGameMoveResponseBean();
    }

    @Override
    public List<GameMoveResponseBean> toGameMoveResponseBean(List<Move> moves) {
        List<GameMoveResponseBean> result = new ArrayList<>();

        for(Move m : moves)
            result.add(m.toGameMoveResponseBean());

        return result;
    }

    @Override
    public GameAddPlayerResponseBean toGameAddPlayerResponseBean(Game game) {
        GameAddPlayerResponseBean bean = new GameAddPlayerResponseBean();
        bean.setChannelName(game.getPusherChannelName());
        return bean;
    }

    @Override
    public GameRaceTrackResponseBean toRaceTrackResponseBean(RaceTrack raceTrack) {
        GameRaceTrackResponseBean bean = new GameRaceTrackResponseBean();
        bean.setId(raceTrack.getId());
        bean.setGameId(raceTrack.getGame().getId());

        List<GameRaceTrackObjectResponseBean> fields = new ArrayList<>();
        for(RaceTrackObject rto: raceTrack.getFields())
            fields.add(rto.toBean());

        bean.setFields(fields);

        return bean;
    }

    @Override
    public GameLegBettingAreaResponseBean toGameLegBettingAreaResponseBean(LegBettingArea legBettingArea) {
        GameLegBettingAreaResponseBean bean = new GameLegBettingAreaResponseBean();
        bean.setId(legBettingArea.getId());
        bean.setTopLegBettingTiles(legBettingArea.topLegBettingTiles());

        return bean;
    }

    @Override
    public GameRaceBettingAreaResponseBean toGameRaceBettingAreaResponseBean(RaceBettingArea raceBettingArea) {
        GameRaceBettingAreaResponseBean bean = new GameRaceBettingAreaResponseBean();
        bean.setId(raceBettingArea.getId());
        bean.setNrOfWinnerBetting(raceBettingArea.getNrOfWinnerBetting());
        bean.setNrOfLoserBetting(raceBettingArea.getNrOfLoserBetting());

        return bean;
    }

    @Override
    public GameDiceAreaResponseBean toGameDiceAreaResponseBean(DiceArea diceArea) {
        GameDiceAreaResponseBean bean = new GameDiceAreaResponseBean();
        bean.setId(diceArea.getId());
        bean.setRolledDice(diceArea.getRolledDice());

        return bean;
    }
}
