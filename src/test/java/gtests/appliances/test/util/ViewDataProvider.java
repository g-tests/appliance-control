package gtests.appliances.test.util;

import gtests.appliances.presentation.view.JobMainView;

import static gtests.appliances.test.util.TestDataProvider.getDeserialized;

/**
 * Provides data for view types
 *
 * @author g-tests
 */
public class ViewDataProvider {

    private static final String VIEWS_PATH = "views/";
    private static final String EDITABLE_STATE_JSON = "/editable_state.json";
    private static final String JOBS_JSON = "/job.json";

    /**
     * Provides job main view
     *
     * @param directoryName directory containing data
     * @return {@link JobMainView}
     */
    public static JobMainView getJobMainView(String directoryName) {
        String path = VIEWS_PATH + directoryName + JOBS_JSON;
        return getDeserialized(path, JobMainView.class);
    }
}
