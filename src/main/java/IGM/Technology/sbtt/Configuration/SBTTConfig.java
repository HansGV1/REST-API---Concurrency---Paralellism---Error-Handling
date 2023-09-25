package IGM.Technology.sbtt.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.HttpStatus;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableRetry(proxyTargetClass=true)
@EnableAspectJAutoProxy
public class SBTTConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public String string() {
        return "";
    }

    @Bean
    public HttpStatus httpStatusBean() {
        return HttpStatus.OK;
    }

}
