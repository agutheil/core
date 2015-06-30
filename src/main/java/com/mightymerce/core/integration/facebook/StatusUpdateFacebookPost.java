package com.mightymerce.core.integration.facebook;

import com.mightymerce.core.domain.Article;
import com.mightymerce.core.integration.SocialPost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * Created by agutheil on 04.05.15.
 */
@Component
@Transactional
public class StatusUpdateFacebookPost implements SocialPost {
    private final Logger log = LoggerFactory.getLogger(StatusUpdateFacebookPost.class);

    private final String applicationNamespace;

    @Inject
    public StatusUpdateFacebookPost(String applicationNamespace) {
        this.applicationNamespace = applicationNamespace;
    }

    @Override
    public String post(Article article, String accessToken) {
        String statusMessage = article.toString();
        log.info(statusMessage);
        Facebook facebook = new FacebookTemplate(accessToken, applicationNamespace);
        return facebook.feedOperations().updateStatus(statusMessage);
    }
}
