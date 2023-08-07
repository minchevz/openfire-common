package gamesys.xmpp.test.util;

import gamesys.openfire.infrastructure.system.VentureNotFoundException;
import gamesys.xmpp.component.impl.openfire.AbstractInternalOpenfireComponent;
import gamesys.xmpp.component.impl.openfire.MUCService;
import gamesys.xmpp.component.impl.openfire.RoomVisibilityStrategy;
import org.jivesoftware.openfire.muc.MUCRoom;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractOpenfireComponentTestHarness extends AbstractComponentTestHarness {

    /**
     * Provides a hook to fake the room list for a MUC service.
     * Note, any room lookup up that is not in the supplied list will result in an equivalent exception to that service
     * not being found.
     *
     * @param roomMappings A list of rooms mapped by venture name.
     */
    protected void setRoomList(Map<String, Iterable<MUCRoom>> roomMappings) {
        if (!(getComponentUnderTest() instanceof AbstractInternalOpenfireComponent)) {
            throw new IllegalStateException("You are attempting to test a component that is not an internal openfire component");
        }
        ((AbstractInternalOpenfireComponent) getComponentUnderTest()).setMUCService(new StubMUCService(roomMappings));
    }

    protected void setRoomList(String serviceName, Iterable<MUCRoom> roomMappings){
        Map<String, Iterable<MUCRoom>> mappings = new HashMap<String, Iterable<MUCRoom>>();
        mappings.put(serviceName, roomMappings);
        setRoomList(mappings);
    }

    private class StubMUCService implements MUCService {


        private final Map<String, Iterable<MUCRoom>> roomMappings;

        public StubMUCService(Map<String, Iterable<MUCRoom>> roomMappings) {
            this.roomMappings = roomMappings;
        }

        public Iterable<MUCRoom> getRoomList(String serviceName, RoomVisibilityStrategy roomVisibilityStrategy) {
            if (!roomMappings.containsKey(serviceName)) {
                throw new VentureNotFoundException("Venture " + serviceName + " not found when performing a room occupancy lookup");
            }
            return roomVisibilityStrategy.getVisibleRooms(roomMappings.get(serviceName));
        }
    }


}
