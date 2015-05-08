package com.mightymerce.core.service.impl;

import com.mightymerce.core.domain.Article;
import com.mightymerce.core.domain.Channel;
import com.mightymerce.core.domain.ChannelPost;
import com.mightymerce.core.domain.CustomerChannel;
import com.mightymerce.core.integration.ChannelType;
import com.mightymerce.core.integration.SocialPoster;
import com.mightymerce.core.repository.ArticleRepository;
import com.mightymerce.core.repository.ChannelRepository;
import com.mightymerce.core.repository.CustomerChannelRepository;
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

    private final CustomerChannelRepository customerChannelRepository;

    private final ChannelRepository channelRepository;
    private final SocialPoster socialPoster;

    @Inject
    public ChannelServiceServiceImpl(ArticleRepository articleRepository, CustomerChannelRepository customerChannelRepository, ChannelRepository channelRepository, SocialPoster socialPoster) {
        this.articleRepository = articleRepository;
        this.customerChannelRepository = customerChannelRepository;
        this.channelRepository = channelRepository;
        this.socialPoster = socialPoster;
    }

    @Override
    public String updateStatus(ChannelPost channelPost) {
        Article article = articleRepository.getOne(channelPost.getArticle().getId());
        CustomerChannel customerChannel = customerChannelRepository.getOne(channelPost.getCustomerChannel().getId());
        String accessToken = customerChannel.getAccessToken();
        Channel channel = channelRepository.getOne(customerChannel.getChannel().getId());
        ChannelType channelType = ChannelType.valueOf(channel.getName().toUpperCase());
        String result = socialPoster.post(article, accessToken, channelType);
        return result;
    }
}
