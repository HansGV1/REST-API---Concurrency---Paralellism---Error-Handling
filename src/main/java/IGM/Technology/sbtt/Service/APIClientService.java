package IGM.Technology.sbtt.Service;

import IGM.Technology.sbtt.CustomAnnotations.RetryRequestBack;
import IGM.Technology.sbtt.Exceptions.RetryRequestBackExceptionHandler;
import com.github.tomakehurst.wiremock.http.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.ExhaustedRetryException;
import org.springframework.retry.annotation.Recover;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@Service
public class APIClientService {

    private final RestTemplate restTemplate;
    private static final Logger logger = LoggerFactory.getLogger(APIClientService.class);

    @Autowired
    public APIClientService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @RetryRequestBack
    public ResponseEntity<String> fetchDataFromThirdPartyAPI() throws RetryRequestBackExceptionHandler, ExhaustedRetryException {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:8082/api/fetch-some-data", String.class);
            System.out.println("Successful request");
            return response;

        } catch (Exception ex) {
            logger.error("Retryable exception occurred: {}", ex.getMessage());
            System.out.println("Retry attempt being executed");
            throw new ExhaustedRetryException("Failed to fetch data from API", ex);
        }
    }

    @Recover
    public void helpRecover(ExhaustedRetryException ex)  {

    }

    @Recover
    public ResponseEntity<String> helpRecover(Exception ex){

        final SimpleDateFormat TSPFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Timestamp timeElapsed = new Timestamp(System.currentTimeMillis() + 2000);

        return ResponseEntity
                .status(HttpStatus.TOO_MANY_REQUESTS)
                .header("Content-Type", "application/json")
                .body("{\"error\": [{\"metadata\": {\"request_time\": \"" + TSPFormat.format(timestamp) + "\", \"response_time\": \""+ TSPFormat.format(timeElapsed) + "\", \"api_version\": \"v1\"}, \"status\": 429,\"message\": \"Too many requests\",\"errors\": [{\"detail\": \"Number of retry attempts has been exhausted - " + ex.getMessage()+ "\"}]}]}");
    }
}