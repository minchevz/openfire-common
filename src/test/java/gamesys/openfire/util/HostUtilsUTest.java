package gamesys.openfire.util;

import org.junit.Test;
import org.xmpp.packet.JID;

import static gamesys.openfire.util.HostUtils.isHost;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.*;

public class HostUtilsUTest {
    private static final String BOB_NAME = "bob_jackpotjoy";
    private static final String HOST_NAME = "chatadmin_host_jackpotjoy";
    private static final String DOMAIN = "mychatserver";
    private static final String JACKPOTJOY_VENTURE = "jackpotjoy";

    @Test
    public void testIsHost() throws Exception {

        JID host = new JID(HOST_NAME, DOMAIN, null);
        JID bob = new JID(BOB_NAME, DOMAIN, null);

        System.out.println(host.toBareJID());

        assertThat(isHost(host)).isTrue();
        assertThat(isHost(bob)).isFalse();
        assertThat(isHost(null)).isFalse();

    }


}