package com.mightymerce.core.integration.twitter;

import com.mightymerce.core.domain.Article;
import com.mightymerce.core.integration.SocialPost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by agutheil on 06.05.15.
 */
@Component
public class TwitterPost implements SocialPost {
    private final Logger log = LoggerFactory.getLogger(TwitterPost.class);
    @Override
    public String post(Article article, String accessToken) {
        log.info(article.toString());
        return "twitter dummy";
    }
}
