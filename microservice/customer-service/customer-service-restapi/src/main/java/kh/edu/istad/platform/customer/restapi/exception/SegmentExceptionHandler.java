package kh.edu.istad.platform.customer.restapi.exception;

import kh.edu.istad.platform.customer.domain.exception.CustomerSegmentDomainException;
import kh.edu.istad.platform.customer.restapi.controller.CustomerSegmentController;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;

import java.util.stream.Collectors;

@RestControllerAdvice(assignableTypes = CustomerSegmentController.class)
public class SegmentExceptionHandler {

    @ExceptionHandler(CustomerSegmentDomainException.class)
    public ProblemDetail domain(CustomerSegmentDomainException e) {
        HttpStatus status = switch (e.getCode()) {
            case SEGMENT_NOT_FOUND, CUSTOMER_NOT_FOUND, MEMBERSHIP_NOT_FOUND -> HttpStatus.NOT_FOUND;
            case SEGMENT_DUPLICATE, SEGMENT_INVALID_STATE, SEGMENT_NOT_EMPTY, MEMBERSHIP_CONFLICT -> HttpStatus.CONFLICT;
            case SEGMENT_INVALID_INPUT -> HttpStatus.BAD_REQUEST;
        };
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, e.getMessage());
        pd.setProperty("code", e.getCode().name());
        return pd;
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public ProblemDetail validation(WebExchangeBindException e) {
        String msg = e.getFieldErrors().stream()
                .map(f -> f.getField() + ": " + f.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, msg);
    }

    /** Safety net for races on the unique indexes (code / name). */
    @ExceptionHandler(DuplicateKeyException.class)
    public ProblemDetail duplicate(DuplicateKeyException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, "Segment code or name already exists");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail illegalArgument(IllegalArgumentException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }
}
