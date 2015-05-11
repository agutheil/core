package hello;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by agutheil on 11.05.15.
 */
@Configuration
public class MyConfig {
    @Bean
    public MightyCore restTemplate(){
        MightyCore mightyCore = new MightyCore("mightymerceapp","mySecretOAuthSecret","http://localhost:8080/oauth/authorize", "http://localhost:8080/oauth/authenticate", "http://localhost:8080/oauth/token");
        mightyCore.setUseParametersForClientAuthentication(false);
        return mightyCore;
    }
}
