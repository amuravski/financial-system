package domain.exception;

public class NoSubsidiaryBankException extends RuntimeException {

    public NoSubsidiaryBankException(String errorMessage) {
        super(errorMessage);
    }

}
