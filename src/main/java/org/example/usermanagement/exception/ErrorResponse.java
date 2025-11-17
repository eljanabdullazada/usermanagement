package org.example.usermanagement.exception;

import java.time.OffsetDateTime;

public record ErrorResponse(OffsetDateTime timestamp, String message) {}
