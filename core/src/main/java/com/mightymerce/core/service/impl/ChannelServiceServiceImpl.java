package com.mightymerce.core.service.impl;

import com.mightymerce.core.domain.Article;
import com.mightymerce.core.domain.Channel;
import com.mightymerce.core.domain.ChannelPost;
import com.mightymerce.core.domain.MerchantChannel;
import com.mightymerce.core.integration.ChannelType;
import com.mightymerce.core.integration.SocialPoster;
import com.mightymerce.core.repository.ArticleRepository;
import com.mightymerce.core.repository.ChannelRepository;
import com.mightymerce.core.repository.MerchantChannelRepository;
import com.mightymerce.core.service.ChannelServiceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

@Service
@Transactional
public class ChannelServiceServiceImpl implements ChannelServiceService {

    private final Logger log = LoggerFactory.getLogger(ChannelServiceServiceImpl.class);

    private final ArticleRepository articleRepository;

    private final MerchantChannelRepository merchantChannelRepository;

    private final ChannelRepository channelRepository;
    private final SocialPoster socialPoster;

    @Inject
    public ChannelServiceServiceImpl(ArticleRepository articleRepository, MerchantChannelRepository merchantChannelRepository, ChannelRepository channelRepository, SocialPoster socialPoster) {
        this.articleRepository = articleRepository;
        this.merchantChannelRepository = merchantChannelRepository;
        this.channelRepository = channelRepository;
        this.socialPoster = socialPoster;
    }

    @Override
    public String updateStatus(ChannelPost channelPost) {
        Article article = articleRepository.getOne(channelPost.getArticle().getId());
        MerchantChannel merchantChannel = merchantChannelRepository.getOne(channelPost.getMerchantChannel().getId());
        String accessToken = merchantChannel.getAccessToken();
        Channel channel = channelRepository.getOne(merchantChannel.getChannel().getId());
        ChannelType channelType = ChannelType.valueOf(channel.getName().toUpperCase());
        String result = socialPoster.post(article, accessToken, channelType);
        return result;
    }
}
