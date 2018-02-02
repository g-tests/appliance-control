package gtests.appliances.test.util;

import com.fasterxml.jackson.core.type.TypeReference;
import gtests.appliances.persistence.model.Endpoint;
import gtests.appliances.persistence.model.EndpointJob;
import gtests.appliances.persistence.model.EndpointProgram;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static gtests.appliances.test.util.TestDataProvider.*;

/**
 * Provides data for persistent entity types
 *
 * @author g-tests
 */
public class EntityDataProvider {

    private static final String ENTITIES_PATH = "entities/";
    private static final String ENDPOINT_JSON = "/endpoint.json";
    private static final String PROGRAMS_JSON = "/programs.json";
    private static final String JOBS_JSON = "/jobs.json";

    /**
     * Provides full structure of endpoint
     *
     * @return {@link Endpoint}
     */
    public static Endpoint getFullEndpoint(String directoryName) throws IOException {
        Endpoint endpoint = getEndpointWithPrograms(directoryName);
        addJobsToEndpoint(endpoint, directoryName);
        return endpoint;
    }

    /**
     * Provides endpoint object without jobs in it
     *
     * @param directoryName directory containing data
     * @return {@link Endpoint}
     */
    public static Endpoint getEndpointWithPrograms(String directoryName) throws IOException {
        String path = ENTITIES_PATH + directoryName + ENDPOINT_JSON;
        Endpoint endpoint = getDeserialized(path, Endpoint.class);
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

    public static Map<String, EndpointProgram> getPrograms(String directoryName, Endpoint endpoint) throws IOException {
        String path = ENTITIES_PATH + directoryName + PROGRAMS_JSON;
        return getDeserialized(path, new TypeReference<List<EndpointProgram>>() {})
                .stream()
                .peek(program -> program.setEndpoint(endpoint))
                .collect(Collectors.toMap(EndpointProgram::getName, endpointProgram -> endpointProgram));
    }

    private static List<EndpointJob> getJobsWithRandomPrograms(String directoryName, Endpoint endpoint) throws IOException {
        String path = ENTITIES_PATH + directoryName + JOBS_JSON;
        List<EndpointJob> list = getDeserialized(path, new TypeReference<List<EndpointJob>>() {});
        for (EndpointJob job : list) {
            job.setProgram(getRandomValue(endpoint.getAvailablePrograms()));
            job.setEndpoint(endpoint);
        }
        return list;
    }
}
