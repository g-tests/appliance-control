package gtests.appliances.util;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;

/**
 * Utility class processing temporal values
 *
 * @author g-tests
 */
public class TemporalConventions {

    /**
     * Default DateTime formatter
     */
    public static final DateTimeFormatter DEFAULT_DT_FORMATTER = ISO_DATE_TIME;

    /**
     * Converts ZonedDateTime to a string
     */
    public static String asString(ZonedDateTime zonedDateTime) {
        return zonedDateTime.format(ISO_DATE_TIME);
    }

    /**
     * Converts a string to ZonedDateTime
     */
    public static ZonedDateTime asZonedDateTime(String string) {
        return ZonedDateTime.from(DEFAULT_DT_FORMATTER.parse(string));
    }
}
