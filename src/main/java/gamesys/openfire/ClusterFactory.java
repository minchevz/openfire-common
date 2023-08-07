package gamesys.openfire;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.hazelcast.config.Config;
import com.hazelcast.config.FileSystemXmlConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.cluster.ClusterEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;

import static com.google.common.base.Optional.*;
import static com.google.common.collect.Iterables.filter;
import static com.hazelcast.core.Hazelcast.getAllHazelcastInstances;
import static java.lang.String.format;
import static org.jivesoftware.openfire.cluster.ClusterManager.isClusteringEnabled;
import static org.jivesoftware.openfire.cluster.ClusterManager.isClusteringStarted;
import static org.jivesoftware.util.JiveGlobals.getHomeDirectory;
import static org.jivesoftware.util.JiveGlobals.getProperty;

public class ClusterFactory {

    private final static Logger LOG = LoggerFactory.getLogger(ClusterFactory.class);

    private static final String HAZELCAST_CONFIG_DIR = getProperty(
            "hazelcast.config.xml.directory", getHomeDirectory()
                    + "/conf");

    private static final String HAZELCAST_CONFIG_FILE =
            getProperty("hazelcast.config.xml.filename", "hazelcast-cache-config.xml");

    public static void reloadPluginIfNeeded(String pluginName) {
        if (isClusteringEnabled() && !isClusteringStarted()) {
            LOG.error(format("Hazelcast instance not found by %s. Reloading... ", pluginName));
            XMPPServer.getInstance().getPluginManager().reloadPlugin(pluginName);
        }
    }

    public static Optional<HazelcastInstance> getExistingHazelcastInstance() {
        if (isClusteringStarted()) {
            return fromNullable(getAllHazelcastInstances().iterator().next());
        } else {
            return absent();
        }

    }

    public static Optional<HazelcastInstance> newHazelcastInstance(String name) {
        final Optional<Config> config = initConfig(name);
        return config.isPresent() ? fromNullable(Hazelcast.newHazelcastInstance(config.get())) : Optional.<HazelcastInstance>absent();
    }

    public static void shutdownAllInstancesWithName(final String name) {
        shutdownInstances(filter(getAllHazelcastInstances(), new Predicate<HazelcastInstance>() {
            @Override
            public boolean apply(HazelcastInstance hazelcastInstance) {
                return hazelcastInstance.getName().equalsIgnoreCase(name);
            }
        }));
    }

    private static void shutdownInstances(Iterable<HazelcastInstance> instances) {
        for (HazelcastInstance instance : instances) {
            instance.shutdown();
        }
    }

    private static Optional<Config> initConfig(String name) {
        try {
            Config config = new FileSystemXmlConfig(buildConfigFilePath());
            config.setInstanceName(name);
            return of(config);
        } catch (FileNotFoundException e) {
            LOG.error("config could not be loaded!", e);
            return absent();
        }
    }

    private static String buildConfigFilePath() {
        return format("%s%s%s", HAZELCAST_CONFIG_DIR, System.getProperty("file.separator"), HAZELCAST_CONFIG_FILE);
    }

    private ClusterFactory() {
    }

    public static ClusterEventListener newClusterEventListener(String pluginName) {
        return new ClusterEventListenerImpl(pluginName);
    }


    private static class ClusterEventListenerImpl implements ClusterEventListener {

        private final String pluginName;

        private ClusterEventListenerImpl(String pluginName) {
            this.pluginName = pluginName;
        }

        @Override
        public void joinedCluster() {
            LOG.info(pluginName + " just joined the Hazelcast cluster. Restarting now");
            XMPPServer.getInstance().getPluginManager().reloadPlugin(pluginName);
        }

        @Override
        public void joinedCluster(byte[] bytes) {

        }

        @Override
        public void leftCluster() {

        }

        @Override
        public void leftCluster(byte[] bytes) {

        }

        @Override
        public void markedAsSeniorClusterMember() {

        }
    }
}
