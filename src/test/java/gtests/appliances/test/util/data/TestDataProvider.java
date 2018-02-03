package gtests.appliances.test.util.data;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.File;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

/**
 * Provides test data from json resources
 *
 * @author g-tests
 */
public class TestDataProvider {

    public static final String RESOURCES_PATH = "src/test/resources/test_objects/";
    private static final ObjectMapper MAPPER;

    static {
        MAPPER = new ObjectMapper();
        MAPPER.configure(SerializationFeature.INDENT_OUTPUT, true);
        SimpleModule module = new SimpleModule();
        module.addSerializer(ZonedDateTime.class, new ZDTSerializer(null));
        module.addDeserializer(ZonedDateTime.class, new ZDTDeserializer(null));
        MAPPER.registerModule(module);
    }

    public static <T> T getRandomValue(Collection<T> programs) {
        int rand = new Random().nextInt(programs.size());
        Iterator<T> iterator = programs.iterator();
        for (int i = 0; i < rand - 1; i++, iterator.next()) {
        }
        return iterator.next();
    }

    /**
     * Looks for the path relative to test objects root and deserializes the file if found
     *
     * @param path path to json file
     * @param type type to deserialize
     */
    public static <T> T getDeserialized(String path, Class<T> type) {
        File file = new File(RESOURCES_PATH + path);
        try {
            return MAPPER.readValue(file, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Looks for the path relative to test objects root and deserializes the file if found
     *
     * @param path path to json file
     * @param type type to deserialize
     */
    public static <T> T getDeserialized(String path, TypeReference<T> type) {
        File file = new File(RESOURCES_PATH + path);
        try {
            return MAPPER.readValue(file, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static class ZDTSerializer extends StdSerializer<ZonedDateTime> {

        protected ZDTSerializer(Class<ZonedDateTime> t) {
            super(t);
        }

        @Override
        public void serialize(ZonedDateTime value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeString(value.format(DateTimeFormatter.ISO_ZONED_DATE_TIME));
        }
    }

    private static class ZDTDeserializer extends StdDeserializer<ZonedDateTime> {

        protected ZDTDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public ZonedDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String string = p.getValueAsString();
            TemporalAccessor parse = DateTimeFormatter.ISO_ZONED_DATE_TIME.parse(string);
            return ZonedDateTime.from(parse);
        }
    }
}
