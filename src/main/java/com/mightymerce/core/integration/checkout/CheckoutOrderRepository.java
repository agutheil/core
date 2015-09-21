package com.mightymerce.core.integration.checkout;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.social.oauth2.TokenStrategy;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;


@Component
public class CheckoutOrderRepository {

	@Autowired
	private OAuth2Template oAuth2Template;

	private MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();

    @Value("${mightymerce.checkoutUrl}")
    private String checkoutUrl;
    
    @Value("${mightymerce.checkoutUser}")
    private String checkoutUser;
    
    @Value("${mightymerce.checkoutPassword}")
    private String checkoutPassword;


	List<CheckoutOrder> findAllCheckoutOrders() {
		AccessGrant ag = oAuth2Template.exchangeCredentialsForAccess(checkoutUser, checkoutPassword, params);
        MightyCheckout mightyCore = new MightyCheckout(ag.getAccessToken(), TokenStrategy.AUTHORIZATION_HEADER, checkoutUrl);
        List<CheckoutOrder> checkoutOrders = mightyCore.getArticles();
        return checkoutOrders;
	}
	
}
