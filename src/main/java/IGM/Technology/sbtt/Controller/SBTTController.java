package IGM.Technology.sbtt.Controller;

import IGM.Technology.sbtt.Exceptions.RetryRequestBackExceptionHandler;
import IGM.Technology.sbtt.Service.HTMLClientService;
import IGM.Technology.sbtt.Service.APIClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.ExhaustedRetryException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sbtt")
public class SBTTController {

    @Autowired
    private HTMLClientService HTMLService;

    private final APIClientService thirdPartyService;

    @Autowired
    public SBTTController(APIClientService thirdPartyService) {
        this.thirdPartyService = thirdPartyService;
    }

    @GetMapping("/site")
    public ResponseEntity<String> generateNewHtml() {
        try {
            ResponseEntity<String> htmlContentFile = HTMLService.retrieveHTML();

            HttpStatusCode statusCode = htmlContentFile.getStatusCode();
            String responseBody = htmlContentFile.getBody();

            System.out.println("Status Code: " + statusCode);
            System.out.println("Response Body: " + responseBody);

            return htmlContentFile;
        } catch (Exception e) {
            String errorMessage = "Failed to retrieve HTML. Please try again.";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }
    @GetMapping("/fetch-some-data")
    public ResponseEntity<String> fetchDataFromThirdPartyAPI() throws ExhaustedRetryException {
        try {
            ResponseEntity<String> apiResponse = thirdPartyService.fetchDataFromThirdPartyAPI();

            HttpStatusCode statusCode = apiResponse.getStatusCode();
            String responseBody = apiResponse.getBody();

            System.out.println("Status Code: " + statusCode);
            System.out.println("Response Body: " + responseBody);

            return apiResponse;

        } catch (RetryRequestBackExceptionHandler e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching data: " + e.getMessage());
        }
    }
}