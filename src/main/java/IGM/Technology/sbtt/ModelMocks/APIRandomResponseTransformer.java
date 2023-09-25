package IGM.Technology.sbtt.ModelMocks;

import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.extension.ResponseTransformer;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.Response;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import java.util.Random;

public class APIRandomResponseTransformer extends ResponseTransformer {

    private final Random random = new Random();

    @Override
    public Response transform(Request request, Response response, FileSource files, Parameters parameters) {
        int randomValue = random.nextInt(100);

        final SimpleDateFormat TSPFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Timestamp timeElapsed = new Timestamp(System.currentTimeMillis() + 2000);

        if (randomValue < 40) { // 40% probability for a 200 response
            return Response.Builder.like(response)
                    .status(200)
                    .body("{\"metadata\": {\"request_time\": \"" + TSPFormat.format(timestamp) + "\", \"response_time\": \"" + TSPFormat.format(timeElapsed) + "\",\"api_version\": \"v1\"}, \"data\": {\"user\": \"John Doe\",\"username\" :\"jjdoes\",\"email\": \"jdoe@company.com\"}}")
                    .build();

        } else { // 60% probability for a 429 response
            return Response.Builder.like(response)
                    .status(429)
                    .body("{\"error\",: [{\"metadata\": {\"request_time\": \"" + TSPFormat.format(timestamp) + "\", \"response_time\": \""+ TSPFormat.format(timeElapsed) + "\", \"api_version\": \"v1\"}, \"status\": 429,\"message\": \"Too many requests\",\"errors\": [{\"detail\": \"Error: Failed to fetch data - Retryable exception occurred\"}]}]}")
                    .build();
        }
    }

    @Override
    public String getName() {
        return "APIRandom-response";
    }

    @Override
    public boolean applyGlobally() {
        return false;
    }
}

