package ch.uzh.ifi.seal.soprafs15.group_09_android.models.events;

/**
 * @author Marco
 */

public abstract class AbstractPusherEvent {

    protected PushEventNameEnum pushEventNameEnum;

    public PushEventNameEnum getPushEventNameEnum() {
        return pushEventNameEnum;
    }

    public void setPushEventNameEnum(PushEventNameEnum pushEventNameEnum) {
        this.pushEventNameEnum = pushEventNameEnum;
    }
}
