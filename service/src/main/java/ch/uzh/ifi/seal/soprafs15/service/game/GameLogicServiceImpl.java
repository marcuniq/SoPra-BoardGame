package ch.uzh.ifi.seal.soprafs15.service.game;

import ch.uzh.ifi.seal.soprafs15.GameConstants;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameMoveRequestBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameStatus;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.MoveEnum;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserStatus;
import ch.uzh.ifi.seal.soprafs15.model.User;
import ch.uzh.ifi.seal.soprafs15.model.game.*;
import ch.uzh.ifi.seal.soprafs15.model.move.Move;
import ch.uzh.ifi.seal.soprafs15.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs15.model.repositories.MoveRepository;
import ch.uzh.ifi.seal.soprafs15.model.repositories.UserRepository;
import ch.uzh.ifi.seal.soprafs15.service.exceptions.InvalidMoveException;
import ch.uzh.ifi.seal.soprafs15.service.exceptions.NotInFastModeException;
import ch.uzh.ifi.seal.soprafs15.service.exceptions.NotYourTurnException;
import ch.uzh.ifi.seal.soprafs15.service.mapper.GameMapperService;
import ch.uzh.ifi.seal.soprafs15.service.pusher.PusherService;
import ch.uzh.ifi.seal.soprafs15.service.pusher.events.GameFinishedEvent;
import ch.uzh.ifi.seal.soprafs15.service.pusher.events.LegOverEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author Marco
 */
@Transactional
@Service("gameLogicService")
public class GameLogicServiceImpl extends GameLogicService {

    private Logger logger = LoggerFactory.getLogger(GameLogicServiceImpl.class);

    protected GameRepository gameRepository;
    protected UserRepository userRepository;
    protected MoveRepository moveRepository;
    protected GameMapperService gameMapperService;
    protected PusherService pusherService;

    private Random random = new Random(System.currentTimeMillis());

    @Autowired
    public GameLogicServiceImpl(GameRepository gameRepository, UserRepository userRepository,
                                MoveRepository moveRepository,
                                GameMapperService gameMapperService, PusherService pusherService){
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.moveRepository = moveRepository;
        this.gameMapperService = gameMapperService;
        this.pusherService = pusherService;
    }

    /**
     * Define sequence of players (should be a circular list) and distribute playerId accordingly
     * @param game
     */
    @Override
    public  Map<Long, Integer> createPlayerSequence(Game game) {
        Map<Long, Integer> userIdToPlayerIdMap = new HashMap<>();

        List<User> players = game.getPlayers();

        Collections.shuffle(players);

        for(int i = 0; i < players.size(); i++){
            User player = players.get(i);
            player.setPlayerId(i+1);

            userIdToPlayerIdMap.put(player.getId(), player.getPlayerId());
        }

        return userIdToPlayerIdMap;
    }

    @Override
    public Move processMove(Game game, User player, Move move) {
        // is it player's turn?
        Integer currentPlayerId = game.getCurrentPlayerId();
        User currentPlayer = game.getPlayers().stream().filter(p -> p.getPlayerId() == currentPlayerId).findFirst().get();

        if(!currentPlayer.getUsername().equals(player.getUsername())) {
            throw new NotYourTurnException(GameLogicServiceImpl.class);
        }

        // valid move? else throw exception
        if(!move.isValid()) {
            throw new InvalidMoveException("Invalid Move", GameLogicServiceImpl.class);
        }

        // execute move
        move.execute();

        // check if game is over
        Boolean gameOver = runGameOverLogic(game);

        // check if leg is over
        if(!gameOver)
            runLegOverLogic(game);


        // next player's turn
        game.nextPlayer();

        return move;
    }

    @Override
    public void startFastMode(Game game) {

        game.setIsInFastMode(true);

        // create fake users
        List<User> players = new ArrayList<>();
        for(int i = 1; i <= GameConstants.FAST_MODE_NUMBER_PLAYERS; i++){
            User fakeUser = new User();
            fakeUser.setUsername("FastModePlayer" + i + "-Game" + game.getId());
            fakeUser.setAge(42);
            fakeUser.setStatus(UserStatus.ONLINE);
            fakeUser.setToken(UUID.randomUUID().toString());

            fakeUser = userRepository.save(fakeUser);

            game.addPlayer(fakeUser);

            players.add(fakeUser);
        }

        // remove owner from player list
        User owner = game.getOwner();
        game.removePlayer(owner);

        // start game
        game.initForGamePlay();
        game.setStatus(GameStatus.RUNNING);

        // create player sequence
        createPlayerSequence(game);

        // loop and make random moves until game is finished
        for(int i = 0; i < GameConstants.FAST_MODE_NUMBER_LOOPS && !game.getStatus().equals(GameStatus.FINISHED); ++i) {
            triggerMoveInFastMode(game);
        }
    }

