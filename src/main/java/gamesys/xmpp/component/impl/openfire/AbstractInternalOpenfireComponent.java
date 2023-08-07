package gamesys.xmpp.component.impl.openfire;

import gamesys.xmpp.component.AbstractComponent;

import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.muc.MUCRoom;
import org.jivesoftware.openfire.muc.MultiUserChatManager;
import org.jivesoftware.openfire.muc.MultiUserChatService;

/**
 * Component implementation specifically for internal Openfire components.
 * Provides access to various internal state not accessible through external components.
 *
 * Note: Be aware of the potential performance limitations of an internal component
 * over an external component as discussed at the link below. If there is no need to access the internal
 * Openfire API, extend from {@link gamesys.xmpp.component.AbstractComponent} instead which is server agnostic.
 * 
 * If you really must use the Openfire API, consider instead wrapping access to it in this
 * class, as it offers protection from breaking changes in the API.
 *
 * @see <a href="http://community.igniterealtime.org/docs/DOC-1925">Openfires Achilles' heel</a>
 */
public abstract class AbstractInternalOpenfireComponent extends AbstractComponent {

    public MUCService mucService;

    /**
     * Returns all rooms for the passed MUC service, with the applied room visibility strategy applied.
     *
     * @param serviceName            The name of the service the callee would like to see the rooms for
     * @param roomVisibilityStrategy A strategy for filtering out any rooms, if desired
     * @return A lits of filtered rooms
     */
    protected Iterable<MUCRoom> getRooms(String serviceName, RoomVisibilityStrategy roomVisibilityStrategy) {
        return getMUCService().getRoomList(serviceName, roomVisibilityStrategy);
    }

    private MUCService getMUCService() {
        if (mucService == null) {
            mucService = new MUCServiceImpl(new MUCServiceRepositoryImpl());
        }
        return mucService;
    }

    // todo Change this to not be available publically, it should only be used by tests
    public void setMUCService(MUCService mucService){
        this.mucService = mucService;
    }

    private class MUCServiceRepositoryImpl implements MUCServiceRepository {

        private final MultiUserChatManager multiUserChatManager;

        public MUCServiceRepositoryImpl() {
            multiUserChatManager = XMPPServer.getInstance().getMultiUserChatManager();
        }

        public MultiUserChatService find(String serviceName) {
            return multiUserChatManager.getMultiUserChatService(serviceName);
        }

    }
}
