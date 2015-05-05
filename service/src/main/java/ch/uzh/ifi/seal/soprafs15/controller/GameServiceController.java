package ch.uzh.ifi.seal.soprafs15.controller;

import ch.uzh.ifi.seal.soprafs15.controller.beans.game.*;
import ch.uzh.ifi.seal.soprafs15.model.game.RaceBettingCard;
import ch.uzh.ifi.seal.soprafs15.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Marco
 *
 * Controller for handling all endpoints starting with /games
 */
@RestController
public class GameServiceController extends GenericService {

	private final Logger logger = LoggerFactory.getLogger(GameServiceController.class);

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
    protected GameAreaService gameAreaService;


	private final String CONTEXT = "/games";


	/*
	 *	Context: /games
     *  Description: Get a list of games
	 */
	@RequestMapping(method = RequestMethod.GET, value = CONTEXT)
	@ResponseStatus(HttpStatus.OK)
    @ResponseBody
	public List<GameResponseBean> listGames(@RequestParam(required = false) final String status) {
		logger.debug("listGames");

        List<GameResponseBean> result = gameService.listGames(status);
        return result;
	}


    /*
     *	Context: /games
     *  Description: Create a new game
     */
	@RequestMapping(method = RequestMethod.POST, value = CONTEXT)
	@ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
	public GameCreateResponseBean addGame(@RequestBody @Valid GameRequestBean gameRequestBean) {
		logger.debug("addGame: " + gameRequestBean);

        GameCreateResponseBean result = gameService.addGame(gameRequestBean);
        return result;
	}
	
	/*
	 *	Context: /games/{gameId}
     *  Description: Get game with gameId
	 */
	@RequestMapping(method = RequestMethod.GET, value = CONTEXT + "/{gameId}")
	@ResponseStatus(HttpStatus.OK)
    @ResponseBody
	public GameResponseBean getGame(@PathVariable Long gameId) {
		logger.debug("getGame: " + gameId);

        GameResponseBean result = gameService.getGame(gameId);
        return result;
	}


    /*
     *	Context: /games/{gameId}/start
     *  Description: Start the game with gameId, only owner is allowed to do that
     */
	@RequestMapping(method = RequestMethod.POST, value = CONTEXT + "/{gameId}/start")
	@ResponseStatus(HttpStatus.OK)
    @ResponseBody
	public GameResponseBean startGame(@PathVariable Long gameId, @RequestBody @Valid GamePlayerRequestBean gamePlayerRequestBean) {
		logger.debug("startGame: " + gameId);

        GameResponseBean result = gameActionService.startGame(gameId, gamePlayerRequestBean);
        return result;
	}


    /*
     *	Context: /games/{gameId}/stop
     *  Description: Stop the game with gameId, only owner is allowed to do that
     */
	@RequestMapping(method = RequestMethod.POST, value = CONTEXT + "/{gameId}/stop")
	@ResponseStatus(HttpStatus.OK)
    @ResponseBody
	public GameResponseBean stopGame(@PathVariable Long gameId, @RequestBody @Valid GamePlayerRequestBean gamePlayerRequestBean) {
		logger.debug("startGame: " + gameId);

        GameResponseBean result = gameActionService.stopGame(gameId, gamePlayerRequestBean);
        return result;
	}


    /*
     *	Context: /games/{gameId}/fast-mode/start
     *  Description: Start the game with gameId in fast-mode, only owner is allowed to do that
     */
    @RequestMapping(method = RequestMethod.POST, value = CONTEXT + "/{gameId}/fast-mode/start")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameResponseBean startFastMode(@PathVariable Long gameId, @RequestBody @Valid GamePlayerRequestBean gamePlayerRequestBean) {
        logger.debug("start fast mode: " + gameId);

        GameResponseBean result = gameActionService.startFastMode(gameId, gamePlayerRequestBean);
        return result;
    }

    /*
     *	Context: /games/{gameId}/fast-mode/next
     *  Description: Trigger next move in fast-mode, only owner is allowed to do that
     */
    @RequestMapping(method = RequestMethod.POST, value = CONTEXT + "/{gameId}/fast-mode/next")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameMoveResponseBean triggerMoveInFastMode(@PathVariable Long gameId, @RequestBody @Valid GamePlayerRequestBean gamePlayerRequestBean) {
        logger.debug("trigger move in fast mode: " + gameId);

        GameMoveResponseBean result = gameActionService.triggerMoveInFastMode(gameId, gamePlayerRequestBean);
        return result;
    }


	/*
	 *	Context: /games/{gameId}/moves
     *  Description: Get a list of all moves made for the game with gameId
	 */
	@RequestMapping(method = RequestMethod.GET, value = CONTEXT + "/{gameId}/moves")
	@ResponseStatus(HttpStatus.OK)
    @ResponseBody
	public List<GameMoveResponseBean> listMoves(@PathVariable Long gameId) {
		logger.debug("listMoves");

        List<GameMoveResponseBean> result = gameMoveService.listMoves(gameId);
        return result;
	}


    /*
	 *	Context: /games/{gameId}/moves
     *  Description: Make a move, done by a player whose turn it is
     */
	@RequestMapping(method = RequestMethod.POST, value = CONTEXT + "/{gameId}/moves")
	@ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
	public GameMoveResponseBean addMove(@PathVariable Long gameId, @RequestBody @Valid GameMoveRequestBean gameMoveRequestBean){
		logger.debug("addMove: " + gameMoveRequestBean);

        GameMoveResponseBean result = gameMoveService.addMove(gameId, gameMoveRequestBean);
        return result;
	}


