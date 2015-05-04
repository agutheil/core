package com.mightymerce.core.service.impl;

import com.mightymerce.core.domain.Article;
import com.mightymerce.core.domain.Channel;
import com.mightymerce.core.domain.ChannelPost;
import com.mightymerce.core.domain.CustomerChannel;
import com.mightymerce.core.repository.ArticleRepository;
import com.mightymerce.core.repository.ChannelRepository;
import com.mightymerce.core.repository.CustomerChannelRepository;
import com.mightymerce.core.service.ChannelServiceService;
import com.mightymerce.core.service.facebook.FacebookPost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

@Service
@Transactional
public class ChannelServiceServiceImpl implements ChannelServiceService {

    private final Logger log = LoggerFactory.getLogger(ChannelServiceServiceImpl.class);

    private final FacebookPost facebookPost;

    private final ArticleRepository articleRepository;

    private final CustomerChannelRepository customerChannelRepository;

    private final ChannelRepository channelRepository;

    @Inject
    public ChannelServiceServiceImpl(@Qualifier("statusUpdateFacebookPost") FacebookPost facebookPost, ArticleRepository articleRepository, CustomerChannelRepository customerChannelRepository, ChannelRepository channelRepository) {
        this.facebookPost = facebookPost;
        this.articleRepository = articleRepository;
        this.customerChannelRepository = customerChannelRepository;
        this.channelRepository = channelRepository;
    }

    @Override
    public String updateStatus(ChannelPost channelPost) {
        Article article = articleRepository.getOne(channelPost.getArticle().getId());
        //finde Token
        CustomerChannel customerChannel = customerChannelRepository.getOne(channelPost.getCustomerChannel().getId());
        String accessToken = customerChannel.getKey();
        //welcher Kanal? Facebook, Twitter, ...
        Channel channel = channelRepository.getOne(customerChannel.getChannel().getId());
        return facebookPost.post(article, accessToken);
    }
}
