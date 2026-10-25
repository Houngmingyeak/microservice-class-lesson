// ErrorResponse.java
package kh.edu.istad.platform.payment.restapi.dto;

import java.time.ZonedDateTime;
import java.util.List;

public record ErrorResponse(int status, String message, List<String> details, ZonedDateTime timestamp) {
}