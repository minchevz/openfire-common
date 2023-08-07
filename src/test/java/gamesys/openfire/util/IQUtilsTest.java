package gamesys.openfire.util;

import org.junit.Test;
import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;

import static gamesys.openfire.util.IQUtils.createDiscoItemsPacket;
import static gamesys.openfire.util.IQUtils.isPacketFromHost;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.xmpp.component.AbstractComponent.NAMESPACE_DISCO_ITEMS;

public class IQUtilsTest {

    @Test
    public void testIsPacketFromHost() throws Exception {

        IQ iq = new IQ();
        iq.setFrom(new JID("panos", "heart", ""));

        IQ iqFromHost = new IQ();
        iqFromHost.setFrom(new JID("panos_host", "heart", ""));

        assertThat(isPacketFromHost(iq)).isFalse();
        assertThat(isPacketFromHost(iqFromHost)).isTrue();


    }

    @Test
    public void testCreateDiscoItemsPacket() throws Exception {

        IQ discoItemsPacket = createDiscoItemsPacket();

        assertThat(discoItemsPacket.getType()).isEqualTo(IQ.Type.get);
        assertThat(discoItemsPacket.getChildElement().getName()).isEqualTo("query");
        assertThat(discoItemsPacket.getChildElement().getNamespace().getText()).isEqualTo(NAMESPACE_DISCO_ITEMS);
    }

    @Test
    public void testCreateDiscoItemsPacketFromNode() throws Exception {
        String node = "heart";
        IQ discoItemsPacket = createDiscoItemsPacket(node);

        assertThat(discoItemsPacket.getType()).isEqualTo(IQ.Type.get);
        assertThat(discoItemsPacket.getChildElement().getName()).isEqualTo("query");
        assertThat(discoItemsPacket.getChildElement().getNamespace().getText()).isEqualTo(NAMESPACE_DISCO_ITEMS);
        assertThat(discoItemsPacket.getChildElement().attributes()).hasSize(1);
        assertThat(discoItemsPacket.getChildElement().attribute(0).getQName().getName()).isEqualTo("node");
        assertThat(discoItemsPacket.getChildElement().attribute(0).getValue()).isEqualTo(node);




    }
}
