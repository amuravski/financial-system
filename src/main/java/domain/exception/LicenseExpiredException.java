package domain.exception;

public class LicenseExpiredException extends RuntimeException {

    public LicenseExpiredException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }

}
