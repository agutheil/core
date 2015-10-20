package com.mightymerce.core.web.rest;

import com.mightymerce.core.Application;
import com.mightymerce.core.domain.CustomerAddress;
import com.mightymerce.core.repository.CustomerAddressRepository;
import com.mightymerce.core.web.rest.dto.CustomerAddressDTO;
import com.mightymerce.core.web.rest.mapper.CustomerAddressMapper;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the CustomerAddressResource REST controller.
 *
 * @see CustomerAddressResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class CustomerAddressResourceTest {

    private static final String DEFAULT_ADDRESS_TO = "SAMPLE_TEXT";
    private static final String UPDATED_ADDRESS_TO = "UPDATED_TEXT";
    private static final String DEFAULT_STREET_ADDRESS = "SAMPLE_TEXT";
    private static final String UPDATED_STREET_ADDRESS = "UPDATED_TEXT";
    private static final String DEFAULT_ZIP = "SAMPLE_TEXT";
    private static final String UPDATED_ZIP = "UPDATED_TEXT";
    private static final String DEFAULT_CITY = "SAMPLE_TEXT";
    private static final String UPDATED_CITY = "UPDATED_TEXT";
    private static final String DEFAULT_STATE = "SAMPLE_TEXT";
    private static final String UPDATED_STATE = "UPDATED_TEXT";
    private static final String DEFAULT_COUNTRY = "SAMPLE_TEXT";
    private static final String UPDATED_COUNTRY = "UPDATED_TEXT";
    private static final String DEFAULT_STATUS = "SAMPLE_TEXT";
    private static final String UPDATED_STATUS = "UPDATED_TEXT";

    @Inject
    private CustomerAddressRepository customerAddressRepository;

    @Inject
    private CustomerAddressMapper customerAddressMapper;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restCustomerAddressMockMvc;

    private CustomerAddress customerAddress;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CustomerAddressResource customerAddressResource = new CustomerAddressResource();
        ReflectionTestUtils.setField(customerAddressResource, "customerAddressRepository", customerAddressRepository);
        ReflectionTestUtils.setField(customerAddressResource, "customerAddressMapper", customerAddressMapper);
        this.restCustomerAddressMockMvc = MockMvcBuilders.standaloneSetup(customerAddressResource).setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        customerAddress = new CustomerAddress();
        customerAddress.setAddressTo(DEFAULT_ADDRESS_TO);
        customerAddress.setStreetAddress(DEFAULT_STREET_ADDRESS);
        customerAddress.setZip(DEFAULT_ZIP);
        customerAddress.setCity(DEFAULT_CITY);
        customerAddress.setState(DEFAULT_STATE);
        customerAddress.setCountry(DEFAULT_COUNTRY);
        customerAddress.setStatus(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createCustomerAddress() throws Exception {
        int databaseSizeBeforeCreate = customerAddressRepository.findAll().size();

        // Create the CustomerAddress
        CustomerAddressDTO customerAddressDTO = customerAddressMapper.customerAddressToCustomerAddressDTO(customerAddress);

        restCustomerAddressMockMvc.perform(post("/api/customerAddresss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customerAddressDTO)))
                .andExpect(status().isCreated());

        // Validate the CustomerAddress in the database
        List<CustomerAddress> customerAddresss = customerAddressRepository.findAll();
        assertThat(customerAddresss).hasSize(databaseSizeBeforeCreate + 1);
        CustomerAddress testCustomerAddress = customerAddresss.get(customerAddresss.size() - 1);
        assertThat(testCustomerAddress.getAddressTo()).isEqualTo(DEFAULT_ADDRESS_TO);
        assertThat(testCustomerAddress.getStreetAddress()).isEqualTo(DEFAULT_STREET_ADDRESS);
        assertThat(testCustomerAddress.getZip()).isEqualTo(DEFAULT_ZIP);
        assertThat(testCustomerAddress.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testCustomerAddress.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testCustomerAddress.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testCustomerAddress.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void checkAddressToIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerAddressRepository.findAll().size();
        // set the field null
        customerAddress.setAddressTo(null);

        // Create the CustomerAddress, which fails.
        CustomerAddressDTO customerAddressDTO = customerAddressMapper.customerAddressToCustomerAddressDTO(customerAddress);

        restCustomerAddressMockMvc.perform(post("/api/customerAddresss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customerAddressDTO)))
                .andExpect(status().isBadRequest());

        List<CustomerAddress> customerAddresss = customerAddressRepository.findAll();
        assertThat(customerAddresss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStreetAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerAddressRepository.findAll().size();
        // set the field null
        customerAddress.setStreetAddress(null);

        // Create the CustomerAddress, which fails.
        CustomerAddressDTO customerAddressDTO = customerAddressMapper.customerAddressToCustomerAddressDTO(customerAddress);

        restCustomerAddressMockMvc.perform(post("/api/customerAddresss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customerAddressDTO)))
                .andExpect(status().isBadRequest());

        List<CustomerAddress> customerAddresss = customerAddressRepository.findAll();
        assertThat(customerAddresss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkZipIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerAddressRepository.findAll().size();
        // set the field null
        customerAddress.setZip(null);

        // Create the CustomerAddress, which fails.
        CustomerAddressDTO customerAddressDTO = customerAddressMapper.customerAddressToCustomerAddressDTO(customerAddress);

        restCustomerAddressMockMvc.perform(post("/api/customerAddresss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customerAddressDTO)))
                .andExpect(status().isBadRequest());

        List<CustomerAddress> customerAddresss = customerAddressRepository.findAll();
        assertThat(customerAddresss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCityIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerAddressRepository.findAll().size();
        // set the field null
        customerAddress.setCity(null);

        // Create the CustomerAddress, which fails.
        CustomerAddressDTO customerAddressDTO = customerAddressMapper.customerAddressToCustomerAddressDTO(customerAddress);

        restCustomerAddressMockMvc.perform(post("/api/customerAddresss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customerAddressDTO)))
                .andExpect(status().isBadRequest());

        List<CustomerAddress> customerAddresss = customerAddressRepository.findAll();
        assertThat(customerAddresss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStateIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerAddressRepository.findAll().size();
        // set the field null
        customerAddress.setState(null);

        // Create the CustomerAddress, which fails.
        CustomerAddressDTO customerAddressDTO = customerAddressMapper.customerAddressToCustomerAddressDTO(customerAddress);

        restCustomerAddressMockMvc.perform(post("/api/customerAddresss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customerAddressDTO)))
                .andExpect(status().isBadRequest());

        List<CustomerAddress> customerAddresss = customerAddressRepository.findAll();
        assertThat(customerAddresss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCountryIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerAddressRepository.findAll().size();
        // set the field null
        customerAddress.setCountry(null);

        // Create the CustomerAddress, which fails.
        CustomerAddressDTO customerAddressDTO = customerAddressMapper.customerAddressToCustomerAddressDTO(customerAddress);

        restCustomerAddressMockMvc.perform(post("/api/customerAddresss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customerAddressDTO)))
                .andExpect(status().isBadRequest());

        List<CustomerAddress> customerAddresss = customerAddressRepository.findAll();
        assertThat(customerAddresss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCustomerAddresss() throws Exception {
        // Initialize the database
        customerAddressRepository.saveAndFlush(customerAddress);

        // Get all the customerAddresss
        restCustomerAddressMockMvc.perform(get("/api/customerAddresss"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(customerAddress.getId().intValue())))
                .andExpect(jsonPath("$.[*].addressTo").value(hasItem(DEFAULT_ADDRESS_TO.toString())))
                .andExpect(jsonPath("$.[*].streetAddress").value(hasItem(DEFAULT_STREET_ADDRESS.toString())))
                .andExpect(jsonPath("$.[*].zip").value(hasItem(DEFAULT_ZIP.toString())))
                .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
                .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
                .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY.toString())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getCustomerAddress() throws Exception {
        // Initialize the database
        customerAddressRepository.saveAndFlush(customerAddress);

        // Get the customerAddress
        restCustomerAddressMockMvc.perform(get("/api/customerAddresss/{id}", customerAddress.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(customerAddress.getId().intValue()))
            .andExpect(jsonPath("$.addressTo").value(DEFAULT_ADDRESS_TO.toString()))
            .andExpect(jsonPath("$.streetAddress").value(DEFAULT_STREET_ADDRESS.toString()))
            .andExpect(jsonPath("$.zip").value(DEFAULT_ZIP.toString()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCustomerAddress() throws Exception {
        // Get the customerAddress
        restCustomerAddressMockMvc.perform(get("/api/customerAddresss/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCustomerAddress() throws Exception {
        // Initialize the database
        customerAddressRepository.saveAndFlush(customerAddress);

		int databaseSizeBeforeUpdate = customerAddressRepository.findAll().size();

        // Update the customerAddress
        customerAddress.setAddressTo(UPDATED_ADDRESS_TO);
        customerAddress.setStreetAddress(UPDATED_STREET_ADDRESS);
        customerAddress.setZip(UPDATED_ZIP);
        customerAddress.setCity(UPDATED_CITY);
        customerAddress.setState(UPDATED_STATE);
        customerAddress.setCountry(UPDATED_COUNTRY);
        customerAddress.setStatus(UPDATED_STATUS);
        
        CustomerAddressDTO customerAddressDTO = customerAddressMapper.customerAddressToCustomerAddressDTO(customerAddress);

        restCustomerAddressMockMvc.perform(put("/api/customerAddresss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customerAddressDTO)))
                .andExpect(status().isOk());

        // Validate the CustomerAddress in the database
        List<CustomerAddress> customerAddresss = customerAddressRepository.findAll();
        assertThat(customerAddresss).hasSize(databaseSizeBeforeUpdate);
        CustomerAddress testCustomerAddress = customerAddresss.get(customerAddresss.size() - 1);
        assertThat(testCustomerAddress.getAddressTo()).isEqualTo(UPDATED_ADDRESS_TO);
        assertThat(testCustomerAddress.getStreetAddress()).isEqualTo(UPDATED_STREET_ADDRESS);
        assertThat(testCustomerAddress.getZip()).isEqualTo(UPDATED_ZIP);
        assertThat(testCustomerAddress.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testCustomerAddress.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testCustomerAddress.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testCustomerAddress.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void deleteCustomerAddress() throws Exception {
        // Initialize the database
        customerAddressRepository.saveAndFlush(customerAddress);

		int databaseSizeBeforeDelete = customerAddressRepository.findAll().size();

        // Get the customerAddress
        restCustomerAddressMockMvc.perform(delete("/api/customerAddresss/{id}", customerAddress.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<CustomerAddress> customerAddresss = customerAddressRepository.findAll();
        assertThat(customerAddresss).hasSize(databaseSizeBeforeDelete - 1);
    }
}
