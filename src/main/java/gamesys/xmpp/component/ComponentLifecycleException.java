package gamesys.xmpp.component;

/**
 * Exception thrown to indicate a problem in the lifecycle of a component,
 * typically with regards to its registration or de-registration.
 */
public class ComponentLifecycleException extends RuntimeException {
    public ComponentLifecycleException(Throwable throwable) {
        super(throwable);
    }
}
