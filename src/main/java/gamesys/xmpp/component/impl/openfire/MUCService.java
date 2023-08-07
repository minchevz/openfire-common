package gamesys.xmpp.component.impl.openfire;

import org.jivesoftware.openfire.muc.MUCRoom;

public interface MUCService {
    
    Iterable<MUCRoom> getRoomList(String serviceName, RoomVisibilityStrategy roomVisibilityStrategy);
}
