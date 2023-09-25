package IGM.Technology.sbtt.ModelMocks;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class HTMLServerMappings {

    public static void configureMappings() {
        stubFor(get(urlEqualTo("/html/generate"))
                .willReturn(aResponse()
                        .withTransformer("HTMLRandom-response","","")
                        .withHeader("Content-Type", "text/html")));
    }

    public static void main(String[] args) {
        configureMappings();
    }

}


