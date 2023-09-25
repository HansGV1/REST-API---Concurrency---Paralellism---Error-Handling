package IGM.Technology.sbtt.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

@ControllerAdvice
@Component
public class CustomHttpClientErrorException extends HttpClientErrorException {
    public CustomHttpClientErrorException(HttpStatus statusCode, String statusText) {
        super(statusCode, statusText);
    }

}