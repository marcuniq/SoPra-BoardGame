package ch.uzh.ifi.seal.soprafs15.model.repositories;

import ch.uzh.ifi.seal.soprafs15.model.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("raceBettingAreaRepository")
public interface RaceBettingAreaRepository extends CrudRepository<RaceBettingArea, Long> {
}
