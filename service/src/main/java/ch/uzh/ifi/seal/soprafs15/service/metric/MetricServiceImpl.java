package ch.uzh.ifi.seal.soprafs15.service.metric;

import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameStatus;
import ch.uzh.ifi.seal.soprafs15.model.game.Game;
import ch.uzh.ifi.seal.soprafs15.model.repositories.GameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Marco
 */
@Service("metricService")
public class MetricServiceImpl extends MetricService {

    private final Logger logger = LoggerFactory.getLogger(MetricServiceImpl.class);

    protected GameRepository gameRepository;

    @Autowired
    public MetricServiceImpl(GameRepository gameRepository){
        this.gameRepository = gameRepository;
    }

    @Override
    public List<Long> listTimeToStart() {
        List<Game> games = (List<Game>) gameRepository.findAll();

        List<Long> tts = games.stream() .filter(game -> (game.getStatus() == GameStatus.RUNNING) || (game.getStatus() == GameStatus.FINISHED))
                                        .map(game -> Duration.between(game.getCreationTime(), game.getStartTime()).getSeconds())
                                        .collect(Collectors.toList());

        return tts;
    }

    @Override
    public Double getAvgTimeToStart() {
        List<Long> tts = listTimeToStart();
        return tts.stream().mapToLong(aLong -> aLong).average().orElse(0);
    }
}
