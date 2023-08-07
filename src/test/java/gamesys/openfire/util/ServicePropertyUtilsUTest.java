package gamesys.openfire.util;

import org.junit.Test;

import static gamesys.openfire.util.ServicePropertyUtils.addToCommaSeparatedStoredValues;
import static gamesys.openfire.util.ServicePropertyUtils.parseCommaSeparatedStoredValues;
import static gamesys.openfire.util.ServicePropertyUtils.removeFromCommaSeparatedStoredValues;
import static org.fest.assertions.api.Assertions.assertThat;

public class ServicePropertyUtilsUTest {
    @Test
    public void testParseStoredValues() throws Exception {

        assertThat(parseCommaSeparatedStoredValues("jungle, casino,")).containsOnly("jungle", "casino");
        assertThat(parseCommaSeparatedStoredValues("")).isEmpty();

    }

    @Test
    public void testAddToCommaSeparatedStoredValues() throws Exception {
        assertThat(parseCommaSeparatedStoredValues(addToCommaSeparatedStoredValues("jungle, casino", "bay"))).
                containsOnly("jungle", "casino", "bay");

        assertThat(parseCommaSeparatedStoredValues(addToCommaSeparatedStoredValues("jungle, bay, casino", "bay"))).
                containsOnly("jungle", "casino", "bay");

        assertThat(parseCommaSeparatedStoredValues(addToCommaSeparatedStoredValues("", "bay"))).
                containsOnly("bay");

    }

    @Test
    public void testRemoveFromCommaSeparatedStoredValues() throws Exception {

        assertThat(parseCommaSeparatedStoredValues(removeFromCommaSeparatedStoredValues("jungle, casino", "casino"))).
                containsOnly("jungle");

        assertThat(parseCommaSeparatedStoredValues(removeFromCommaSeparatedStoredValues("jungle, casino", "bay"))).
                containsOnly("casino", "jungle");

        assertThat(parseCommaSeparatedStoredValues(removeFromCommaSeparatedStoredValues("", "bay"))).
                isEmpty();


    }
}
