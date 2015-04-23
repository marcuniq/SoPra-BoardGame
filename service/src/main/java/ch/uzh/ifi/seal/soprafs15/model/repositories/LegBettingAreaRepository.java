package ch.uzh.ifi.seal.soprafs15.model.repositories;

import ch.uzh.ifi.seal.soprafs15.model.game.LegBettingArea;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("legBettingAreaRepository")
public interface LegBettingAreaRepository extends CrudRepository<LegBettingArea, Long> {
}
