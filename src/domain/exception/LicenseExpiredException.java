package domain.exception;

public class NoLicenceException extends RuntimeException {

    public NoLicenceException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }

}
