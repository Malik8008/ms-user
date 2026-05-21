package az.msuser.exception;

public class PhoneNotFoundException extends RuntimeException {
    public PhoneNotFoundException(String message) {
        super(message);
    }
}
