package ws.academy.auction.core.exception;

public class StorageFileUploadException extends RuntimeException {
    public StorageFileUploadException(String message) {
        super(message);
    }

    public StorageFileUploadException(String message, Exception e) {
        super(message, e);
    }
}
