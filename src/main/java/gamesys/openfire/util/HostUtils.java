package gamesys.openfire.util;

import org.xmpp.packet.JID;

public class HostUtils {

    private static final String HOST_SUFFIX = "_host";

    public static boolean isHost(JID jid) {
        return jid != null && jid.getNode().contains(HOST_SUFFIX);
    }

}
