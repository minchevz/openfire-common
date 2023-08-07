package gamesys.xmpp.component;

import com.google.common.collect.ObjectArrays;
import gamesys.xmpp.packet.VersionResponseIQ;
import org.dom4j.Element;
import org.xmpp.packet.IQ;

import static gamesys.xmpp.packet.VersionResponseIQ.newVersionResponseIQ;

/**
 * Provides a default {@link gamesys.xmpp.component.Component} implementation, enhancing {@link org.xmpp.component.AbstractComponent} with
 * a number of Gamesys specific convenient additions, for example the ability to retrieve artifact version information
 * through XMPP calls.
 */
public abstract class AbstractComponent extends org.xmpp.component.AbstractComponent implements Component {

    /**
   	 * The XMPP 'Software Version' namespace.
   	 *
   	 * @see <a href="http://xmpp.org/extensions/xep-0092.html">XEP-0029</a>
   	 */
    public static final String NAMESPACE_VERSION = "jabber:iq:version";

    private String componentDescription = null;

    /**
     *
     * @return A human-readable description of the functionality of this component
     */
    @Override
    public final String getDescription() {
        if (componentDescription == null) {
            componentDescription = new StringBuffer()
                    .append(getClass().getName())
                    .append(": ")
                    .append(getVersion()).toString();
        }
        return componentDescription;
    }

    /**
     * Returns the name of this component, including artifact version information for
     * easy visual confirmation of running artifact versions.
     * @return The name of this component, including version information
     */
    @Override
    public abstract String getName();

    @Override
    protected final String[] discoInfoFeatureNamespaces() {
        return ObjectArrays.concat(new String[]{NAMESPACE_VERSION}, setAdditionalFeatureNamespaces(), String.class);
    }

    @Override
    protected final IQ handleIQGet(IQ iq) throws Exception {

        final Element childElement = iq.getChildElement();
        String namespace = null;

        if (childElement != null) {
            namespace = childElement.getNamespaceURI();
        }

        if(NAMESPACE_VERSION.equals(namespace)){
            return (newVersionResponseIQ(iq, getName(), getVersion()));
        }

        return respondToIQGet();

    }

    /**
     * Method to override in order to respond to IQ Get requests. Note this must be overriden in favour of
     * {@link org.xmpp.component.AbstractComponent#handleIQGet(org.xmpp.packet.IQ)}
     * @return
     */
    @Override
    public IQ respondToIQGet(){
        return null;
    }

    /**
     * May be overriden by subclasses to describe any additional feature namespaces implemented by the component.
     * @return The XMPP namespaces of any additional features implemented by the component
     */
    public String[] setAdditionalFeatureNamespaces(){
        return new String[]{};
    }


}
