package com.mightymerce.core.integration.impl;

import com.mightymerce.core.domain.Article;
import com.mightymerce.core.domain.enumeration.Channel;
import com.mightymerce.core.integration.SocialPoster;
import com.mightymerce.core.integration.SocialPost;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class DefaultSocialPoster implements SocialPoster{
    private final SocialPost facebookPost;
    private final SocialPost twitterPost;
    private final SocialPost pinterestPost;

    @Inject
    public DefaultSocialPoster(@Qualifier("linkFacebookPost")  SocialPost facebookPost, SocialPost twitterPost, SocialPost pinterestPost) {
        this.facebookPost = facebookPost;
        this.twitterPost = twitterPost;
        this.pinterestPost = pinterestPost;
    }

    @Override
    public String post(Article article, String accessToken, Channel channel) {
        String result;
        switch (channel) {
            case facebook:
                result = facebookPost.post(article, accessToken);
                break;
            case twitter:
                result = twitterPost.post(article, accessToken);
                break;
            case pinterest:
                result = pinterestPost.post(article, accessToken);
                break;
            default:
                result = "channel not found";
        }
        return result;
    }
}
