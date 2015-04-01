package ch.uzh.ifi.seal.soprafs15.model.repositories;

import ch.uzh.ifi.seal.soprafs15.model.game.Game;
import ch.uzh.ifi.seal.soprafs15.model.move.Move;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("moveRepository")
public interface MoveRepository extends CrudRepository<Move, Long> {
    List<Move> findByGame(Game game);
}
