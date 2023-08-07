package gamesys.openfire.util;

import org.apache.commons.lang.StringUtils;
import org.xmpp.packet.JID;

public class VentureStringUtils {

    private static String HOST_STRING = "_host";

    public static String getUserName(String username) {
        int indexOfUnderscrore = StringUtils.reverse(username).indexOf("_");
        if (indexOfUnderscrore == -1) {
            return username;
        }
        return username.substring(0, username.length() - (indexOfUnderscrore + 1));
    }

    public static String getVentureName(String username) {
        int indexOfUnderscrore = StringUtils.reverse(username).indexOf("_");
        if (indexOfUnderscrore == -1) {
            return null;
        }
        return username.substring(username.length() - (indexOfUnderscrore));
    }

    public static Boolean isHost(String username) {
        return (username.length() > HOST_STRING.length() && (username.substring(username.length() - HOST_STRING.length())).equals(HOST_STRING));
    }

    public static String getHostUserName(String username) {
        return username.substring(0, username.length() - HOST_STRING.length());
    }

    public static String getVentureOfRoom(JID room) {
        return room.getDomain().split("\\..")[0];
    }

}
