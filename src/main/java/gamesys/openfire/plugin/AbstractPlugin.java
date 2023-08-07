package gamesys.openfire.plugin;

import com.google.inject.Injector;
import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.component.Component;
import org.xmpp.component.ComponentException;
import org.xmpp.component.ComponentManager;

import static org.xmpp.component.ComponentManagerFactory.getComponentManager;

public abstract class AbstractPlugin {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractPlugin.class);

    private CamelContext camelContext;

    private String pluginName;

    protected void initialise(String pluginName) {
        this.pluginName = pluginName;
    }

    protected void registerComponent(String subDomain, Component component) throws ComponentException {
        ComponentManager componentManager = getComponentManager();
        try {
            componentManager.addComponent(subDomain, component);
            LOGGER.info("Plugin [{}] registered component [{}] on sub-domain [{}]", pluginName, component.getName(), subDomain);
        } catch (ComponentException e) {
            LOGGER.error("Exception in plugin [{}] while registering component [{}] on sub-domain [{}]", pluginName, component.getName(), subDomain, e);
            throw e;
        }
    }

    protected void deregisterComponent(String subDomain) throws ComponentException {
        ComponentManager componentManager = getComponentManager();
        try {
            componentManager.removeComponent(subDomain);
            LOGGER.info("Plugin [{}] de-registered component on sub-domain [{}]", pluginName, subDomain);
        } catch (ComponentException e) {
            LOGGER.error("Exception in plugin [{}] while de-registering component on sub-domain [{}]", pluginName, subDomain, e);
            throw e;
        }
    }

    protected void startCamelContext(Injector injector) {
        LOGGER.info("About to start Camel context in [{}] plugin", pluginName);

        camelContext = injector.getInstance(CamelContext.class);
        if (camelContext instanceof DefaultCamelContext) {
            LOGGER.info("Retrieved DefaultCamelContext instance, starting Camel context in [{}] plugin if not already started", pluginName);

            if (!((DefaultCamelContext) camelContext).isStarted()) {
                try {
                    camelContext.start();
                    LOGGER.info("Camel context has started in [{}] plugin", pluginName);
                } catch (Exception e) {
                    LOGGER.error("Failed to start Camel context in [{}] plugin", pluginName, e);
                    throw new RuntimeException("Cannot start Camel context in [" + pluginName + "] plugin", e);
                }
            }
        }
    }

    protected void stopCamelContext() {
        LOGGER.info("About to stop Camel context in [{}] plugin", pluginName);
        try {
            if (camelContext != null) {
                camelContext.stop();
                LOGGER.info("Camel context in [{}] plugin has been stopped", pluginName);
            }
        } catch (Exception e) {
            LOGGER.error("Failed to stop Camel context in [{}] plugin", pluginName, e);
            throw new RuntimeException("Cannot stop Camel context in [" + pluginName + "] plugin", e);
        }
    }
}
