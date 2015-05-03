package ch.uzh.ifi.seal.soprafs15.model.game;

/**
 * @author Marco
 */
public class CamelStackBooleanPair {
    private CamelStack stack;
    private Boolean splitOccurred;

    public CamelStackBooleanPair(CamelStack stack, Boolean splitOccurred){
        this.stack = stack;
        this.splitOccurred = splitOccurred;
    }

    public CamelStack getStack() {
        return stack;
    }

    public void setStack(CamelStack stack) {
        this.stack = stack;
    }

    public Boolean getSplitOccurred() {
        return splitOccurred;
    }

    public void setSplitOccurred(Boolean splitOccurred) {
        this.splitOccurred = splitOccurred;
    }
}
