package IGM.Technology.sbtt;

import IGM.Technology.sbtt.ModelMocks.HTMLServerInitializer;
import IGM.Technology.sbtt.ModelMocks.HTMLServerMappings;
import IGM.Technology.sbtt.ModelMocks.ThirdPartyAPIInitializer;
import IGM.Technology.sbtt.ModelMocks.ThirdPartyAPIMappings;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;

@SpringBootApplication
@ComponentScan(basePackages="IGM.Technology.sbtt")
public class SbttApplication {

	public static void main(String[] args) {

		SpringApplication.run(SbttApplication.class, args);

		HTMLServerInitializer.start();
		configureFor("localhost", 8081);
		HTMLServerMappings.configureMappings();

		ThirdPartyAPIInitializer.start();
		configureFor("localhost", 8082);
		ThirdPartyAPIMappings.configureMappings();

	}
}