package com.mightymerce.core.integration.checkout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mightymerce.core.domain.Address;
import com.mightymerce.core.domain.Article;
import com.mightymerce.core.domain.Customer;
import com.mightymerce.core.domain.SocialOrder;
import com.mightymerce.core.domain.User;
import com.mightymerce.core.domain.enumeration.OrderStatus;
import com.mightymerce.core.repository.AddressRepository;
import com.mightymerce.core.repository.ArticleRepository;
import com.mightymerce.core.repository.CustomerRepository;
import com.mightymerce.core.repository.SocialOrderRepository;
import com.mightymerce.core.repository.UserRepository;

@Component
public class CheckoutOrderToSocialOrderConverter {
	private final Logger log = LoggerFactory.getLogger(CheckoutOrderToSocialOrderConverter.class);
	
	@Autowired
	private ArticleRepository articleRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	SocialOrderRepository socialOrderRepository;
	
	@Autowired
	AddressRepository addressRepository;
	
	
	SocialOrder convertAndSave(CheckoutOrder checkoutOrder) {
        
        SocialOrder output = new SocialOrder();
        Article article = articleRepository.findOne(checkoutOrder.getCoreArticleId());
        if (article == null) {
        	throw new CheckoutOrderImportException("Article not found. Cannot import Order with txid "+checkoutOrder.getTransactionId());
        }
        User user = userRepository.findOne(article.getUser().getId());
        
        Customer customer = new Customer();
        customer.setLastName(checkoutOrder.getLastName());
        customer.setName(checkoutOrder.getFirstName());
        customer.setPayerId(checkoutOrder.getPayerId());
        customer.setUser(user);
        customer = customerRepository.save(customer);
        
        Address delivery = new Address();
        delivery.setAddressee(checkoutOrder.getShipToName());
        delivery.setStreetname(checkoutOrder.getShipToStreet());
        delivery.setHousenumber("");
        delivery.setTown(checkoutOrder.getShipToCity());
        delivery.setPostalCode(checkoutOrder.getShipToZip());
        delivery.setUser(user);
        delivery = addressRepository.save(delivery);
        
        output.setArticle(article);
        output.setPaymentStatus(checkoutOrder.getPaymentStatus());
        output.setOrderStatus(OrderStatus.created);
        output.setTransactionId(checkoutOrder.getTransactionId());
        output.setTotalAmount(checkoutOrder.getTotalAmt());      
        output.setUser(user);
        output.setDelivery(delivery);
        output.setCustomer(customer);
        output = socialOrderRepository.save(output);
        
        return output;
	}
}
