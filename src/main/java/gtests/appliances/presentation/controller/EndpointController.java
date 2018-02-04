package gtests.appliances.presentation.controller;

import gtests.appliances.presentation.service.EndpointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static gtests.appliances.presentation.ResourcePaths.Endpoints;
import static gtests.appliances.presentation.ResourcePaths.Ids;
import static java.util.Optional.of;

/**
 * Read-only controller for endpoints
 *
 * @author g-tests
 */
@RestController
public class EndpointController {

    @Autowired
    private EndpointService endpointService;

    @GetMapping(Endpoints.COLLECTION)
    public ResponseEntity<?> list() {
        return RestResponses.forGet(of(endpointService.list()));
    }

    @GetMapping(Endpoints.ITEM)
    public ResponseEntity<?> get(@PathVariable(Ids.ENDPOINT) String endpointId) {
        return RestResponses.forGet(endpointService.get(endpointId));
    }
}
