package ch.uzh.ifi.seal.soprafs15.controller;

import ch.uzh.ifi.seal.soprafs15.controller.beans.game.*;
import ch.uzh.ifi.seal.soprafs15.model.User;
import ch.uzh.ifi.seal.soprafs15.model.game.Game;
import ch.uzh.ifi.seal.soprafs15.model.move.Move;
import ch.uzh.ifi.seal.soprafs15.service.*;
import ch.uzh.ifi.seal.soprafs15.service.mapper.GameMapperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * @author Marco
 */
@RestController
public class GameServiceController extends GenericService {

	Logger logger = LoggerFactory.getLogger(GameServiceController.class);

    @Autowired
    protected GameService gameService;

    @Autowired
    protected UserService userService;

    @Autowired
    protected GameMoveService gameMoveService;

    @Autowired
    protected GamePlayerService gamePlayerService;

    @Autowired
    protected GameActionService gameActionService;

    @Autowired
    protected GameMapperService gameMapperService;

	private final String CONTEXT = "/games";


	/*
	 *	Context: /games
     *  Description:
	 */
	@RequestMapping(method = RequestMethod.GET, value = CONTEXT)
	@ResponseStatus(HttpStatus.OK)
    @ResponseBody
	public List<GameResponseBean> listGames() {
		logger.debug("listGames");

        try {
            List<Game> games = gameService.listGames();
            List<GameResponseBean> result = gameMapperService.toGameResponseBean(games);

            return result;
        } catch (Exception e){
            return null;
        }
	}


    /*
     *	Context: /games
     *  Description:
     */
	@RequestMapping(method = RequestMethod.POST, value = CONTEXT)
	@ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
	public GameResponseBean addGame(@RequestBody @Valid GameRequestBean gameRequestBean) {
		logger.debug("addGame: " + gameRequestBean);

        try {
            Game game = gameMapperService.toGame(gameRequestBean);
            game = gameService.addGame(game);
            GameResponseBean result = gameMapperService.toGameResponseBean(game);

            return result;
        } catch (Exception e){
            return null;
        }
	}
	
	/*
	 *	Context: /games/{gameId}
     *  Description:
	 */
	@RequestMapping(method = RequestMethod.GET, value = CONTEXT + "/{gameId}")
	@ResponseStatus(HttpStatus.OK)
    @ResponseBody
	public GameResponseBean getGame(@PathVariable Long gameId) {
		logger.debug("getGame: " + gameId);

        try {
            Game game = gameService.getGame(gameId);
            GameResponseBean result = gameMapperService.toGameResponseBean(game);

            return result;
        } catch (Exception e){
            return null;
        }
	}


    /*
     *	Context: /games/{gameId}/start
     *  Description:
     */
	@RequestMapping(method = RequestMethod.POST, value = CONTEXT + "/{gameId}/start")
	@ResponseStatus(HttpStatus.OK)
    @ResponseBody
	public GameResponseBean startGame(@PathVariable Long gameId, @RequestBody @Valid GamePlayerRequestBean gamePlayerRequestBean) {
		logger.debug("startGame: " + gameId);

        try {
            User user = gameMapperService.toUser(gamePlayerRequestBean);
            Game game = gameActionService.startGame(gameId, user);
            GameResponseBean result = gameMapperService.toGameResponseBean(game);

            return result;
        } catch (Exception e){
            return null;
        }
	}


    /*
     *	Context: /games/{gameId}/stop
     *  Description:
     */
	@RequestMapping(method = RequestMethod.POST, value = CONTEXT + "/{gameId}/stop")
	@ResponseStatus(HttpStatus.OK)
    @ResponseBody
	public GameResponseBean stopGame(@PathVariable Long gameId, @RequestBody @Valid GamePlayerRequestBean gamePlayerRequestBean) {
		logger.debug("startGame: " + gameId);

        try {
            User user = gameMapperService.toUser(gamePlayerRequestBean);
            Game game = gameActionService.stopGame(gameId, user);
            GameResponseBean result = gameMapperService.toGameResponseBean(game);

            return result;
        } catch (Exception e){
            return null;
        }
	}


    /*
     *	Context: /games/{gameId}/start-fast-mode
     *  Description:
     */
    @RequestMapping(method = RequestMethod.POST, value = CONTEXT + "/{gameId}/start-fast-mode")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameResponseBean startFastMode(@PathVariable Long gameId, @RequestBody @Valid GamePlayerRequestBean gamePlayerRequestBean) {
        logger.debug("start fast mode: " + gameId);

        try {
            User user = gameMapperService.toUser(gamePlayerRequestBean);
            Game game = gameActionService.startFastMode(gameId, user);
            GameResponseBean result = gameMapperService.toGameResponseBean(game);

            return result;
        } catch (Exception e){
            return null;
        }
    }


