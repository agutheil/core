package com.mightymerce.core.integration.facebook;

import com.mightymerce.core.domain.Product;
import com.mightymerce.core.integration.SocialPost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.FacebookLink;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;

import javax.inject.Inject;

/**
 * Created by agutheil on 26/06/15.
 */
@Component
@Transactional
public class LinkFacebookPost implements SocialPost {

    private final Logger log = LoggerFactory.getLogger(LinkFacebookPost.class);

    private final String applicationNamespace;

    @Value("${mightymerce.checkoutLink}")
    private String checkoutLink;

    @Inject
    public LinkFacebookPost(String applicationNamespace) {
        this.applicationNamespace = applicationNamespace;
    }

    @Override
    public String post(Product product, String accessToken) {
        log.info("CheckoutLink: "+checkoutLink);
        String statusMessage = product.toString();
        log.debug(statusMessage);
        Facebook facebook = new FacebookTemplate(accessToken, applicationNamespace);
        String link = checkoutLink+product.getId();
        String name  = product.getTitle();
        String caption = product.getDescription();
        String description = product.getPrice() + " " + product.getCurrency();
        FacebookLink facebookLink = new FacebookLink(link,name,caption,description);
        return facebook.feedOperations().postLink("", facebookLink);
    }
}
