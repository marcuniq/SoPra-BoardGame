package ch.uzh.ifi.seal.soprafs15.service.pusher.events;

/**
 * @author Marco
 */
public abstract class AbstractPushEvent {

    private PushEventNameEnum pushEventNameEnum;

    public AbstractPushEvent(PushEventNameEnum pushEventNameEnum){
        this.pushEventNameEnum = pushEventNameEnum;
    }


    public PushEventNameEnum getPushEventNameEnum() {
        return pushEventNameEnum;
    }

    public void setPushEventNameEnum(PushEventNameEnum pushEventNameEnum) {
        this.pushEventNameEnum = pushEventNameEnum;
    }
}
