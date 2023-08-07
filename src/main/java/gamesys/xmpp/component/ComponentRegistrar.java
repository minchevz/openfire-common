package gamesys.xmpp.component;

/**
 * Defines the contract for component registrars, used to register and de-register
 * components.
 */
public interface ComponentRegistrar {

    /**
     * Registers a component such that packets can be routed to it
     * @param component The component to register
     */
    void registerComponent(Component component);

    /**
     * De-registers a component so that it will no longer be available
     * to process packets routed to it.
     * @param component The component to de-register
     */
    void deRegisterComponent(Component component);

}
