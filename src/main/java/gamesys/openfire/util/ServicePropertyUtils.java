package gamesys.openfire.util;

import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;

import java.util.Set;

import static com.google.common.collect.ImmutableSet.copyOf;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;

public class ServicePropertyUtils {

    public static final char SEPARATOR = ',';

    public static Set<String> parseCommaSeparatedStoredValues(String storedValues) {

        return copyOf(Splitter.on(',').trimResults().omitEmptyStrings().split(storedValues));

    }

    public static String addToCommaSeparatedStoredValues(String storedValues, String valueToBeAdded) {

        ImmutableSet.Builder<String> builder = ImmutableSet.builder();

        builder.addAll(parseCommaSeparatedStoredValues(storedValues));
        builder.add(valueToBeAdded);

        return Joiner.on(SEPARATOR).join(builder.build());
    }

    public static String removeFromCommaSeparatedStoredValues(String storedValues, final String valueToBeRemoved) {

        return Joiner.on(SEPARATOR).join(filter(
                parseCommaSeparatedStoredValues(storedValues),
                new Predicate<String>() {
            @Override
            public boolean apply(String value) {
                return !value.equalsIgnoreCase(valueToBeRemoved);
            }
        }));
    }

}
