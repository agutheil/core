package com.mightymerce.core.integration.checkout;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CheckoutOrderRepository {
	@Autowired
	private RestTemplate checkoutOrderRestTemplate;

	List<CheckoutOrder> findAllCheckoutOrders() {
		int port = 8081;
		String url = "http://localhost:{port}/order?page={page}&size={size}";

		ResponseEntity<PagedResources<CheckoutOrder>> responseEntity = checkoutOrderRestTemplate.exchange(url, HttpMethod.GET, null,
				new ParameterizedTypeReference<PagedResources<CheckoutOrder>>() {
				}, port, 0, 100);
		PagedResources<CheckoutOrder> resources = responseEntity.getBody();
		List<CheckoutOrder> orders = new ArrayList(resources.getContent());
		System.out.println(orders);
		return orders;
	}

	CheckoutOrder findCheckoutOrderById(String id) {
		CheckoutOrder checkoutOrder = checkoutOrderRestTemplate.getForObject("http://localhost:8081/order/" + id,
				CheckoutOrder.class);
		return checkoutOrder;
	}

	public void setRestTemplate(RestTemplate restTemplate) {
		this.checkoutOrderRestTemplate = restTemplate;
	}
}
