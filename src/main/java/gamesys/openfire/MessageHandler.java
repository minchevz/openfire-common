package gamesys.openfire;

import com.google.common.base.Predicate;
import org.jivesoftware.openfire.SessionManager;
import org.jivesoftware.openfire.session.ClientSession;
import org.jivesoftware.openfire.session.LocalClientSession;
import org.xmpp.packet.JID;
import org.xmpp.packet.Message;

import java.util.Collection;

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Lists.newArrayList;

public class MessageHandler {

    public static void sendMessageToSession(JID player, Message message) {
        ClientSession clientSession = SessionManager.getInstance().getSession(player);
        message.setTo(clientSession.getAddress());
        message.setFrom(clientSession.getServerName());
        clientSession.process(message);
    }
    
    public static void sendMessageToSession(JID player, JID source, Message message) {
        ClientSession clientSession = SessionManager.getInstance().getSession(player);
        message.setTo(clientSession.getAddress());
        message.setFrom(source);
        clientSession.process(message);
    }

    public static void sendMessageToPlayer(JID player, Message message, boolean localSessionsOnly) {
        for (ClientSession session : retrieveSessions(player, localSessionsOnly)) {
            message.setTo(session.getAddress());
            message.setFrom(session.getServerName());
            session.process(message);
        }
    }

    private static Collection<ClientSession> retrieveSessions(JID player, boolean localSessionsOnly) {
        final Collection<ClientSession> sessions = SessionManager.getInstance().getSessions(getChatUsername(player));
        if (localSessionsOnly) {
            return getLocalMemberSessions(sessions);
        } else {
            return sessions;
        }
    }

    private static Collection<ClientSession> getLocalMemberSessions(Iterable<ClientSession> sessions) {
        return newArrayList(filter(sessions, new Predicate<ClientSession>() {
            @Override
            public boolean apply(ClientSession clientSession) {
                return clientSession instanceof LocalClientSession;
            }
        }));
    }

    private static String getChatUsername(JID jid) {
        return jid.getNode();
    }
}
