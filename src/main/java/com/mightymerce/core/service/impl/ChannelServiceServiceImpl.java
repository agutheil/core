package com.mightymerce.core.service.impl;

import com.mightymerce.core.domain.ChannelPost;
import com.mightymerce.core.service.ChannelServiceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;

import javax.inject.Inject;

@Service
@Transactional
public class ChannelServiceServiceImpl implements ChannelServiceService {

    private final Logger log = LoggerFactory.getLogger(ChannelServiceServiceImpl.class);

    private final Facebook facebook;

    @Inject
    public ChannelServiceServiceImpl(Facebook facebook) {
        this.facebook = facebook;
    }

    @Override
    public void updateStatus(ChannelPost channelPost) {
        postProduct();
        //postStatusUpdate(channelPost);
    }

    private void postStatusUpdate(ChannelPost channelPost) {
        String statusMessage = channelPost.getArticle().toString();
        facebook.feedOperations().updateStatus(statusMessage);
    }

    private void postProduct() {
        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
        System.out.println("---- Map created-----> ");
        map.set("fb:app_id", "404823203038294"); // The offer's ID.
        map.set("claim_limit", "5"); // The maximum number of times the offer can be claimed.
        map.set("coupon_type", "clothing"); // The type of offer.
        // map.set("created_time", "xx.xx.xxxx"); // The time the offer was created.
        // map.set("expiration_time", "xx.xx.xxxx"); // The expiration time of the offer (for display purposes).
        //map.set("from", Page); // The page that published the offer. Type = Page
        map.set("og:image", "http://www.imageRepo.com/resources/test.png"); // the image on the left
        map.set("redemption_code", "3x2511"); // A code to to receive the discount or promotion.
        //map.set("redemption_link", ""); // The URL where the offer may be redeemed.
        map.set("terms", "bla bla bla bla"); // The terms of the offer.
        map.set("og:title", "Best offer ever on mightymerce"); // The title of the offer.
        map.set("og:message", "If you don't take this you might be ..."); // The description text of the offer.
        map.set("og:type", "product"); // xxx
        System.out.println("---- Map filled -----> ");
// DOES NOT WORK YET!!!!!
        facebook.openGraphOperations().publishAction("/objects/Product", map, false);
        System.out.println("---- Graph posted -----> ");
    }
}
