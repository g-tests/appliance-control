package gtests.appliances.presentation.controller;

import gtests.appliances.presentation.service.ProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static gtests.appliances.presentation.ResourcePaths.Ids;
import static gtests.appliances.presentation.ResourcePaths.Programs;

/**
 * Read-only controller for programs
 *
 * @author g-tests
 */
@RestController
public class ProgramController {

    @Autowired
    private ProgramService programService;

    @GetMapping(Programs.COLLECTION)
    public ResponseEntity<?> list(@PathVariable(Ids.ENDPOINT) String endpointId) {
        return RestResponses.forList(programService.list(endpointId));
    }

    @GetMapping(Programs.ITEM)
    public ResponseEntity<?> list(@PathVariable(Ids.ENDPOINT) String endpointId,
                                  @PathVariable(Ids.PROGRAM) Long programId) {
        return RestResponses.forGet(programService.get(endpointId, programId));
    }
}
