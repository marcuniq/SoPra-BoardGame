package ch.uzh.ifi.seal.soprafs15.service;

import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameMoveRequestBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameStatus;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.MoveEnum;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserStatus;
import ch.uzh.ifi.seal.soprafs15.model.User;
import ch.uzh.ifi.seal.soprafs15.model.game.Color;
import ch.uzh.ifi.seal.soprafs15.model.game.Game;
import ch.uzh.ifi.seal.soprafs15.model.move.Move;
import ch.uzh.ifi.seal.soprafs15.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs15.model.repositories.UserRepository;
import ch.uzh.ifi.seal.soprafs15.service.exceptions.InvalidMoveException;
import ch.uzh.ifi.seal.soprafs15.service.exceptions.NotYourTurnException;
import ch.uzh.ifi.seal.soprafs15.service.mapper.GameMapperService;
import ch.uzh.ifi.seal.soprafs15.service.pusher.PusherService;
import ch.uzh.ifi.seal.soprafs15.service.pusher.events.FastModeAlmostFinishedEvent;
import ch.uzh.ifi.seal.soprafs15.service.pusher.events.GameFinishedEvent;
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
    protected GameMapperService gameMapperService;
    protected PusherService pusherService;

    private Random random = new Random();

    @Autowired
    public GameLogicServiceImpl(GameRepository gameRepository, UserRepository userRepository,
                                GameMapperService gameMapperService, PusherService pusherService){
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
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
        User currentPlayer = game.getPlayers().get(game.getCurrentPlayer());

        if(!currentPlayer.getUsername().equals(player.getUsername())) {
            throw new NotYourTurnException(GameLogicServiceImpl.class);
        }

        // valid move? else throw exception
        if(false) {
            throw new InvalidMoveException("Invalid Move", GameLogicServiceImpl.class);
        }

        // execute move
        move.execute();

        // check if game is finished
        if(false) {
            game.setStatus(GameStatus.FINISHED);
            pusherService.pushToSubscribers(new GameFinishedEvent(), game);
        }

        return move;
    }

    @Override
    public void startFastMode(Game game) {

        // create fake users
        List<User> players = new ArrayList<>();
        for(int i = 1; i <= 5; i++){
            User fakeUser = new User();
            fakeUser.setUsername("FakeUser" + i + "-Game" + game.getId());
            fakeUser.setAge(42);
            fakeUser.setStatus(UserStatus.ONLINE);
            fakeUser.setToken(UUID.randomUUID().toString());

            fakeUser = userRepository.save(fakeUser);

            game.addPlayer(fakeUser);

            players.add(fakeUser);
        }

        // remove owner from player list
        User owner = userRepository.findByUsername(game.getOwner());
        game.removePlayer(owner);

        // start game
        game.initForGamePlay();
        game.setStatus(GameStatus.RUNNING);

        // loop and make random moves until game is finished
        while (!game.getStatus().equals(GameStatus.FINISHED)) {
            for (int i = 1; i <= 5; i++) {
                Integer playerId = i;

                // get player whose turn it is
                User fakeUser = players.stream().filter(p -> p.getPlayerId() == playerId).findFirst().get();

                // make random move
                Boolean startLoop = true;
                Move move = null;
                while(startLoop || !move.isValid()){
                    startLoop = false;

                    GameMoveRequestBean bean = new GameMoveRequestBean();
                    bean.setToken(fakeUser.getToken());

                    MoveEnum randomMove = MoveEnum.randomMove();
                    bean.setMove(randomMove);

                    if (randomMove == MoveEnum.DESERT_TILE_PLACING){

                        bean.setDesertTileAsOasis(random.nextBoolean());

                        Integer position = random.nextInt(16);
                        bean.setDesertTilePosition(position);

                    } else if(randomMove == MoveEnum.LEG_BETTING){

                        bean.setLegBettingTileColor(Color.randomColor());

                    } else if(randomMove == MoveEnum.RACE_BETTING){

                        bean.setRaceBettingOnWinner(random.nextBoolean());
                        bean.setRaceBettingColor(Color.randomColor());
                    }

                    move = gameMapperService.toMove(game, fakeUser, bean);
                }

                // execute move
                processMove(game, fakeUser, move);

                //move = (Move) moveRepository.save(move);
                game.addMove(move);


            }
        }
        // roll back a couple of moves


        // notify owner
        pusherService.pushToSubscribers(new FastModeAlmostFinishedEvent(), game);
        //
    }

    @Override
    public void stopFastMode(Game game) {

    }

    private void cleanupAfterFastMode(Game game){



    }

}
