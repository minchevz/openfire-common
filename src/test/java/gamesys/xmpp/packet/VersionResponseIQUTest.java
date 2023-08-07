package gamesys.xmpp.packet;

import gamesys.xmpp.component.AbstractComponent;
import org.fest.assertions.api.Assertions;
import org.junit.Test;
import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;

public class VersionResponseIQUTest {

    @Test
    public void testNewVersionResponseIQ() throws Exception {

        IQ versionRequest = new IQ(IQ.Type.get);
        versionRequest.setID("250-0");
        versionRequest.setChildElement("query", AbstractComponent.NAMESPACE_VERSION);
        versionRequest.setFrom(new JID("12345", "chat", null));
        versionRequest.setTo(new JID("43212", "chat", null));

        IQ iq = VersionResponseIQ.newVersionResponseIQ(versionRequest, "test", "1.0");

        Assertions.assertThat(iq.toXML()).
                isEqualTo("<iq type=\"result\" id=\"250-0\" from=\"43212@chat\" to=\"12345@chat\"><query xmlns=\"jabber:iq:version\"><name>test</name><version>1.0</version></query></iq>");

    }
}
