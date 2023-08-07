package gamesys.xmpp.test.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.component.*;
import org.xmpp.component.Component;
import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;
import org.xmpp.packet.Packet;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class StubComponentManager implements ComponentManager {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final BlockingQueue<Packet> queue = new LinkedBlockingQueue<Packet>();

    public String getLastSentPacketXML() throws InterruptedException {
        return queue.poll(2, TimeUnit.SECONDS).toXML();
    }

    @Override
    public void addComponent(String subDomain, Component component) throws ComponentException {
        JID componentJID = new JID(subDomain + "." + "chat");
        component.initialize(componentJID, this);
    }

    @Override
    public void removeComponent(String s) throws ComponentException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void sendPacket(Component component, Packet packet) throws ComponentException {
        log.debug("Component received a send packet request with the packet {}", packet);
        queue.add(packet);
    }

    @Override
    public IQ query(Component component, IQ iq, long l) throws ComponentException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void query(Component component, IQ iq, IQResultListener iqResultListener) throws ComponentException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getProperty(String s) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setProperty(String s, String s1) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getServerName() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isExternalMode() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
