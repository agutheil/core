package com.mightymerce.core.web.rest;

import com.mightymerce.core.Application;
import com.mightymerce.core.domain.SocialOrder;
import com.mightymerce.core.repository.SocialOrderRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mightymerce.core.domain.enumeration.DeliveryStatus;
import com.mightymerce.core.domain.enumeration.OrderStatus;

/**
 * Test class for the SocialOrderResource REST controller.
 *
 * @see SocialOrderResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class SocialOrderResourceTest {

    private static final String DEFAULT_TRANSACTION_ID = "SAMPLE_TEXT";
    private static final String UPDATED_TRANSACTION_ID = "UPDATED_TEXT";

    private static final BigDecimal DEFAULT_TOTAL_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_AMOUNT = new BigDecimal(2);
    private static final String DEFAULT_PAYMENT_STATUS = "SAMPLE_TEXT";
    private static final String UPDATED_PAYMENT_STATUS = "UPDATED_TEXT";

    private static final DeliveryStatus DEFAULT_DELIVERY_STATUS = DeliveryStatus.sent;
    private static final DeliveryStatus UPDATED_DELIVERY_STATUS = DeliveryStatus.delivered;

    private static final OrderStatus DEFAULT_ORDER_STATUS = OrderStatus.created;
    private static final OrderStatus UPDATED_ORDER_STATUS = OrderStatus.inProgress;

    @Inject
    private SocialOrderRepository socialOrderRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restSocialOrderMockMvc;

    private SocialOrder socialOrder;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SocialOrderResource socialOrderResource = new SocialOrderResource();
        ReflectionTestUtils.setField(socialOrderResource, "socialOrderRepository", socialOrderRepository);
        this.restSocialOrderMockMvc = MockMvcBuilders.standaloneSetup(socialOrderResource).setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        socialOrder = new SocialOrder();
        socialOrder.setTransactionId(DEFAULT_TRANSACTION_ID);
        socialOrder.setTotalAmount(DEFAULT_TOTAL_AMOUNT);
        socialOrder.setPaymentStatus(DEFAULT_PAYMENT_STATUS);
        socialOrder.setDeliveryStatus(DEFAULT_DELIVERY_STATUS);
        socialOrder.setOrderStatus(DEFAULT_ORDER_STATUS);
    }

    @Test
    @Transactional
    public void createSocialOrder() throws Exception {
        int databaseSizeBeforeCreate = socialOrderRepository.findAll().size();

        // Create the SocialOrder

        restSocialOrderMockMvc.perform(post("/api/socialOrders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(socialOrder)))
                .andExpect(status().isCreated());

        // Validate the SocialOrder in the database
        List<SocialOrder> socialOrders = socialOrderRepository.findAll();
        assertThat(socialOrders).hasSize(databaseSizeBeforeCreate + 1);
        SocialOrder testSocialOrder = socialOrders.get(socialOrders.size() - 1);
        assertThat(testSocialOrder.getTransactionId()).isEqualTo(DEFAULT_TRANSACTION_ID);
        assertThat(testSocialOrder.getTotalAmount()).isEqualTo(DEFAULT_TOTAL_AMOUNT);
        assertThat(testSocialOrder.getPaymentStatus()).isEqualTo(DEFAULT_PAYMENT_STATUS);
        assertThat(testSocialOrder.getDeliveryStatus()).isEqualTo(DEFAULT_DELIVERY_STATUS);
        assertThat(testSocialOrder.getOrderStatus()).isEqualTo(DEFAULT_ORDER_STATUS);
    }

    @Test
    @Transactional
    public void getAllSocialOrders() throws Exception {
        // Initialize the database
        socialOrderRepository.saveAndFlush(socialOrder);

        // Get all the socialOrders
        restSocialOrderMockMvc.perform(get("/api/socialOrders"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(socialOrder.getId().intValue())))
                .andExpect(jsonPath("$.[*].transactionId").value(hasItem(DEFAULT_TRANSACTION_ID.toString())))
                .andExpect(jsonPath("$.[*].totalAmount").value(hasItem(DEFAULT_TOTAL_AMOUNT.intValue())))
                .andExpect(jsonPath("$.[*].paymentStatus").value(hasItem(DEFAULT_PAYMENT_STATUS.toString())))
                .andExpect(jsonPath("$.[*].deliveryStatus").value(hasItem(DEFAULT_DELIVERY_STATUS.toString())))
                .andExpect(jsonPath("$.[*].orderStatus").value(hasItem(DEFAULT_ORDER_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getSocialOrder() throws Exception {
        // Initialize the database
        socialOrderRepository.saveAndFlush(socialOrder);

        // Get the socialOrder
        restSocialOrderMockMvc.perform(get("/api/socialOrders/{id}", socialOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(socialOrder.getId().intValue()))
            .andExpect(jsonPath("$.transactionId").value(DEFAULT_TRANSACTION_ID.toString()))
            .andExpect(jsonPath("$.totalAmount").value(DEFAULT_TOTAL_AMOUNT.intValue()))
            .andExpect(jsonPath("$.paymentStatus").value(DEFAULT_PAYMENT_STATUS.toString()))
            .andExpect(jsonPath("$.deliveryStatus").value(DEFAULT_DELIVERY_STATUS.toString()))
            .andExpect(jsonPath("$.orderStatus").value(DEFAULT_ORDER_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSocialOrder() throws Exception {
        // Get the socialOrder
        restSocialOrderMockMvc.perform(get("/api/socialOrders/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSocialOrder() throws Exception {
        // Initialize the database
        socialOrderRepository.saveAndFlush(socialOrder);

		int databaseSizeBeforeUpdate = socialOrderRepository.findAll().size();

        // Update the socialOrder
        socialOrder.setTransactionId(UPDATED_TRANSACTION_ID);
        socialOrder.setTotalAmount(UPDATED_TOTAL_AMOUNT);
        socialOrder.setPaymentStatus(UPDATED_PAYMENT_STATUS);
        socialOrder.setDeliveryStatus(UPDATED_DELIVERY_STATUS);
        socialOrder.setOrderStatus(UPDATED_ORDER_STATUS);
        

        restSocialOrderMockMvc.perform(put("/api/socialOrders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(socialOrder)))
                .andExpect(status().isOk());

        // Validate the SocialOrder in the database
        List<SocialOrder> socialOrders = socialOrderRepository.findAll();
        assertThat(socialOrders).hasSize(databaseSizeBeforeUpdate);
        SocialOrder testSocialOrder = socialOrders.get(socialOrders.size() - 1);
        assertThat(testSocialOrder.getTransactionId()).isEqualTo(UPDATED_TRANSACTION_ID);
        assertThat(testSocialOrder.getTotalAmount()).isEqualTo(UPDATED_TOTAL_AMOUNT);
        assertThat(testSocialOrder.getPaymentStatus()).isEqualTo(UPDATED_PAYMENT_STATUS);
        assertThat(testSocialOrder.getDeliveryStatus()).isEqualTo(UPDATED_DELIVERY_STATUS);
        assertThat(testSocialOrder.getOrderStatus()).isEqualTo(UPDATED_ORDER_STATUS);
    }

    @Test
    @Transactional
    public void deleteSocialOrder() throws Exception {
        // Initialize the database
        socialOrderRepository.saveAndFlush(socialOrder);

		int databaseSizeBeforeDelete = socialOrderRepository.findAll().size();

        // Get the socialOrder
        restSocialOrderMockMvc.perform(delete("/api/socialOrders/{id}", socialOrder.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<SocialOrder> socialOrders = socialOrderRepository.findAll();
        assertThat(socialOrders).hasSize(databaseSizeBeforeDelete - 1);
    }
}
