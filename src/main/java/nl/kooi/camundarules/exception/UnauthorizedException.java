package nl.kooi.camundarules.exception;

public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException() {
        super("User is not authorized to make this request");
    }
}
