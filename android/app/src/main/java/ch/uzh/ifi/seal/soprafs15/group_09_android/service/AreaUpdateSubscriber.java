package ch.uzh.ifi.seal.soprafs15.group_09_android.service;

import ch.uzh.ifi.seal.soprafs15.group_09_android.models.AbstractArea;

/**
 * @author Marco
 */
public abstract class AreaUpdateSubscriber {
    public abstract void onUpdate(AbstractArea area);
    public abstract void onError(String errorMessage);
}
