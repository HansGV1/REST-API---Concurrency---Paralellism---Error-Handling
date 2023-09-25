package IGM.Technology.sbtt.ModelMocks;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

public class HTMLServerInitializer {

    private static WireMockServer wireMockServer;

    public static void start() {
        WireMockConfiguration wireMockConfig = WireMockConfiguration.options()
                .port(8081)
                .extensions(new HTMLGenerationTransformer());
        wireMockServer = new WireMockServer(wireMockConfig);
        wireMockServer.start();

    }

    public static void stop() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }

    public static void main(String[] args) {
        start();
    }
}