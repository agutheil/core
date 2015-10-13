package com.mightymerce.core.service.impl;

import com.mightymerce.core.domain.Product;
import com.mightymerce.core.domain.ChannelPost;
import com.mightymerce.core.domain.MerchantChannel;
import com.mightymerce.core.domain.enumeration.Channel;
import com.mightymerce.core.integration.SocialPoster;
import com.mightymerce.core.repository.ProductRepository;
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

    private final ProductRepository productRepository;

    private final MerchantChannelRepository merchantChannelRepository;

    private final SocialPoster socialPoster;

    @Inject
    public ChannelPostServiceImpl(ProductRepository productRepository, MerchantChannelRepository merchantChannelRepository, SocialPoster socialPoster) {
        this.productRepository = productRepository;
        this.merchantChannelRepository = merchantChannelRepository;
        this.socialPoster = socialPoster;
    }

    @Override
    public String updateStatus(ChannelPost channelPost) {
        Product product = productRepository.getOne(channelPost.getProduct().getId());
        MerchantChannel merchantChannel = merchantChannelRepository.getOne(channelPost.getMerchantChannel().getId());
        String accessToken = merchantChannel.getAccessToken();
        Channel channel = merchantChannel.getChannel();
        String result = socialPoster.post(product, accessToken, channel);
        return result;
    }
}
