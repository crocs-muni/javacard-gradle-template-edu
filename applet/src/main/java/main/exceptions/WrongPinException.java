package main.exceptions;

public class WrongPinException extends CardRuntimeException {
    public WrongPinException(String message) {
        super(message);
    }
}
