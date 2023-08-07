package gamesys.openfire.util;

import static gamesys.openfire.util.VentureStringUtils.*;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.xmpp.packet.JID;

public class VentureStringUtilsUTest {

	@Test
	public void testGetUserName() {
        assertThat(getUserName("user_name_harrahs")).isEqualTo("user_name");
	}

	@Test
	public void testGetVentureName() {
        assertThat(getVentureName("user_name_harrahs")).isEqualTo("harrahs");
	}

	@Test
	public void testIsHost() {
        assertThat(isHost("username_host")).isTrue();
	}
	
	@Test
	public void testGetHostUsername() {
        assertThat(getHostUserName("username_host")).isEqualTo("username");
	}

    @Test
    public void testGetVentureOfRoom() throws Exception {
        JID room = new JID("cloud", "jackpotjoy.chat", "");
        assertThat(getVentureOfRoom(room)).isEqualTo("jackpotjoy");
    }
}