	/*
	 *	Context: /games/{gameId}/moves
     *  Description:
	 */
	@RequestMapping(method = RequestMethod.GET, value = CONTEXT + "/{gameId}/moves")
	@ResponseStatus(HttpStatus.OK)
    @ResponseBody
	public List<GameMoveResponseBean> listMoves(@PathVariable Long gameId) {
		logger.debug("listMoves");

        try {
            List<Move> moves = gameMoveService.listMoves(gameId);
            List<GameMoveResponseBean> result = gameMapperService.toGameMoveResponseBean(moves);

            return result;
        } catch (Exception e){
            return null;
        }
	}


    /*
	 *	Context: /games/{gameId}/moves
     *  Description:
     */
	@RequestMapping(method = RequestMethod.POST, value = CONTEXT + "/{gameId}/moves")
	@ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
	public GameMoveResponseBean addMove(@PathVariable Long gameId, @RequestBody @Valid GameMoveRequestBean gameMoveRequestBean) {
		logger.debug("addMove: " + gameMoveRequestBean);

        try {
            Move move = gameMapperService.toMove(gameMoveRequestBean);
            move = gameMoveService.addMove(gameId, move);
            GameMoveResponseBean result = gameMapperService.toGameMoveResponseBean(move);

            return result;
        } catch (Exception e){
            return null;
        }
	}


    /*
     *	Context: /games/{gameId}/moves/{moveId}
     *  Description:
     */
	@RequestMapping(method = RequestMethod.GET, value = CONTEXT + "/{gameId}/moves/{moveId}")
	@ResponseStatus(HttpStatus.OK)
    @ResponseBody
	public GameMoveResponseBean getMove(@PathVariable Long gameId, @PathVariable Long moveId) {
		logger.debug("getMove: " + gameId);

        try {
            Move move = gameMoveService.getMove(gameId, moveId);
            GameMoveResponseBean result = gameMapperService.toGameMoveResponseBean(move);

            return result;
        } catch (Exception e){
            return null;
        }
	}


	/*
     *	Context: /games/{gameId}/players
     *  Description:
	 */
	@RequestMapping(method = RequestMethod.GET, value = CONTEXT + "/{gameId}/players")
	@ResponseStatus(HttpStatus.OK)
    @ResponseBody
	public List<GamePlayerResponseBean> listPlayers(@PathVariable Long gameId) {
		logger.debug("listPlayers");

        try {
            List<User> players = gamePlayerService.listPlayer(gameId);
            List<GamePlayerResponseBean> result = gameMapperService.toGamePlayerResponseBean(players);

            return result;
        } catch (Exception e){
            return null;
        }
	}


    /*
     *	Context: /games/{gameId}/players
     *  Description:
     */
	@RequestMapping(method = RequestMethod.POST, value = CONTEXT + "/{gameId}/players")
	@ResponseStatus(HttpStatus.OK)
    @ResponseBody
	public GamePlayerResponseBean addPlayer(@PathVariable Long gameId, @RequestBody @Valid GamePlayerRequestBean gamePlayerRequestBean) {
		logger.debug("addPlayer: " + gamePlayerRequestBean);

        try {
            User player = gameMapperService.toUser(gamePlayerRequestBean);
            player = gamePlayerService.addPlayer(gameId, player);
            GamePlayerResponseBean result = gameMapperService.toGamePlayerResponseBean(player);

            return result;
        } catch (Exception e) {
            return null;
        }
	}


    /*
     *	Context: /games/{gameId}/players/{playerId}
     *  Description:
     */
	@RequestMapping(method = RequestMethod.GET, value = CONTEXT + "/{gameId}/players/{playerId}")
	@ResponseStatus(HttpStatus.OK)
    @ResponseBody
	public GamePlayerResponseBean getPlayer(@PathVariable Long gameId, @PathVariable Integer playerId) {
		logger.debug("getPlayer: " + gameId);

        try {
            User player = gamePlayerService.getPlayer(gameId, playerId);
            GamePlayerResponseBean result = gameMapperService.toGamePlayerResponseBean(player);

            return result;
        } catch (Exception e){
            return null;
        }
	}
}
