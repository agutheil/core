package com.mightymerce.core.integration.checkout;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.oauth2.TokenStrategy;

/**
 * Created by agutheil on 11.05.15.
 */
public class MightyCheckout extends AbstractOAuth2ApiBinding {
    private String coreUrl;

    protected MightyCheckout() {
        super();
    }

    protected MightyCheckout(String accessToken) {
        super(accessToken);
    }

    protected MightyCheckout(String accessToken, TokenStrategy tokenStrategy) {
        super(accessToken, tokenStrategy);
    }

    protected MightyCheckout(String accessToken, TokenStrategy tokenStrategy, String coreUrl) {
        super(accessToken, tokenStrategy);
        this.coreUrl = coreUrl;
    }

    public CheckoutOrder getOrder(String orderId) {
    	CheckoutOrder checkoutOrder = getRestTemplate().getForObject(coreUrl+"/api/orders/"+orderId, CheckoutOrder.class);
        return checkoutOrder;
    }
    public List<CheckoutOrder> getArticles() {
    	ResponseEntity<CheckoutOrder[]> responseEntity = getRestTemplate().getForEntity(coreUrl+"/api/orders", CheckoutOrder[].class);
    	CheckoutOrder[] articles = responseEntity.getBody();
    	return Arrays.asList(articles);
    }
}
