package gtests.appliances.test.util;

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
import gtests.appliances.persistence.model.Endpoint;
import gtests.appliances.persistence.model.EndpointJob;
import gtests.appliances.persistence.model.EndpointProgram;

import java.io.File;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

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

    /**
     * Provides endpoint object without jobs in it
     *
     * @param directoryName directory containing data
     * @return {@link Endpoint}
     */
    public static Endpoint getEndpointWithPrograms(String directoryName) throws IOException {
        File file = new File(RESOURCES_PATH + directoryName + "/endpoint.json");
        Endpoint endpoint = MAPPER.readValue(file, Endpoint.class);
        Map<String, EndpointProgram> programs = getPrograms(directoryName, endpoint);
        endpoint.setAvailablePrograms(new HashSet<>(programs.values()));
        return endpoint;
    }

    /**
     * Adds jobs to provided endpoint, links them to <b>random</b> programs from the endpoint
     *
     * @param endpoint      endpoint
     * @param directoryName directory containing data
     */
    public static void addJobsToEndpoint(Endpoint endpoint, String directoryName) throws IOException {
        Map<Boolean, List<EndpointJob>> isPendingJobs = getJobsWithRandomPrograms(directoryName, endpoint)
                .stream()
                .collect(Collectors
                        .groupingBy(endpointJob -> endpointJob.getFinished() == null));
        endpoint.setCompletedJobs(new HashSet<>(isPendingJobs.get(false)));
        endpoint.setPendingJobs(new HashSet<>(isPendingJobs.get(true)));
    }

    private static Map<String, EndpointProgram> getPrograms(String directoryName, Endpoint endpoint) throws IOException {
        File file = new File(RESOURCES_PATH + directoryName + "/programs.json");
        List<EndpointProgram> list = MAPPER.readValue(file, new TypeReference<List<EndpointProgram>>() {
        });
        return list
                .stream()
                .peek(program -> program.setEndpoint(endpoint))
                .collect(Collectors.toMap(EndpointProgram::getName, endpointProgram -> endpointProgram));
    }

    private static List<EndpointJob> getJobsWithRandomPrograms(String directoryName, Endpoint endpoint) throws IOException {
        File file = new File(RESOURCES_PATH + directoryName + "/jobs.json");
        List<EndpointJob> list = MAPPER.readValue(file, new TypeReference<List<EndpointJob>>() {
        });
        for (EndpointJob job : list) {
            job.setProgram(getRandomValue(endpoint.getAvailablePrograms()));
            job.setEndpoint(endpoint);
        }
        return list;
    }

    private static <T> T getRandomValue(Collection<T> programs) {
        int rand = new Random().nextInt(programs.size());
        Iterator<T> iterator = programs.iterator();
        for (int i = 0; i < rand - 1; i++, iterator.next()) {
        }
        return iterator.next();
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
