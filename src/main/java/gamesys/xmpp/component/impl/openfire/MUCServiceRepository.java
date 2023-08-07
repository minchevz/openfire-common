package gamesys.xmpp.component.impl.openfire;

import org.jivesoftware.openfire.muc.MultiUserChatService;

public interface MUCServiceRepository {

    MultiUserChatService find(String venture);

}
