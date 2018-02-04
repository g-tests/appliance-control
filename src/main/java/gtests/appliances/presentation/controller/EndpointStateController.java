package gtests.appliances.presentation.controller;

import gtests.appliances.presentation.service.EditableStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static gtests.appliances.presentation.ResourcePaths.Endpoints;
import static gtests.appliances.presentation.ResourcePaths.Ids;

/**
 * Rest controller for endpoint's editable state
 *
 * @author g-tests
 */
@RestController
public class EndpointStateController {

    @Autowired
    private EditableStateService editableStateService;

    @GetMapping(Endpoints.EDITABLE_STATE)
    public ResponseEntity<?> get(@PathVariable(Ids.ENDPOINT) String endpointId) {
        return RestResponses.forGet(editableStateService.get(endpointId));
    }

    @PutMapping(Endpoints.EDITABLE_STATE)
    public ResponseEntity<?> update(
            @PathVariable(Ids.ENDPOINT) String endpointId,
            @RequestBody HashMap<String, Object> updatedState,
            HttpServletRequest request) {
        return RestResponses.forPut(
                editableStateService.update(endpointId, updatedState)
                        .map(map -> request.getRequestURL().toString())
                        .map(URI::create));
    }
}
