/*
* This class represents an API error response.
* It contains information about the error, including the timestamp, HTTP status code, error message, and any additional details.
*/
package com.example.inventory.exception;

import java.time.Instant;
import java.util.List;

public record ApiError(Instant timestamp, int status, String message, List<String> details) {

    public ApiError(int status, String message) {
        this(Instant.now(), status, message, List.of());
    }

    public ApiError(int status, String message, List<String> details) {
        this(Instant.now(), status, message, details);
    }
}
