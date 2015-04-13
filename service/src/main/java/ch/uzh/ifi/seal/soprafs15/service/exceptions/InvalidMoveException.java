package ch.uzh.ifi.seal.soprafs15.service.exceptions;

import ch.uzh.ifi.seal.soprafs15.model.move.Move;

/**
 * @author Marco
 */
public class InvalidMoveException extends Exception {
    public InvalidMoveException(Move move) {
    }
}
