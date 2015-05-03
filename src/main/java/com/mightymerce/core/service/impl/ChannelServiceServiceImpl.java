package com.mightymerce.core.service.impl;

import com.mightymerce.core.service.ChannelServiceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public void updateStatus(String statusMessage) {
        facebook.feedOperations().updateStatus(statusMessage);
    }
}
