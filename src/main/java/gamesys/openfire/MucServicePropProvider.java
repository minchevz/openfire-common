package gamesys.openfire;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

import static gamesys.openfire.util.ServicePropertyUtils.addToCommaSeparatedStoredValues;
import static gamesys.openfire.util.ServicePropertyUtils.parseCommaSeparatedStoredValues;
import static gamesys.openfire.util.ServicePropertyUtils.removeFromCommaSeparatedStoredValues;
import static java.lang.String.format;
import static org.jivesoftware.openfire.muc.spi.MUCPersistenceManager.getProperty;
import static org.jivesoftware.openfire.muc.spi.MUCPersistenceManager.refreshProperties;
import static org.jivesoftware.openfire.muc.spi.MUCPersistenceManager.setProperty;

public class MucServicePropProvider {

    private static final String INVISIBLE_ROOMS = "invisible.rooms";
    private static final String STREAMING_ROOMS = "streaming.rooms";

    private final static Logger LOGGER = LoggerFactory.getLogger(MucServicePropProvider.class);

    private MucServicePropProvider() {
    }

    public static synchronized Set<String> getStreamingRooms(String venture) {
        return parseCommaSeparatedStoredValues(getStreamingRoomsProperty(venture));
    }

    public static synchronized void addToStreamingRooms(String roomName, String venture) {
        setProperty(venture, STREAMING_ROOMS, addToCommaSeparatedStoredValues(getStreamingRoomsProperty(venture), roomName));
    }

    public static synchronized void removeFromStreamingRooms(String roomName, String venture) {
        setProperty(venture, STREAMING_ROOMS, removeFromCommaSeparatedStoredValues(getStreamingRoomsProperty(venture), roomName));
    }

    public static synchronized Set<String> getInvisibleRooms(String venture) {
        return parseCommaSeparatedStoredValues(getInvisibleRoomsProperty(venture));
    }

    public static synchronized void addToInvisibleRooms(String roomName, String venture) {
        setProperty(venture, INVISIBLE_ROOMS, addToCommaSeparatedStoredValues(getInvisibleRoomsProperty(venture), roomName));
    }

    public static synchronized void removeFromInvisibleRooms(String roomName, String venture) {
        setProperty(venture, INVISIBLE_ROOMS, removeFromCommaSeparatedStoredValues(getInvisibleRoomsProperty(venture), roomName));
    }

    private static String getInvisibleRoomsProperty(String venture) {
        return getPropertyValue(venture, INVISIBLE_ROOMS);
    }

    private static String getStreamingRoomsProperty(String venture) {
        return getPropertyValue(venture, STREAMING_ROOMS);
    }

    private static String getPropertyValue(String venture, String propertyName) {

        try {
            refreshProperties(venture);
            return getProperty(venture, propertyName, "");
        } catch (Exception e) {
            LOGGER.error(format("Error occurred while getting property %s for venture %s", propertyName, venture), e);
            return "";
        }

    }
}
