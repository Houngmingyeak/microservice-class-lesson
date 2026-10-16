package kh.edu.istad.platform.order.restapi.dto;

import java.util.List;

public record CancelOrderRequest(
        List<String> failureMessages
) {
}
