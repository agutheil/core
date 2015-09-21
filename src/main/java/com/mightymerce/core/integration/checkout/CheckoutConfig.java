package com.mightymerce.core.integration.checkout;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.beans.factory.annotation.Value;

/**
 * Created by agutheil on 11.05.15.
 */
@Configuration
public class CheckoutConfig {

	@Value("${mightymerce.checkoutUrl}")
	private String checkoutUrl;

    @Bean
    public OAuth2Template oAuth2Template(){
        OAuth2Template oAuth2Template = new OAuth2Template("checkoutapp","mySecretOAuthSecret",checkoutUrl+"/oauth/authorize", checkoutUrl+"/oauth/authenticate", checkoutUrl+"/oauth/token");
        oAuth2Template.setUseParametersForClientAuthentication(false);
        return oAuth2Template;
    }
}
