package com.mightymerce.core.integration.facebook;

import com.mightymerce.core.domain.Article;
import com.mightymerce.core.integration.SocialPost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.FacebookLink;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * Created by agutheil on 26/06/15.
 */
@Component
@Transactional
public class LinkFacebookPost implements SocialPost {

    private final Logger log = LoggerFactory.getLogger(LinkFacebookPost.class);

    private final String applicationNamespace;

    @Inject
    public LinkFacebookPost(String applicationNamespace) {
        this.applicationNamespace = applicationNamespace;
    }

    @Override
    public String post(Article article, String accessToken) {
        String statusMessage = article.toString();
        log.info(statusMessage);
        Facebook facebook = new FacebookTemplate(accessToken, applicationNamespace);
        String link = "http://schubber.local:8081/checkout/"+article.getId();
        String name  = article.getName();
        String caption = article.getDescription();
        String description = article.toString();
        String picture = "https://upload.wikimedia.org/wikipedia/commons/d/d6/Schuh.jpg";
        FacebookLink facebookLink = new FacebookLink(link,name,caption,description, picture);
        return facebook.feedOperations().postLink(description, facebookLink);
    }
}
