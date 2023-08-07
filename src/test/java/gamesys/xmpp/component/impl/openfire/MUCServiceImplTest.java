package gamesys.xmpp.component.impl.openfire;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.jivesoftware.openfire.muc.MUCRoom;
import org.jivesoftware.openfire.muc.MultiUserChatService;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MUCServiceImplTest {
    
    @Test
    public void visibleRoomsReturned() {
        String venture = "Starspins";
        
        MUCRoom visibleRoom = mock(MUCRoom.class);
        MUCRoom invisibleRoom = mock(MUCRoom.class);
        List<MUCRoom> allRooms = Arrays.asList(visibleRoom, invisibleRoom);
        List<MUCRoom> visibleRooms = Arrays.asList(visibleRoom);
       
        MultiUserChatService multiUserChatService = mock(MultiUserChatService.class);
        when(multiUserChatService.getChatRooms()).thenReturn(allRooms);
        
        RoomVisibilityStrategy roomVisibilityStrategy = mock(RoomVisibilityStrategy.class);
        when(roomVisibilityStrategy.getVisibleRooms(allRooms)).thenReturn(visibleRooms);
        
        MUCServiceRepository mockRepository = mock(MUCServiceRepository.class);
        when(mockRepository.find(venture)).thenReturn(multiUserChatService);
        
        MUCServiceImpl mucService = new MUCServiceImpl(mockRepository);
        Iterable<MUCRoom> roomList = mucService.getRoomList(venture, roomVisibilityStrategy);
        
        assertEquals("The visible rooms returned are not correct", visibleRooms, roomList);
    }

    @Test
    public void noRoomsResultsInEmptyRoomList() {
        MUCServiceRepository mockRepository = mock(MUCServiceRepository.class);
        RoomVisibilityStrategy mockRoomVisibilityStrategy = mock(RoomVisibilityStrategy.class);
        String serviceName = "Starspins";
        
        MUCServiceImpl mucService = new MUCServiceImpl(mockRepository);
        Iterable<MUCRoom> roomList = mucService.getRoomList(serviceName, mockRoomVisibilityStrategy);
        
        int expectedNumberOfRooms = 0;
        int actualNumberOfRooms = countItemsInIterable(roomList);
        
        assertEquals("The rooms list should be empty", expectedNumberOfRooms, actualNumberOfRooms);
    }

    private int countItemsInIterable(Iterable<?> iterable) {
        Iterator<?> roomListIterator = iterable.iterator();
        int actualNumberOfRooms = 0;
        
        while(roomListIterator.hasNext()) {
            actualNumberOfRooms = actualNumberOfRooms + 1;
            roomListIterator.next();
        }
        
        return actualNumberOfRooms;
    }
}
