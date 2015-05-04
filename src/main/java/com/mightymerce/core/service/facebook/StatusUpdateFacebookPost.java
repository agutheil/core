package com.mightymerce.core.service.facebook;

import com.mightymerce.core.domain.Article;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * Created by agutheil on 04.05.15.
 */
@Component
@Transactional
public class StatusUpdateFacebookPost implements FacebookPost{
    private final Logger log = LoggerFactory.getLogger(StatusUpdateFacebookPost.class);

    private final Facebook facebook;

    @Inject
    public StatusUpdateFacebookPost(Facebook facebook) {
        this.facebook = facebook;
    }

    @Override
    public String post(Article article) {
        String statusMessage = article.toString();
        log.info(statusMessage);
        return facebook.feedOperations().updateStatus(statusMessage);
    }
}
