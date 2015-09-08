package com.mightymerce.core.service.impl;

import com.mightymerce.core.service.ChannelPostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ChannelPostServiceImpl implements ChannelPostService {

    private final Logger log = LoggerFactory.getLogger(ChannelPostServiceImpl.class);

}
