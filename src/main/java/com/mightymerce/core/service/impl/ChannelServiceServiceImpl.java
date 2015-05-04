package com.mightymerce.core.service.impl;

import com.mightymerce.core.domain.Article;
import com.mightymerce.core.domain.ChannelPost;
import com.mightymerce.core.repository.ArticleRepository;
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

    @Inject
    public ChannelServiceServiceImpl(@Qualifier("statusUpdateFacebookPost") FacebookPost facebookPost, ArticleRepository articleRepository) {
        this.facebookPost = facebookPost;
        this.articleRepository = articleRepository;
    }

    @Override
    public String updateStatus(ChannelPost channelPost) {
        Article article = articleRepository.getOne(channelPost.getArticle().getId());
        return facebookPost.post(article);
    }
}
