package com.mightymerce.core.integration.checkout;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mightymerce.core.domain.SocialOrder;
import com.mightymerce.core.repository.SocialOrderRepository;

@Component
public class CheckoutOrderImporter {
	private final Logger log = LoggerFactory.getLogger(CheckoutOrderImporter.class);
	
	@Autowired
	CheckoutOrderToSocialOrderConverter converter;
	
	@Autowired
	CheckoutOrderRepository checkoutOrderRepository;
	
	@Autowired
	SocialOrderRepository socialOrderRepository;
	
	@Scheduled(fixedRate = 5000)
	public void importAndSave() {
		log.info("Importing orders ...");
		List<CheckoutOrder> orders = checkoutOrderRepository.findAllCheckoutOrders();
		SocialOrder socialOrder;
		for (CheckoutOrder checkoutOrder : orders) {
			log.info("checking id order for txid "+checkoutOrder.getTransactionId()+ " exists");
			List<SocialOrder> txorders = socialOrderRepository.findByTransactionID(checkoutOrder.getTransactionId());
			if (null == txorders || txorders.isEmpty()) {
				log.info("Saving order ...");
				socialOrder = converter.convertAndSave(checkoutOrder);
				log.info("Order saved "+socialOrder);
			} else {
				log.info("Order already exists");
			}
		}
		
	}
}
