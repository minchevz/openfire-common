package gamesys.xmpp.component;

import org.xmpp.packet.IQ;

/**
 * Marker interface for Gamesys components that can have their lifecycles
 * managed by a {@link gamesys.xmpp.component.ComponentRegistrar}
 */
public interface Component extends org.xmpp.component.Component {

    String getSubDomain();

    String getVersion();

    IQ respondToIQGet();

}
