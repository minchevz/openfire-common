package gamesys.xmpp.component.impl.openfire;


import gamesys.xmpp.component.Component;
import gamesys.xmpp.component.ComponentLifecycleException;
import gamesys.xmpp.component.ComponentRegistrar;
import org.jivesoftware.openfire.component.InternalComponentManager;
import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.component.ComponentException;
import org.xmpp.component.ComponentManager;
import org.xmpp.component.ComponentManagerFactory;

import java.io.File;

/**
 * Convenience class for registering and de-registering components through the Openfire
 * plugin mechanism.
 * 
 * Can be used to deploy 'external' components (i.e. components that do not use
 * Openfire API), as well as components that do, into Openfire.
 * 
 * The destruction of the plugin guarantees the {@link org.xmpp.component.Component#shutdown()}
 * method will be invoked while the component is de-registered.
 */
public abstract class OpenfireComponentRegisteringPlugin implements Plugin {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final Component component;

    protected OpenfireComponentRegisteringPlugin(Component component) {
        this.component = component;
    }

    /**
     * Called by Openfire as part of the plugin startup lifecycle
     *
     * @param pluginManager Plugin manager
     * @param file Plugin file
     */
    @Override
    public final void initializePlugin(PluginManager pluginManager, File file) {
        new OpenfireComponentRegistrar().registerComponent(component);
    }

    /**
     * Called by Openfire as part of the plugin shutdown lifecycle
     */
    @Override
    public final void destroyPlugin() {
        new OpenfireComponentRegistrar().deRegisterComponent(component);
    }

    private class OpenfireComponentRegistrar implements ComponentRegistrar {

        @Override
        public void registerComponent(Component component) {
            try {
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
            if (!(componentManager instanceof InternalComponentManager)) {
                throw new IllegalStateException("You are attempting to use an OpenfireComponentRegistrar to register a component outside of Openfire");
            }
            return componentManager;
        }


    }
}
