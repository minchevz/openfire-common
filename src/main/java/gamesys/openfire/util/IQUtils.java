package gamesys.openfire.util;

import org.xmpp.packet.IQ;
import org.xmpp.packet.Packet;

import static org.xmpp.component.AbstractComponent.NAMESPACE_DISCO_ITEMS;

public class IQUtils {

    private IQUtils() {

    }

    public static boolean isPacketFromHost(Packet packet) {
        return HostUtils.isHost(packet.getFrom());
    }

    /**
     * Provides an IQ GET disco item request with a query namespace, ready to be filled out and an optional node.
     *
     * @param node The node to query on.
     * @return An IQ disco items request with a query namespace and a node
     */
    public static IQ createDiscoItemsPacket(String node) {
        IQ discoItemsPacket = createDiscoItemsPacket();
        discoItemsPacket.getChildElement().addAttribute("node", node);
        return discoItemsPacket;
    }

    /**
     * Provides an IQ GET disco item request with a query namespace, ready to be filled out.
     *
     * @return An IQ disco items request with a query namespace
     */
    public static IQ createDiscoItemsPacket() {
        IQ discoItemsRequest = new IQ(IQ.Type.get);
        discoItemsRequest.setChildElement("query", NAMESPACE_DISCO_ITEMS);
        return discoItemsRequest;
    }

}
