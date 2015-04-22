package com.mightymerce.core.service.impl;

import com.mightymerce.core.service.OrderQueueServiceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderQueueServiceServiceImpl implements OrderQueueServiceService {

    private final Logger log = LoggerFactory.getLogger(OrderQueueServiceServiceImpl.class);

}
