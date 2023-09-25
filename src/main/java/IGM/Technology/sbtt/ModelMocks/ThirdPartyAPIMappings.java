package IGM.Technology.sbtt.ModelMocks;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class ThirdPartyAPIMappings {

    public static void configureMappings() {
        stubFor(get(urlEqualTo("/api/fetch-some-data"))
                .willReturn(aResponse()
                        .withTransformer("APIRandom-response","","")
                        .withHeader("Content-Type", "application/json")));
    }

    public static void main(String[] args) {
        configureMappings();
    }

}


