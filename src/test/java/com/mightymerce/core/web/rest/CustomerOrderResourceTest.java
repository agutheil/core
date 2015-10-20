package com.mightymerce.core.web.rest;

import com.mightymerce.core.Application;
import com.mightymerce.core.domain.CustomerOrder;
import com.mightymerce.core.repository.CustomerOrderRepository;
import com.mightymerce.core.web.rest.dto.CustomerOrderDTO;
import com.mightymerce.core.web.rest.mapper.CustomerOrderMapper;

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
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mightymerce.core.domain.enumeration.OrderStatus;
import com.mightymerce.core.domain.enumeration.PaymentStatus;
import com.mightymerce.core.domain.enumeration.Tax;
import com.mightymerce.core.domain.enumeration.Currency;
import com.mightymerce.core.domain.enumeration.Currency;

/**
 * Test class for the CustomerOrderResource REST controller.
 *
 * @see CustomerOrderResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class CustomerOrderResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");


    private static final DateTime DEFAULT_PLACED_ON = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_PLACED_ON = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_PLACED_ON_STR = dateTimeFormatter.print(DEFAULT_PLACED_ON);

    private static final OrderStatus DEFAULT_ORDER_STATUS = OrderStatus.Created;
    private static final OrderStatus UPDATED_ORDER_STATUS = OrderStatus.Canceled;

    private static final PaymentStatus DEFAULT_PAYMENT_STATUS = PaymentStatus.Paid;
    private static final PaymentStatus UPDATED_PAYMENT_STATUS = PaymentStatus.Pending;

    private static final Double DEFAULT_TOTAL_AMOUNT = 1D;
    private static final Double UPDATED_TOTAL_AMOUNT = 2D;

    private static final Tax DEFAULT_TAX = Tax.GE_VAT_19PC;
    private static final Tax UPDATED_TAX = Tax.GE_VAT_19PC;
    private static final String DEFAULT_PAYPAL_ACCOUNT_ID = "SAMPLE_TEXT";
    private static final String UPDATED_PAYPAL_ACCOUNT_ID = "UPDATED_TEXT";
    private static final String DEFAULT_PAYPAL_TRANSACTION_ID = "SAMPLE_TEXT";
    private static final String UPDATED_PAYPAL_TRANSACTION_ID = "UPDATED_TEXT";

    private static final Currency DEFAULT_CURRENCY = Currency.EUR;
    private static final Currency UPDATED_CURRENCY = Currency.EUR;

    private static final Currency DEFAULT_TAX_CURRENCY = Currency.;
    private static final Currency UPDATED_TAX_CURRENCY = Currency.;

    @Inject
    private CustomerOrderRepository customerOrderRepository;

    @Inject
    private CustomerOrderMapper customerOrderMapper;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restCustomerOrderMockMvc;

    private CustomerOrder customerOrder;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CustomerOrderResource customerOrderResource = new CustomerOrderResource();
        ReflectionTestUtils.setField(customerOrderResource, "customerOrderRepository", customerOrderRepository);
        ReflectionTestUtils.setField(customerOrderResource, "customerOrderMapper", customerOrderMapper);
        this.restCustomerOrderMockMvc = MockMvcBuilders.standaloneSetup(customerOrderResource).setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        customerOrder = new CustomerOrder();
        customerOrder.setPlacedOn(DEFAULT_PLACED_ON);
        customerOrder.setOrderStatus(DEFAULT_ORDER_STATUS);
        customerOrder.setPaymentStatus(DEFAULT_PAYMENT_STATUS);
        customerOrder.setTotalAmount(DEFAULT_TOTAL_AMOUNT);
        customerOrder.setTax(DEFAULT_TAX);
        customerOrder.setPaypalAccountId(DEFAULT_PAYPAL_ACCOUNT_ID);
        customerOrder.setPaypalTransactionId(DEFAULT_PAYPAL_TRANSACTION_ID);
        customerOrder.setCurrency(DEFAULT_CURRENCY);
        customerOrder.setTaxCurrency(DEFAULT_TAX_CURRENCY);
    }

    @Test
    @Transactional
    public void createCustomerOrder() throws Exception {
        int databaseSizeBeforeCreate = customerOrderRepository.findAll().size();

        // Create the CustomerOrder
        CustomerOrderDTO customerOrderDTO = customerOrderMapper.customerOrderToCustomerOrderDTO(customerOrder);

        restCustomerOrderMockMvc.perform(post("/api/customerOrders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customerOrderDTO)))
                .andExpect(status().isCreated());

        // Validate the CustomerOrder in the database
        List<CustomerOrder> customerOrders = customerOrderRepository.findAll();
        assertThat(customerOrders).hasSize(databaseSizeBeforeCreate + 1);
        CustomerOrder testCustomerOrder = customerOrders.get(customerOrders.size() - 1);
        assertThat(testCustomerOrder.getPlacedOn().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_PLACED_ON);
        assertThat(testCustomerOrder.getOrderStatus()).isEqualTo(DEFAULT_ORDER_STATUS);
        assertThat(testCustomerOrder.getPaymentStatus()).isEqualTo(DEFAULT_PAYMENT_STATUS);
        assertThat(testCustomerOrder.getTotalAmount()).isEqualTo(DEFAULT_TOTAL_AMOUNT);
        assertThat(testCustomerOrder.getTax()).isEqualTo(DEFAULT_TAX);
        assertThat(testCustomerOrder.getPaypalAccountId()).isEqualTo(DEFAULT_PAYPAL_ACCOUNT_ID);
        assertThat(testCustomerOrder.getPaypalTransactionId()).isEqualTo(DEFAULT_PAYPAL_TRANSACTION_ID);
        assertThat(testCustomerOrder.getCurrency()).isEqualTo(DEFAULT_CURRENCY);
        assertThat(testCustomerOrder.getTaxCurrency()).isEqualTo(DEFAULT_TAX_CURRENCY);
    }

    @Test
    @Transactional
    public void checkPlacedOnIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerOrderRepository.findAll().size();
        // set the field null
        customerOrder.setPlacedOn(null);

        // Create the CustomerOrder, which fails.
        CustomerOrderDTO customerOrderDTO = customerOrderMapper.customerOrderToCustomerOrderDTO(customerOrder);

        restCustomerOrderMockMvc.perform(post("/api/customerOrders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customerOrderDTO)))
                .andExpect(status().isBadRequest());

        List<CustomerOrder> customerOrders = customerOrderRepository.findAll();
        assertThat(customerOrders).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOrderStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerOrderRepository.findAll().size();
        // set the field null
        customerOrder.setOrderStatus(null);

        // Create the CustomerOrder, which fails.
        CustomerOrderDTO customerOrderDTO = customerOrderMapper.customerOrderToCustomerOrderDTO(customerOrder);

        restCustomerOrderMockMvc.perform(post("/api/customerOrders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customerOrderDTO)))
                .andExpect(status().isBadRequest());

        List<CustomerOrder> customerOrders = customerOrderRepository.findAll();
        assertThat(customerOrders).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPaymentStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerOrderRepository.findAll().size();
        // set the field null
        customerOrder.setPaymentStatus(null);

        // Create the CustomerOrder, which fails.
        CustomerOrderDTO customerOrderDTO = customerOrderMapper.customerOrderToCustomerOrderDTO(customerOrder);

        restCustomerOrderMockMvc.perform(post("/api/customerOrders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customerOrderDTO)))
                .andExpect(status().isBadRequest());

        List<CustomerOrder> customerOrders = customerOrderRepository.findAll();
        assertThat(customerOrders).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTotalAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerOrderRepository.findAll().size();
        // set the field null
        customerOrder.setTotalAmount(null);

        // Create the CustomerOrder, which fails.
        CustomerOrderDTO customerOrderDTO = customerOrderMapper.customerOrderToCustomerOrderDTO(customerOrder);

        restCustomerOrderMockMvc.perform(post("/api/customerOrders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customerOrderDTO)))
                .andExpect(status().isBadRequest());

        List<CustomerOrder> customerOrders = customerOrderRepository.findAll();
        assertThat(customerOrders).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTaxIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerOrderRepository.findAll().size();
        // set the field null
        customerOrder.setTax(null);

        // Create the CustomerOrder, which fails.
        CustomerOrderDTO customerOrderDTO = customerOrderMapper.customerOrderToCustomerOrderDTO(customerOrder);

        restCustomerOrderMockMvc.perform(post("/api/customerOrders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customerOrderDTO)))
                .andExpect(status().isBadRequest());

        List<CustomerOrder> customerOrders = customerOrderRepository.findAll();
        assertThat(customerOrders).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPaypalAccountIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerOrderRepository.findAll().size();
        // set the field null
        customerOrder.setPaypalAccountId(null);

        // Create the CustomerOrder, which fails.
        CustomerOrderDTO customerOrderDTO = customerOrderMapper.customerOrderToCustomerOrderDTO(customerOrder);

        restCustomerOrderMockMvc.perform(post("/api/customerOrders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customerOrderDTO)))
                .andExpect(status().isBadRequest());

        List<CustomerOrder> customerOrders = customerOrderRepository.findAll();
        assertThat(customerOrders).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPaypalTransactionIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerOrderRepository.findAll().size();
        // set the field null
        customerOrder.setPaypalTransactionId(null);

        // Create the CustomerOrder, which fails.
        CustomerOrderDTO customerOrderDTO = customerOrderMapper.customerOrderToCustomerOrderDTO(customerOrder);

        restCustomerOrderMockMvc.perform(post("/api/customerOrders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customerOrderDTO)))
                .andExpect(status().isBadRequest());

        List<CustomerOrder> customerOrders = customerOrderRepository.findAll();
        assertThat(customerOrders).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCurrencyIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerOrderRepository.findAll().size();
        // set the field null
        customerOrder.setCurrency(null);

        // Create the CustomerOrder, which fails.
        CustomerOrderDTO customerOrderDTO = customerOrderMapper.customerOrderToCustomerOrderDTO(customerOrder);

        restCustomerOrderMockMvc.perform(post("/api/customerOrders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customerOrderDTO)))
                .andExpect(status().isBadRequest());

        List<CustomerOrder> customerOrders = customerOrderRepository.findAll();
        assertThat(customerOrders).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTaxCurrencyIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerOrderRepository.findAll().size();
        // set the field null
        customerOrder.setTaxCurrency(null);

        // Create the CustomerOrder, which fails.
        CustomerOrderDTO customerOrderDTO = customerOrderMapper.customerOrderToCustomerOrderDTO(customerOrder);

        restCustomerOrderMockMvc.perform(post("/api/customerOrders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customerOrderDTO)))
                .andExpect(status().isBadRequest());

        List<CustomerOrder> customerOrders = customerOrderRepository.findAll();
        assertThat(customerOrders).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCustomerOrders() throws Exception {
        // Initialize the database
        customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrders
        restCustomerOrderMockMvc.perform(get("/api/customerOrders"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(customerOrder.getId().intValue())))
                .andExpect(jsonPath("$.[*].placedOn").value(hasItem(DEFAULT_PLACED_ON_STR)))
                .andExpect(jsonPath("$.[*].orderStatus").value(hasItem(DEFAULT_ORDER_STATUS.toString())))
                .andExpect(jsonPath("$.[*].paymentStatus").value(hasItem(DEFAULT_PAYMENT_STATUS.toString())))
                .andExpect(jsonPath("$.[*].totalAmount").value(hasItem(DEFAULT_TOTAL_AMOUNT.doubleValue())))
                .andExpect(jsonPath("$.[*].tax").value(hasItem(DEFAULT_TAX.toString())))
                .andExpect(jsonPath("$.[*].paypalAccountId").value(hasItem(DEFAULT_PAYPAL_ACCOUNT_ID.toString())))
                .andExpect(jsonPath("$.[*].paypalTransactionId").value(hasItem(DEFAULT_PAYPAL_TRANSACTION_ID.toString())))
                .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY.toString())))
                .andExpect(jsonPath("$.[*].taxCurrency").value(hasItem(DEFAULT_TAX_CURRENCY.toString())));
    }

    @Test
    @Transactional
    public void getCustomerOrder() throws Exception {
        // Initialize the database
        customerOrderRepository.saveAndFlush(customerOrder);

        // Get the customerOrder
        restCustomerOrderMockMvc.perform(get("/api/customerOrders/{id}", customerOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(customerOrder.getId().intValue()))
            .andExpect(jsonPath("$.placedOn").value(DEFAULT_PLACED_ON_STR))
            .andExpect(jsonPath("$.orderStatus").value(DEFAULT_ORDER_STATUS.toString()))
            .andExpect(jsonPath("$.paymentStatus").value(DEFAULT_PAYMENT_STATUS.toString()))
            .andExpect(jsonPath("$.totalAmount").value(DEFAULT_TOTAL_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.tax").value(DEFAULT_TAX.toString()))
            .andExpect(jsonPath("$.paypalAccountId").value(DEFAULT_PAYPAL_ACCOUNT_ID.toString()))
            .andExpect(jsonPath("$.paypalTransactionId").value(DEFAULT_PAYPAL_TRANSACTION_ID.toString()))
            .andExpect(jsonPath("$.currency").value(DEFAULT_CURRENCY.toString()))
            .andExpect(jsonPath("$.taxCurrency").value(DEFAULT_TAX_CURRENCY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCustomerOrder() throws Exception {
        // Get the customerOrder
        restCustomerOrderMockMvc.perform(get("/api/customerOrders/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCustomerOrder() throws Exception {
        // Initialize the database
        customerOrderRepository.saveAndFlush(customerOrder);

		int databaseSizeBeforeUpdate = customerOrderRepository.findAll().size();

        // Update the customerOrder
        customerOrder.setPlacedOn(UPDATED_PLACED_ON);
        customerOrder.setOrderStatus(UPDATED_ORDER_STATUS);
        customerOrder.setPaymentStatus(UPDATED_PAYMENT_STATUS);
        customerOrder.setTotalAmount(UPDATED_TOTAL_AMOUNT);
        customerOrder.setTax(UPDATED_TAX);
        customerOrder.setPaypalAccountId(UPDATED_PAYPAL_ACCOUNT_ID);
        customerOrder.setPaypalTransactionId(UPDATED_PAYPAL_TRANSACTION_ID);
        customerOrder.setCurrency(UPDATED_CURRENCY);
        customerOrder.setTaxCurrency(UPDATED_TAX_CURRENCY);
        
        CustomerOrderDTO customerOrderDTO = customerOrderMapper.customerOrderToCustomerOrderDTO(customerOrder);

        restCustomerOrderMockMvc.perform(put("/api/customerOrders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customerOrderDTO)))
                .andExpect(status().isOk());

        // Validate the CustomerOrder in the database
        List<CustomerOrder> customerOrders = customerOrderRepository.findAll();
        assertThat(customerOrders).hasSize(databaseSizeBeforeUpdate);
        CustomerOrder testCustomerOrder = customerOrders.get(customerOrders.size() - 1);
        assertThat(testCustomerOrder.getPlacedOn().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_PLACED_ON);
        assertThat(testCustomerOrder.getOrderStatus()).isEqualTo(UPDATED_ORDER_STATUS);
        assertThat(testCustomerOrder.getPaymentStatus()).isEqualTo(UPDATED_PAYMENT_STATUS);
        assertThat(testCustomerOrder.getTotalAmount()).isEqualTo(UPDATED_TOTAL_AMOUNT);
        assertThat(testCustomerOrder.getTax()).isEqualTo(UPDATED_TAX);
        assertThat(testCustomerOrder.getPaypalAccountId()).isEqualTo(UPDATED_PAYPAL_ACCOUNT_ID);
        assertThat(testCustomerOrder.getPaypalTransactionId()).isEqualTo(UPDATED_PAYPAL_TRANSACTION_ID);
        assertThat(testCustomerOrder.getCurrency()).isEqualTo(UPDATED_CURRENCY);
        assertThat(testCustomerOrder.getTaxCurrency()).isEqualTo(UPDATED_TAX_CURRENCY);
    }

    @Test
    @Transactional
    public void deleteCustomerOrder() throws Exception {
        // Initialize the database
        customerOrderRepository.saveAndFlush(customerOrder);

		int databaseSizeBeforeDelete = customerOrderRepository.findAll().size();

        // Get the customerOrder
        restCustomerOrderMockMvc.perform(delete("/api/customerOrders/{id}", customerOrder.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<CustomerOrder> customerOrders = customerOrderRepository.findAll();
        assertThat(customerOrders).hasSize(databaseSizeBeforeDelete - 1);
    }
}
