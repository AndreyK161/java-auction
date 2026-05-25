package ws.academy.auction.core.exception;

public class InvalidBidValidationException extends RuntimeException {
    public InvalidBidValidationException(String message) {
        super(message);
    }
}
