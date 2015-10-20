package com.mightymerce.core.integration.checkout;

import com.mightymerce.core.domain.*;
import com.mightymerce.core.domain.enumeration.Currency;
import com.mightymerce.core.domain.enumeration.PaymentStatus;
import com.mightymerce.core.domain.enumeration.Tax;
import com.mightymerce.core.repository.*;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mightymerce.core.domain.enumeration.OrderStatus;

@Component
public class CheckoutOrderToSocialOrderConverter {
	private final Logger log = LoggerFactory.getLogger(CheckoutOrderToSocialOrderConverter.class);

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private CustomerOrderRepository customerOrderRepository;

	@Autowired
	private CustomerAddressRepository customerAddressRepository;


	CustomerOrder convertAndSave(CheckoutOrder checkoutOrder) {

        CustomerOrder customerOrder = new CustomerOrder();

        Product product = productRepository.findOne(checkoutOrder.getCoreArticleId());
        if (product == null) {
        	throw new CheckoutOrderImportException("Product not found. Cannot import Order with txid " + checkoutOrder.getTransactionId());
        }
        User user = userRepository.findOne(product.getUser().getId());

        Customer customer = new Customer();
        customer.setLastName(checkoutOrder.getLastName());
        customer.setFirstName(checkoutOrder.getFirstName());
        customer.setEmail(checkoutOrder.getEmail());
        customer.setUser(user);
        customer.setStatus(checkoutOrder.getPayerStatus());
        customer = customerRepository.save(customer);

        CustomerAddress address = new CustomerAddress();
        address.setAddressTo(checkoutOrder.getShipToName());
        address.setStreetAddress(checkoutOrder.getShipToStreet());
        address.setZip(checkoutOrder.getShipToZip());
        address.setCity(checkoutOrder.getShipToCity());
        address.setState(checkoutOrder.getShipToState());
        address.setCountry(checkoutOrder.getShipToCntryCode());
        address.setStatus(checkoutOrder.getAddressStatus());
        address.setUser(user);
        address = customerAddressRepository.save(address);
        customer.setShippingAddress(address);
        customer.setBillingAddress(address);

        customerOrder.setPaypalAccountId(checkoutOrder.getPayerId());
        customerOrder.setProduct(product);
        customerOrder.setPaymentStatus(PaymentStatus.valueOf(checkoutOrder.getPaymentStatus()));
        customerOrder.setOrderStatus(OrderStatus.Created);
        customerOrder.setPaypalTransactionId(checkoutOrder.getTransactionId());
        customerOrder.setTotalAmount(checkoutOrder.getTotalAmt().doubleValue());
        customerOrder.setCurrency(checkoutOrder.getCurrencyCode());
        customerOrder.setUser(user);
        customerOrder.setCustomer(customer);
        customerOrder.setPlacedOn(DateTime.now());
        customerOrder = customerOrderRepository.save(customerOrder);
        customerOrder.setTax(product.getTax());
        customerOrder.setTaxCurrency(customerOrder.getCurrency());

        return customerOrder;
	}
}
