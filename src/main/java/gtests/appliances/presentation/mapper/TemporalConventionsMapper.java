package gtests.appliances.presentation.mapper;

import gtests.appliances.util.TemporalConventions;
import org.mapstruct.Mapper;

import java.time.ZonedDateTime;

/**
 * DateTime mapper to be used in other mappers
 *
 * @author g-tests
 */
@Mapper
public interface TemporalConventionsMapper {

    /**
     * Serializes date to string using {@link TemporalConventions}
     */
    default String zdtToString(ZonedDateTime zonedDateTime) {
        return TemporalConventions.asString(zonedDateTime);
    }

    /**
     * Deserializes date from string using {@link TemporalConventions}
     */
    default ZonedDateTime zdtToString(String string) {
        return TemporalConventions.asZonedDateTime(string);
    }
}
