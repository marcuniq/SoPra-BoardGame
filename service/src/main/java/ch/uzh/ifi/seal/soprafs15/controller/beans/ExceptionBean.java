package ch.uzh.ifi.seal.soprafs15.controller.beans;

/**
 * @author Marco
 */
public class ExceptionBean {

    private String message;
    private String status;
    private Class invoker;

    public ExceptionBean(String message, Class invoker, String status){
        this.message = message;
        this.invoker = invoker;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Class getInvoker() {
        return invoker;
    }

    public void setInvoker(Class invoker) {
        this.invoker = invoker;
    }
}