package gamesys.xmpp.component;

import gamesys.xmpp.packet.VersionResponseIQ;
import gamesys.xmpp.test.util.AbstractComponentTestHarness;
import org.junit.Test;
import org.xmpp.packet.IQ;

public class AbstractComponentUTest extends AbstractComponentTestHarness {

    public static final String VERSION = "1.0";
    public static final String NAME = "test";
    private final AbstractComponentUTest.TestComponent testObject;

    private final String version;
    private final String name;

    public AbstractComponentUTest() {
        version = VERSION;
        name = NAME;
        testObject = new TestComponent(name, version, name);
    }

    @Override
    protected AbstractComponent setComponentUnderTest() {
        return new TestComponent(name, version, name);
    }

    @Test
    public void verifyComponentRespondsToPingsWithVersionNumber() throws Exception {

        IQ versionRequest = new IQ(IQ.Type.get);
        versionRequest.setChildElement("query", AbstractComponent.NAMESPACE_VERSION);

        processPlayerPacket(versionRequest);

        assertSent(VersionResponseIQ.newVersionResponseIQ(versionRequest, name, version));

    }

    class TestComponent extends AbstractComponent {

        private final String version;

        private final String subDomain;

        private final String name;

        TestComponent(String subDomain, String version, String name) {
            this.version = version;
            this.subDomain = subDomain;
            this.name = name;
        }

        @Override
        public String getVersion() {
            return version;
        }

        @Override
        public String getSubDomain() {
            return subDomain;
        }

        @Override
        public String getName() {
            return name;
        }
    }

}