    @Override
    public Move triggerMoveInFastMode(Game game){

        if(!game.isInFastMode()){
            throw new NotInFastModeException(GameLogicServiceImpl.class);
        }

        // get current player
        Integer currentPlayerId = game.getCurrentPlayerId();
        User currentPlayer = game.getPlayers().stream().filter(p -> p.getPlayerId() == currentPlayerId).findFirst().get();

        // create random move
        Boolean startLoop = true;
        int loopIteration = 0;
        Move move = null;
        while(startLoop || !move.isValid()){
            startLoop = false;
            ++loopIteration;

            GameMoveRequestBean bean = new GameMoveRequestBean();
            bean.setToken(currentPlayer.getToken());

            MoveEnum randomMove = MoveEnum.randomMove();

            // make it a bit less random to avoid long waiting to find a valid move
            if(loopIteration > 2)
                randomMove = MoveEnum.DICE_ROLLING;

            bean.setMove(randomMove);

            if (randomMove == MoveEnum.DESERT_TILE_PLACING){

                bean.setDesertTileAsOasis(random.nextBoolean());

                Integer position = random.nextInt(16) + 1;
                bean.setDesertTilePosition(position);

            } else if(randomMove == MoveEnum.LEG_BETTING){

                bean.setLegBettingTileColor(Color.randomColor());

            } else if(randomMove == MoveEnum.RACE_BETTING){

                bean.setRaceBettingOnWinner(random.nextBoolean());
                bean.setRaceBettingColor(Color.randomColor());
            }

            move = gameMapperService.toMove(game, currentPlayer, bean);
        }

        // process move
        processMove(game, currentPlayer, move);

        // save
        move = (Move) moveRepository.save(move);
        game.addMove(move);

        return move;
    }

    @Override
    public void stopFastMode(Game game) {
        // TODO
    }


    private Boolean runLegOverLogic(Game game) {
        DiceArea diceArea = game.getDiceArea();
        Boolean legOver = diceArea.getDiceInPyramid().size() == 0;

        if(legOver){
            // put dice back
            diceArea.init();

            // give back desert tiles
            for(User p : game.getPlayers()){
                p.setHasDesertTile(true);
            }

            // remove desert tiles from race track
            game.getRaceTrack().removeDesertTiles();

            // payout time
            runPayoutLogic(game);

            // remove leg betting tiles from players
            for(User p : game.getPlayers()){
                p.removeAllLegBettingTiles();
            }

            // reinitialize leg betting area
            game.getLegBettingArea().init();

            // notify player
            pusherService.pushToSubscribers(new LegOverEvent(), game);
        }

        return legOver;
    }

    private Boolean runGameOverLogic(Game game) {

        // check if camel is over finishing line, then game is over
        for(int i = 17; i < 20; i++) {
            RaceTrackObject rto = game.getRaceTrack().getRaceTrackObject(i);
            if(rto != null && rto.getClass() == CamelStack.class) {
                game.setStatus(GameStatus.FINISHED);

                // payout time
                runPayoutLogic(game);

                // notify player
                pusherService.pushToSubscribers(new GameFinishedEvent(), game);

                return true;
            }
        }

        return false;
    }

    private void runPayoutLogic(Game game){

        Map<Color, Ranking> camelRankingMap = game.getRaceTrack().getRanking();

        // payout leg betting
        for(User p : game.getPlayers()){

            // payout for each leg betting tile
            for(LegBettingTile t : p.getLegBettingTiles()){

                Integer winningMoney = 0;
                switch (camelRankingMap.get(t.getColor())){
                    case FIRST: winningMoney = t.getLeadingPositionGain(); break;
                    case SECOND:winningMoney = t.getSecondPositionGain(); break;
                    default:    winningMoney = p.getMoney() <= 0 ? 0 : t.getOtherPositionLoss(); break;
                }

                p.setMoney(p.getMoney() + winningMoney);
            }
        }

        // payout race betting
        if(game.getStatus() == GameStatus.FINISHED){

            // get race betting stacks
            List<RaceBettingCard> winnerBetting = game.getRaceBettingArea().getWinnerBetting().getStack();
            List<RaceBettingCard> loserBetting = game.getRaceBettingArea().getLoserBetting().getStack();

            // determine winnerColor and loserColor
            Color winnerColor = camelRankingMap.entrySet()
                                    .stream()
                                    .filter(entry -> Objects.equals(entry.getValue(), Ranking.FIRST))
                                    .map(Map.Entry::getKey)
                                    .findFirst().get();

            Color loserColor = camelRankingMap.entrySet()
                                    .stream()
                                    .filter(entry -> Objects.equals(entry.getValue(), Ranking.LAST))
                                    .map(Map.Entry::getKey)
                                    .findFirst().get();

            // payout
            payoutRaceBettings(winnerBetting, winnerColor);
            payoutRaceBettings(loserBetting, loserColor);
        }
    }

    /**
     *
     * @param bettings is a stack of either winner betting or loser betting
     * @param color is either the winning color or the losing color
     */
    private void payoutRaceBettings(List<RaceBettingCard> bettings, Color color){
        Stack<Integer> moneyReward = new Stack<>();
        moneyReward.addAll(Arrays.asList(2,3,5,8));

        for(RaceBettingCard r : bettings){
            User player = r.getUser();

            if(r.getColor() == color){
                // player has bet on the right color

                Integer winningMoney = !moneyReward.isEmpty() ? moneyReward.pop() : 1;
                player.setMoney(player.getMoney() + winningMoney);

            } else {
                // player cant have negative money
                if(player.getMoney() > 0)
                    player.setMoney(player.getMoney() - 1);
            }
        }
    }
}
