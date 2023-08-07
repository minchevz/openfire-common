package gamesys.xmpp.test.util;

import gamesys.xmpp.component.AbstractComponent;
import gamesys.xmpp.component.Component;
import gamesys.xmpp.component.ComponentLifecycleException;
import gamesys.xmpp.component.ComponentRegistrar;
import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.component.ComponentException;
import org.xmpp.component.ComponentManager;
import org.xmpp.component.ComponentManagerFactory;
import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;
import org.xmpp.packet.Packet;

import static org.junit.Assert.assertThat;
import static org.xmlmatchers.XmlMatchers.isEquivalentTo;
import static org.xmlmatchers.transform.XmlConverters.the;
import static org.xmpp.component.AbstractComponent.NAMESPACE_DISCO_ITEMS;

/**
 * Base class for testing components.
 * 
 * Testing components can be tricky and requires a certain amount of setup (e.g. in the case where mocking is employed,
 * setting up a mock component factory and using that to initialize the component). It can be difficult to use a {@link
 * org.hamcrest.Matcher} out of the box to verify packets as {@link org.xmpp.packet.IQ} for example does not override
 * equals and often you won't care to match certain details in the packet, for example the id attribute.
 * 
 * Testing without using mock frameworks also brings its own problems, namely that {@link org.xmpp.component.AbstractComponent}
 * is deliberately multi-threaded, and as such, unit test assertions will often be called before the component has done the work
 * under test.
 * 
 * This class aims to tackle the problems above, and make component unit tests free of the additional noise needed around
 * setup.
 */
public abstract class AbstractComponentTestHarness {

    private AbstractComponent componentUnderTest;

    /**
     * Provides a logger for any subclass to use
     */
    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final StubComponentManager componentManager;

    private final TestComponentRegistrar testComponentRegistrar;

    private JID PLAYER_JID;

    protected AbstractComponentTestHarness() {
        componentManager = new StubComponentManager();
        testComponentRegistrar = new TestComponentRegistrar(componentManager);
    }

    /**
     * Registers the component under test with a component manager.
     * 
     * If any component setup is needed, for example setting mocks on the component, use {@link #setUpComponent()}
     * which is guaranteed to be called before component registration with the component manager
     */
    @Before
    public final void registerComponent() {
        setUpComponent();
        this.componentUnderTest = setComponentUnderTest();
        testComponentRegistrar.registerComponent(componentUnderTest);
    }

    @After
    public final void deRegisterComponent() {
        testComponentRegistrar.deRegisterComponent(componentUnderTest);
    }

    /**
     * Provides a hook to perform any component setup needed before {@link #registerComponent()}
     * is performed.
     */
    protected void setUpComponent() {
    }

    /**
     * Overriden by subclasses to present the component that is under test.
     * This harness guarantees to use the same instance of the component over the course of an individual test, and
     * therefore subclasses do not need to worry about ensuring about managing component instances. It would be
     * perfectly reasonably for a subclass to return a new instance of the component under test in their implementation
     * of this method.
     *
     * @return The component under test
     */
    protected abstract AbstractComponent setComponentUnderTest();

    protected final AbstractComponent getComponentUnderTest() {
        return componentUnderTest;
    }

    /**
     * Asserts that the last packet sent by the component matches what is passed.
     *
     * @param expectedPacket The component the packet is expected to send.
     * @throws Exception The execption
     */
    protected final void assertSent(Packet expectedPacket) throws Exception {

        expectedPacket.setFrom(getComponentUnderTest().getJID());

        assertThat(the(lastSentPacketXML()), isEquivalentTo(the(expectedPacket.toXML())));

    }

    /**
     * Asserts that the expected packet was sent to the player.
     * 
     * This is to be used in collaboration with {@link #processPlayerPacket(org.xmpp.packet.Packet)}. It provides the
     * convenience of not having to setup the 'to' expectation on the packet.
     *
     * @param expectedPacket The packet the component is expected to send
     * @throws Exception The exception
     */
    protected final void assertPacketSentToPlayer(Packet expectedPacket) throws Exception {

        expectedPacket.setTo(PLAYER_JID);
        assertSent(expectedPacket);

    }

    /**
     * Convenience method to process a packet, adding sender information that indicates it has been sent by a member,
     * rather than a host.
     * 
     * The format of the sender's JID will mirror that of a member's JID, for example 12345@chat.
     *
     * @param packet The packet to send.
     */
    protected final void processPlayerPacket(Packet packet) {
        PLAYER_JID = new JID("12345", "chat", null);
        packet.setFrom(PLAYER_JID);
        packet.setTo(getComponentUnderTest().getJID());
        componentUnderTest.processPacket(packet);
    }

    /**
     * Convenience method to process a packet, adding sender information that indicates it has been sent by a host,
     * rather than a member.
     * 
     * The format of the sender's JID will mirror that of a host's JID, for example michael_host_jackpotjoy@chat.
     *
     * @param packet Host packet
     */
    protected final void processHostPacket(Packet packet) {
        packet.setFrom(new JID("michael_host_jackpotjoy", "chat", null));
        packet.setTo(getComponentUnderTest().getJID());
        componentUnderTest.processPacket(packet);
    }

    private String lastSentPacketXML() throws Exception {
        return componentManager.getLastSentPacketXML();
    }

    private class TestComponentRegistrar implements ComponentRegistrar {

        public TestComponentRegistrar(StubComponentManager componentManager) {
            ComponentManagerFactory.setComponentManager(componentManager);
        }

        @Override
        public void registerComponent(Component component) {
            try {
                log.debug("Registering {}", component);
                getComponentManager().addComponent(component.getSubDomain(), component);
            } catch (ComponentException e) {
                log.error("Exception while registering {} component", component.getSubDomain());
                throw new ComponentLifecycleException(e);
            }
        }

        @Override
        public void deRegisterComponent(Component component) {
            try {
                getComponentManager().removeComponent(component.getSubDomain());
            } catch (ComponentException e) {
                log.error("Exception while deregistering {} component", component.getSubDomain());
                throw new ComponentLifecycleException(e);
            }
        }

        private ComponentManager getComponentManager() {
            ComponentManager componentManager = ComponentManagerFactory.getComponentManager();
            if (!(componentManager instanceof StubComponentManager)) {
                throw new IllegalStateException("You are attempting to use a real component manager in a stub test. Ensure no-one has set the ComponentManagerFactory component manager");
            }
            return componentManager;
        }
    }
}
