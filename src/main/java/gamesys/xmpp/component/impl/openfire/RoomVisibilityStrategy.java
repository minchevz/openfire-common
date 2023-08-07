package gamesys.xmpp.component.impl.openfire;

import org.jivesoftware.openfire.muc.MUCRoom;

import java.io.Serializable;

public interface RoomVisibilityStrategy extends Serializable {

    Iterable<MUCRoom> getVisibleRooms(Iterable<MUCRoom> mucRoomList);

}
