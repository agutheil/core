package com.mightymerce.core.service.impl;

import com.mightymerce.core.domain.Article;
import com.mightymerce.core.domain.ChannelPost;
import com.mightymerce.core.domain.MerchantChannel;
import com.mightymerce.core.domain.enumeration.Channel;
import com.mightymerce.core.integration.SocialPoster;
import com.mightymerce.core.repository.ArticleRepository;
import com.mightymerce.core.repository.MerchantChannelRepository;
import com.mightymerce.core.service.ChannelPostService;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ChannelPostServiceImpl implements ChannelPostService {

    private final Logger log = LoggerFactory.getLogger(ChannelPostServiceImpl.class);

    private final ArticleRepository articleRepository;

    private final MerchantChannelRepository merchantChannelRepository;

    private final SocialPoster socialPoster;

    @Inject
    public ChannelPostServiceImpl(ArticleRepository articleRepository, MerchantChannelRepository merchantChannelRepository, SocialPoster socialPoster) {
        this.articleRepository = articleRepository;
        this.merchantChannelRepository = merchantChannelRepository;
        this.socialPoster = socialPoster;
    }

    @Override
    public String updateStatus(ChannelPost channelPost) {
        Article article = articleRepository.getOne(channelPost.getArticle().getId());
        MerchantChannel merchantChannel = merchantChannelRepository.getOne(channelPost.getMerchantChannel().getId());
        String accessToken = merchantChannel.getAccessToken();
        Channel channel = merchantChannel.getChannel();
        String result = socialPoster.post(article, accessToken, channel);
        return result;
    }
}
