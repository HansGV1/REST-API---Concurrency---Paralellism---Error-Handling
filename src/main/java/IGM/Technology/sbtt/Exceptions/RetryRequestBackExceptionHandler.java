package IGM.Technology.sbtt.Exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.retry.ExhaustedRetryException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Component
public class RetryRequestBackExceptionHandler extends Exception {

    public RetryRequestBackExceptionHandler(String message, Throwable cause) {
        super(message, cause);
    }
    @ExceptionHandler(CustomHttpClientErrorException.class)
    public ResponseEntity<String> handleRetryableException(CustomHttpClientErrorException ex) {
        return ResponseEntity.status(ex.getStatusCode()).body(ex.getMessage());
    }
}