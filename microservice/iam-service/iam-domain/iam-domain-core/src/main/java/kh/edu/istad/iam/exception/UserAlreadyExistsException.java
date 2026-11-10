package kh.edu.istad.iam.exception;

/**
 * Thrown when a username or email is already used by another user.
 * Extends IllegalArgumentException so the current exception handler keeps working.
 */
public class UserAlreadyExistsException extends IllegalArgumentException {

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
