package gamesys.xmpp.packet;

import gamesys.xmpp.component.AbstractComponent;
import org.dom4j.Element;
import org.xmpp.packet.IQ;

public class VersionResponseIQ {

    public static IQ newVersionResponseIQ(IQ originalRequest, String name, String version) {
        IQ iq = new IQ(IQ.Type.result, originalRequest.getID());

        iq.setFrom(originalRequest.getTo());
        iq.setTo(originalRequest.getFrom());
        Element query = iq.setChildElement("query", AbstractComponent.NAMESPACE_VERSION);
        query.addElement("name").addText(name);
        query.addElement("version").addText(version);

        return iq;
    }


}
