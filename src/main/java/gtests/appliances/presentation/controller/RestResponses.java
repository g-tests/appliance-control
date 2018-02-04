package gtests.appliances.presentation.controller;

import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.Optional;

/**
 * Static factory for typical response entities
 *
 * @author g-tests
 */
public class RestResponses {

    /**
     * Produces typical response for POST request
     *
     * @param createdLocation optional URI of created resource (empty if parent resource is missing)
     * @return {@link ResponseEntity}
     */
    public static ResponseEntity<?> forPost(Optional<URI> createdLocation) {
        return createdLocation
                .map(RestResponses::createdResponse)
                .orElseGet(RestResponses::notFoundResponse);
    }

    /**
     * Produces typical response for GET request to collection resource
     *
     * @param responseBody list of found resources (empty if parent resource is missing)
     * @return {@link ResponseEntity}
     */
    public static <BT> ResponseEntity<BT> forList(Optional<BT> responseBody) {
        return forGet(responseBody);
    }


    /**
     * Produces typical response for GET request to single resource
     *
     * @param responseBody found resource (empty if either parent or requested resource itself is missing)
     * @return {@link ResponseEntity}
     */
    public static <BT> ResponseEntity<BT> forGet(Optional<BT> responseBody) {
        return responseBody
                .map(ResponseEntity::ok)
                .orElseGet(RestResponses::notFoundResponse);
    }

    /**
     * Produces typical response for PUT request
     *
     * @param updatedLocation optional URI of updated resource (empty if parent resource is missing)
     * @return {@link ResponseEntity}
     */
    public static ResponseEntity<?> forPut(Optional<URI> updatedLocation) {
        return updatedLocation
                .map(RestResponses::updatedResponse)
                .orElseGet(RestResponses::notFoundResponse);
    }

    private static <BT> ResponseEntity<BT> updatedResponse(URI uri) {
        return ResponseEntity.noContent().location(uri).build();
    }

    public static ResponseEntity<?> forDelete(boolean parentIsMissing) {
        return parentIsMissing ?
                ResponseEntity.notFound().build() :
                ResponseEntity.noContent().build();
    }

    private static <BT> ResponseEntity<BT> createdResponse(URI location) {
        return ResponseEntity.created(location).build();
    }

    private static <T> ResponseEntity<T> notFoundResponse() {
        return ResponseEntity.notFound().build();
    }


}
