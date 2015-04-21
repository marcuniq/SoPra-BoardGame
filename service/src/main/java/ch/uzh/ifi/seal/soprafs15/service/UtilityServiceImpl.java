package ch.uzh.ifi.seal.soprafs15.service;

import ch.uzh.ifi.seal.soprafs15.model.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Marco
 *
 * Implementation of utility functions exposed to API
 */
@Transactional
@Service("utilityService")
public class UtilityServiceImpl extends UtilityService {

    private final Logger logger = LoggerFactory.getLogger(UtilityServiceImpl.class);

    protected List<CrudRepository> repos;

    @Autowired
    public UtilityServiceImpl(DiceAreaRepository diceAreaRepository,
                              GameRepository gameRepository,
                              LegBettingAreaRepository legBettingAreaRepository,
                              MoveRepository moveRepository,
                              RaceBettingAreaRepository raceBettingAreaRepository,
                              RaceTrackRepository raceTrackRepository,
                              UserRepository userRepository){
        repos = new ArrayList<>();

        repos.add(diceAreaRepository);
        repos.add(gameRepository);
        repos.add(legBettingAreaRepository);
        repos.add(moveRepository);
        repos.add(raceBettingAreaRepository);
        repos.add(raceTrackRepository);
        repos.add(userRepository);
    }


    /**
     * Delete all entities, for integration tests only
     * @return true
     */
    @Override
    public Boolean reset() {
        logger.debug("reset backend, delete all");

        repos.stream().forEach(r -> r.deleteAll());
        return true;
    }
}
