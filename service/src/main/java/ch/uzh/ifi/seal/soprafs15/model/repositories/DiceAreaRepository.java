package ch.uzh.ifi.seal.soprafs15.model.repositories;

import ch.uzh.ifi.seal.soprafs15.model.game.DiceArea;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("diceAreaRepository")
public interface DiceAreaRepository extends CrudRepository<DiceArea, Long> {
}
