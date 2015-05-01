package ch.uzh.ifi.seal.soprafs15.model;

/**
 * @author Marco
 */
public interface Stack<T> {
    public void push(T entity);
    public T pop();
    public T peek();
}
