package com.mightymerce.core.integration;

import com.mightymerce.core.domain.Article;

/**
 * Created by agutheil on 06.05.15.
 */
public interface SocialPoster {
    public String post(Article article, String accessToken, ChannelType channelType);
}
