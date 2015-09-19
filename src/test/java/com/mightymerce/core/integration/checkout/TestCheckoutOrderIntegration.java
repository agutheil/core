package com.mightymerce.core.integration.checkout;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestCheckoutOrderIntegration {

	CheckoutOrderRepository checkoutOrderRepository;

	@Before
	public void setUp(){
		checkoutOrderRepository = new CheckoutOrderRepository();
		checkoutOrderRepository.setRestTemplate(restTemplate());
	}

	@Test
	public void testFindAllCheckoutOrders() {
		List<CheckoutOrder> orders = checkoutOrderRepository.findAllCheckoutOrders();
		assertNotNull(orders);
	}

	@Test
	public void testFindCheckoutOrderById() {
		CheckoutOrder order = checkoutOrderRepository.findCheckoutOrderById("55fdd3bcd4c6384923698c5e");
		assertEquals(Long.valueOf(1L), order.getArticle());
	}

	private RestTemplate restTemplate() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.registerModule(new Jackson2HalModule());

		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setSupportedMediaTypes(MediaType.parseMediaTypes("application/hal+json"));
		converter.setObjectMapper(mapper);
		return new RestTemplate(Arrays.asList(converter));
	}

}
