package gtests.appliances.presentation.controller;

import gtests.appliances.presentation.service.JobService;
import gtests.appliances.presentation.view.JobMainView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;

import static gtests.appliances.presentation.ResourcePaths.Ids;
import static gtests.appliances.presentation.ResourcePaths.Jobs;

/**
 * Rest controller for endpoint's jobs access
 *
 * @author g-tests
 */
@RestController
public class JobController {

    @Autowired
    private JobService jobService;

    @GetMapping(Jobs.COLLECTION)
    public ResponseEntity<?> list(@PathVariable(Ids.ENDPOINT) String endpointId) {
        return RestResponses.forGet(jobService.list(endpointId));
    }

    @PostMapping(Jobs.COLLECTION)
    public ResponseEntity<?> create(
            HttpServletRequest request,
            @PathVariable(Ids.ENDPOINT) String endpointId,
            @Valid @RequestBody JobMainView jobView) {
        return RestResponses.forPost(
                jobService
                        .create(jobView, endpointId)
                        .map(view ->
                                request.getRequestURL()
                                        .append("/")
                                        .append(view.getId())
                                        .toString())
                        .map(URI::create));
    }

    @GetMapping(Jobs.ITEM)
    public ResponseEntity<?> get(
            @PathVariable(Ids.ENDPOINT) String endpointId,
            @PathVariable(Ids.JOB) Long jobId) {
        return RestResponses.forGet(jobService.read(endpointId, jobId));
    }

    @PutMapping(Jobs.ITEM)
    public ResponseEntity<?> update(
            HttpServletRequest request,
            @PathVariable(Ids.ENDPOINT) String endpointId,
            @PathVariable(Ids.JOB) Long jobId,
            @Valid @RequestBody JobMainView jobView) {
        jobView.setId(jobId);
        return RestResponses.forPut(
                jobService.update(jobView, endpointId)
                        .map(view -> URI.create(request.getRequestURL().toString())));
    }

    @DeleteMapping(Jobs.ITEM)
    public ResponseEntity<?> delete(@PathVariable(Ids.ENDPOINT) String endpointId,
                                    @PathVariable(Ids.JOB) Long jobId) {
        return RestResponses.forDelete(!jobService.delete(jobId, endpointId).isPresent());
    }
}
