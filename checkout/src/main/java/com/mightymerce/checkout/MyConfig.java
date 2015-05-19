package com.mightymerce.checkout;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.oauth2.OAuth2Template;

/**
 * Created by agutheil on 11.05.15.
 */
@Configuration
public class MyConfig {
    @Bean
    public OAuth2Template oAuth2Template(){
        OAuth2Template oAuth2Template = new OAuth2Template("mightymerceapp","mySecretOAuthSecret","http://localhost:8080/oauth/authorize", "http://localhost:8080/oauth/authenticate", "http://localhost:8080/oauth/token");
        oAuth2Template.setUseParametersForClientAuthentication(false);
        return oAuth2Template;
    }
}