    /*
     *	Context: /games/{gameId}/moves/{moveId}
     *  Description: Get the move with moveId
     */
	@RequestMapping(method = RequestMethod.GET, value = CONTEXT + "/{gameId}/moves/{moveId}")
	@ResponseStatus(HttpStatus.OK)
    @ResponseBody
	public GameMoveResponseBean getMove(@PathVariable Long gameId, @PathVariable Long moveId) {
		logger.debug("gameId: " + gameId + " , moveId" + moveId);

        GameMoveResponseBean result = gameMoveService.getMove(moveId);
        return result;
	}


	/*
     *	Context: /games/{gameId}/players
     *  Description: List of all players of the game with gameId
	 */
	@RequestMapping(method = RequestMethod.GET, value = CONTEXT + "/{gameId}/players")
	@ResponseStatus(HttpStatus.OK)
    @ResponseBody
	public List<GamePlayerResponseBean> listPlayers(@PathVariable Long gameId) {
		logger.debug("listPlayers");

        List<GamePlayerResponseBean> result = gamePlayerService.listPlayer(gameId);
        return result;
	}


    /*
     *	Context: /games/{gameId}/players
     *  Description: User joins game with gameId
     */
	@RequestMapping(method = RequestMethod.POST, value = CONTEXT + "/{gameId}/players")
	@ResponseStatus(HttpStatus.OK)
    @ResponseBody
	public GameAddPlayerResponseBean addPlayer(@PathVariable Long gameId, @RequestBody @Valid GamePlayerRequestBean gamePlayerRequestBean) {
		logger.debug("addPlayer: " + gamePlayerRequestBean);

        GameAddPlayerResponseBean result = gamePlayerService.addPlayer(gameId, gamePlayerRequestBean);
        return result;
	}


    /*
     *	Context: /games/{gameId}/players/{playerId}
     *  Description: Get player with playerId of game with gameId
     */
	@RequestMapping(method = RequestMethod.GET, value = CONTEXT + "/{gameId}/players/{playerId}")
	@ResponseStatus(HttpStatus.OK)
    @ResponseBody
	public GamePlayerResponseBean getPlayer(@PathVariable Long gameId, @PathVariable Integer playerId) {
		logger.debug("getPlayer: " + gameId);

        GamePlayerResponseBean result = gamePlayerService.getPlayer(gameId, playerId);
        return result;
	}


    /*
     *	Context: /games/{gameId}/players/{playerId}
     *  Description: Remove player with playerId of game with gameId
     */
    @RequestMapping(method = RequestMethod.DELETE, value = CONTEXT + "/{gameId}/players/{playerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void removePlayer(@PathVariable Long gameId, @PathVariable Integer playerId,
                             @RequestBody @Valid GamePlayerRequestBean gamePlayerRequestBean,
                             @RequestParam(required = false) final Boolean isUserId) {
        logger.debug("remove player " + playerId + " from game " + gameId);

        gamePlayerService.removePlayer(gameId, playerId, gamePlayerRequestBean, isUserId);
    }

    /*
     *	Context: /games/{gameId}/players/{playerId}/racebettingcards
     *  Description: Get player's race betting cards
     */
    @RequestMapping(method = RequestMethod.POST, value = CONTEXT + "/{gameId}/players/{playerId}/racebettingcards")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<RaceBettingCard> getRaceBettingCards(@PathVariable Long gameId, @PathVariable Integer playerId,
                                            @RequestBody @Valid GamePlayerRequestBean gamePlayerRequestBean) {
        logger.debug("getRaceBettingCards: " + playerId);

        List<RaceBettingCard> result = gamePlayerService.getRaceBettingCards(gameId, playerId, gamePlayerRequestBean);
        return result;
    }

    /*
     *	Context: /games/racetrack
     *  Description: Get race track
     */
    @RequestMapping(method = RequestMethod.GET, value = CONTEXT + "/{gameId}/racetrack")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameRaceTrackResponseBean getRaceTrack(@PathVariable Long gameId) {
        logger.debug("get racetrack: " + gameId);

        GameRaceTrackResponseBean result = gameAreaService.getRaceTrack(gameId);
        return result;
    }

    /*
    *	Context: /games/legbettingarea
    *  Description: Get leg betting area
    */
    @RequestMapping(method = RequestMethod.GET, value = CONTEXT + "/{gameId}/legbettingarea")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameLegBettingAreaResponseBean getLegBettingArea(@PathVariable Long gameId) {
        logger.debug("get leg betting area: " + gameId);

        GameLegBettingAreaResponseBean result = gameAreaService.getLegBettingArea(gameId);
        return result;
    }

    /*
    *	Context: /games/racebettingarea
    *  Description: Get race betting area
    */
    @RequestMapping(method = RequestMethod.GET, value = CONTEXT + "/{gameId}/racebettingarea")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameRaceBettingAreaResponseBean getRaceBettingArea(@PathVariable Long gameId) {
        logger.debug("get race betting area: " + gameId);

        GameRaceBettingAreaResponseBean result = gameAreaService.getRaceBettingArea(gameId);
        return result;
    }

    /*
    *	Context: /games/dicearea
    *  Description: Get dice area
    */
    @RequestMapping(method = RequestMethod.GET, value = CONTEXT + "/{gameId}/dicearea")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameDiceAreaResponseBean getDiceArea(@PathVariable Long gameId) {
        logger.debug("get dice area: " + gameId);

        GameDiceAreaResponseBean result = gameAreaService.getDiceArea(gameId);
        return result;
    }
}
