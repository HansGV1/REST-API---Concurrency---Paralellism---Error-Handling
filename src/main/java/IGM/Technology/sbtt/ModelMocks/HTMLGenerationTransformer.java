package IGM.Technology.sbtt.ModelMocks;

import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.extension.ResponseTransformer;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.Response;
import lombok.Getter;
import lombok.Setter;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.nio.file.*;

public class HTMLGenerationTransformer extends ResponseTransformer {

    @Getter
    @Setter
    private long count;

    @Getter
    @Setter
    private long numberOfHTMLLinesPerPiece;

    @Override
    public Response transform(Request request, Response response, FileSource files, Parameters parameters) {
        try {
            return Response.Builder.like(response)
                    .status(200)
                    .body(generateHTML())
                    .build();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String generateHTML() throws ExecutionException, InterruptedException {
        // Set number of availableCores
        int availableCores = Runtime.getRuntime().availableProcessors();
        // Define the number of tasks based on CPU cores
        int numTasks = availableCores;

        String filePath = "src/main/resources/templates/about.html";

        try {
            // make a connection to the file
            Path file = Paths.get(filePath);
            // read all lines of the file
            setCount(Files.lines(file).count());
        } catch (Exception e) {
            e.getStackTrace();
        }

        setNumberOfHTMLLinesPerPiece(getCount() / numTasks);

        ExecutorService executor = Executors.newScheduledThreadPool(availableCores);

        // Create a list of CompletableFuture tasks for parallel generation
        List<CompletableFuture<String>> tasks = IntStream.range(1, numTasks + 1)
                .mapToObj(i -> CompletableFuture.supplyAsync(() -> generateHTMLPart(i), executor))
                .collect(Collectors.toList());

        // Wait for all tasks to complete and combine the HTML parts
        CompletableFuture<Void> allOf = CompletableFuture.allOf(tasks.toArray(new CompletableFuture[0]));
        allOf.join();

        executor.shutdown();

        String generatedHTML = tasks.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.joining());
        return generatedHTML;
    }

    private String generateHTMLPart(int taskNumber) {

        long startTime = System.currentTimeMillis();

        try {
            Thread.sleep(1000); // Simulate delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Read a portion of the existing HTML file
        Resource resource = new ClassPathResource("templates/about.html");
        StringBuilder htmlPart = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String line;
            int lineCount = 1;
            while ((line = reader.readLine()) != null) {
                if (lineCount >= taskNumber * numberOfHTMLLinesPerPiece - numberOfHTMLLinesPerPiece + 1 && lineCount <= taskNumber * numberOfHTMLLinesPerPiece) {
                    if (lineCount == taskNumber * numberOfHTMLLinesPerPiece) {
                        String end = "<p><b>Here ends the generated hardcoded piece of HTML file for Task # " + Integer.toString(taskNumber) + " </b></p>\n";
                        htmlPart.append(line).append("\n");
                        htmlPart.append(end).append("\n");
                        break;
                    } else if (lineCount == taskNumber * numberOfHTMLLinesPerPiece - numberOfHTMLLinesPerPiece + 1) {
                        String start = "<p><b>Here starts the generated hardcoded piece of HTML file for Task # " + Integer.toString(taskNumber) + " </b></p>\n";
                        htmlPart.append(start).append("\n");
                        htmlPart.append(line).append("\n");
                    } else {
                        htmlPart.append(line).append("\n");
                    }
                }
                lineCount++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        System.out.println("Task " + taskNumber + " execution time: " + executionTime + "ms");

        return htmlPart.toString();
    }

    @Override
    public String getName() {
        return "HTMLRandom-response";
    }

    @Override
    public boolean applyGlobally() {
        return false;
    }
}
