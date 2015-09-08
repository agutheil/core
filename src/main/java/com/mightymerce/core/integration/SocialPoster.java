package com.mightymerce.core.integration;

import com.mightymerce.core.domain.Article;
import com.mightymerce.core.domain.enumeration.Channel;

/**
 * Created by agutheil on 06.05.15.
 */
public interface SocialPoster {
    public String post(Article article, String accessToken, Channel channel);
}
