package ws.academy.auction.core.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ws.academy.auction.api.dto.rs.ErrorRs;
import ws.academy.auction.core.exception.InvalidAccountStateException;
import ws.academy.auction.core.exception.StorageFileUploadException;
import ws.academy.auction.core.exception.NotFoundException;

import java.util.Objects;

@Slf4j
@RestControllerAdvice
@SuppressWarnings("rawtypes")
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorRs> handleNotFoundException(NotFoundException e) {
        log.error(e.getMessage(), e);

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorRs.builder()
                        .message(e.getMessage())
                        .build());
    }

//    TODO добавить свои исключения и их обработку

    @ExceptionHandler(StorageFileUploadException.class)
    public ResponseEntity<ErrorRs> handleStorageFileUploadException(StorageFileUploadException e) {
        log.error(e.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorRs.builder()
                        .message(e.getMessage())
                        .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorRs> handleUnexpectedException(Exception e) {
        log.error(e.getMessage(), e);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorRs.builder()
                        .message(e.getMessage())
                        .build());
    }

    @ExceptionHandler(InvalidAccountStateException.class)
    public ResponseEntity<ErrorRs> handleAccountNotActivatedException(InvalidAccountStateException e) {
        log.error(e.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorRs.builder()
                        .message(e.getMessage())
                        .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorRs> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorRs.builder()
                        .message("Ошибка валидации: " + Objects.requireNonNull(e.getFieldError()).getDefaultMessage())
                        .data(Objects.requireNonNull(e.getTarget()).getClass().getSimpleName())
                        .build());
    }

}
