package sharedkube.redisson.core.exception;

public class MultiRedissionsNotFoundException extends RuntimeException {

    public MultiRedissionsNotFoundException() {

    }

    public MultiRedissionsNotFoundException(String message) {
        super(message);
    }

    public MultiRedissionsNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
