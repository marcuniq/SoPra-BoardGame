package ch.uzh.ifi.seal.soprafs15.model.repositories;

import ch.uzh.ifi.seal.soprafs15.model.game.RaceTrack;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("raceTrackRepository")
public interface RaceTrackRepository extends CrudRepository<RaceTrack, Long> {
}
