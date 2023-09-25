package IGM.Technology.sbtt.Service;

import IGM.Technology.sbtt.Exceptions.RetryRequestBackExceptionHandler;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.http.ResponseEntity;
import org.springframework.retry.ExhaustedRetryException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HTMLClientService {

    private final RestTemplate restTemplate;

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(APIClientService.class);

    @Autowired
    public HTMLClientService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public ResponseEntity<String> retrieveHTML() throws Exception {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:8081/html/generate", String.class);
            System.out.println("Successful request");
            return response;

        } catch (Exception ex) {
            throw new Exception("Failed to retrieve HTML", ex);
        }
    }

}