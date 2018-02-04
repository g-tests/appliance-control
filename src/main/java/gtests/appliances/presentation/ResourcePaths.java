package gtests.appliances.presentation;

/**
 * Paths and path variables used for resource mapping
 *
 * @author g-tests
 */
public class ResourcePaths {

    /**
     * Identifiers used for path variables
     */
    public interface Ids {
        String ENDPOINT = "endpointId";
        String JOB = "jobId";
        String PROGRAM = "programId";
    }

    /**
     * Paths for endpoint resource mapping
     */
    public interface Endpoints {
        String COLLECTION = "/endpoints";
        String ITEM = COLLECTION + "/{" + Ids.ENDPOINT + "}";
        String EDITABLE_STATE = ITEM + "/state";
    }

    /**
     * Paths for job resource mapping
     */
    public interface Jobs {
        String COLLECTION = Endpoints.ITEM + "/jobs";
        String ITEM = COLLECTION + "/{" + Ids.JOB + "}";
    }

    /**
     * Paths for program resource mapping
     */
    public interface Programs {
        String COLLECTION = Endpoints.ITEM + "/programs";
        String ITEM = COLLECTION + "/{" + Ids.PROGRAM + "}";
    }
}
