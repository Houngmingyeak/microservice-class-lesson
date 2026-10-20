package kh.edu.istad.platform.customer.domain.exception;

public class CustomerSegmentDomainException extends RuntimeException {
    private final SegmentErrorCode code;

    public CustomerSegmentDomainException(SegmentErrorCode code, String message) {
        super(message);
        this.code = code;
    }
    public SegmentErrorCode getCode() { return code; }
}
