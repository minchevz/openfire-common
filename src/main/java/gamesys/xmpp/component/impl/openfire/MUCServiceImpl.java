package gamesys.xmpp.component.impl.openfire;

import java.util.Collections;
import java.util.List;

import org.jivesoftware.openfire.muc.MUCRoom;
import org.jivesoftware.openfire.muc.MultiUserChatService;

public class MUCServiceImpl implements MUCService {

    private final MUCServiceRepository mucServiceRepository;

    public MUCServiceImpl(MUCServiceRepository mucServiceRepository) {
        this.mucServiceRepository = mucServiceRepository;
    }

    public Iterable<MUCRoom> getRoomList(String serviceName, RoomVisibilityStrategy roomVisibilityStrategy) {
        Iterable<MUCRoom> visibleRooms = Collections.emptyList();
        MultiUserChatService multiUserChatService = mucServiceRepository.find(serviceName);

        if (multiUserChatService != null) {
            List<MUCRoom> chatRooms = multiUserChatService.getChatRooms();
            visibleRooms = roomVisibilityStrategy.getVisibleRooms(chatRooms);
        }

        return visibleRooms;
    }

}
