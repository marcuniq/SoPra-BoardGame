package ch.uzh.ifi.seal.soprafs15.model.repositories;

import ch.uzh.ifi.seal.soprafs15.model.game.GameState;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository("gameStateRepository")
public interface GameStateRepository extends CrudRepository<GameState, Long> {
}