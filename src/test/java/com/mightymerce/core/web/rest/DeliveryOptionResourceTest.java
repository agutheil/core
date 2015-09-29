package com.mightymerce.core.web.rest;

import com.mightymerce.core.Application;
import com.mightymerce.core.domain.DeliveryOption;
import com.mightymerce.core.repository.DeliveryOptionRepository;
import com.mightymerce.core.web.rest.dto.DeliveryOptionDTO;
import com.mightymerce.core.web.rest.mapper.DeliveryOptionMapper;

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
 * Test class for the DeliveryOptionResource REST controller.
 *
 * @see DeliveryOptionResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class DeliveryOptionResourceTest {

    private static final String DEFAULT_TITLE = "SAMPLE_TEXT";
    private static final String UPDATED_TITLE = "UPDATED_TEXT";
    private static final String DEFAULT_WITHIN = "SAMPLE_TEXT";
    private static final String UPDATED_WITHIN = "UPDATED_TEXT";
    private static final String DEFAULT_COUNTRY = "SAMPLE_TEXT";
    private static final String UPDATED_COUNTRY = "UPDATED_TEXT";
    private static final String DEFAULT_TAXROW = "SAMPLE_TEXT";
    private static final String UPDATED_TAXROW = "UPDATED_TEXT";

    private static final Double DEFAULT_COST = 1D;
    private static final Double UPDATED_COST = 2D;
    private static final String DEFAULT_CURRENCY = "SAMPLE_TEXT";
    private static final String UPDATED_CURRENCY = "UPDATED_TEXT";

    @Inject
    private DeliveryOptionRepository deliveryOptionRepository;

    @Inject
    private DeliveryOptionMapper deliveryOptionMapper;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restDeliveryOptionMockMvc;

    private DeliveryOption deliveryOption;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DeliveryOptionResource deliveryOptionResource = new DeliveryOptionResource();
        ReflectionTestUtils.setField(deliveryOptionResource, "deliveryOptionRepository", deliveryOptionRepository);
        ReflectionTestUtils.setField(deliveryOptionResource, "deliveryOptionMapper", deliveryOptionMapper);
        this.restDeliveryOptionMockMvc = MockMvcBuilders.standaloneSetup(deliveryOptionResource).setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        deliveryOption = new DeliveryOption();
        deliveryOption.setTitle(DEFAULT_TITLE);
        deliveryOption.setWithin(DEFAULT_WITHIN);
        deliveryOption.setCountry(DEFAULT_COUNTRY);
        deliveryOption.setTaxrow(DEFAULT_TAXROW);
        deliveryOption.setCost(DEFAULT_COST);
        deliveryOption.setCurrency(DEFAULT_CURRENCY);
    }

    @Test
    @Transactional
    public void createDeliveryOption() throws Exception {
        int databaseSizeBeforeCreate = deliveryOptionRepository.findAll().size();

        // Create the DeliveryOption
        DeliveryOptionDTO deliveryOptionDTO = deliveryOptionMapper.deliveryOptionToDeliveryOptionDTO(deliveryOption);

        restDeliveryOptionMockMvc.perform(post("/api/deliveryOptions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(deliveryOptionDTO)))
                .andExpect(status().isCreated());

        // Validate the DeliveryOption in the database
        List<DeliveryOption> deliveryOptions = deliveryOptionRepository.findAll();
        assertThat(deliveryOptions).hasSize(databaseSizeBeforeCreate + 1);
        DeliveryOption testDeliveryOption = deliveryOptions.get(deliveryOptions.size() - 1);
        assertThat(testDeliveryOption.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testDeliveryOption.getWithin()).isEqualTo(DEFAULT_WITHIN);
        assertThat(testDeliveryOption.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testDeliveryOption.getTaxrow()).isEqualTo(DEFAULT_TAXROW);
        assertThat(testDeliveryOption.getCost()).isEqualTo(DEFAULT_COST);
        assertThat(testDeliveryOption.getCurrency()).isEqualTo(DEFAULT_CURRENCY);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = deliveryOptionRepository.findAll().size();
        // set the field null
        deliveryOption.setTitle(null);

        // Create the DeliveryOption, which fails.
        DeliveryOptionDTO deliveryOptionDTO = deliveryOptionMapper.deliveryOptionToDeliveryOptionDTO(deliveryOption);

        restDeliveryOptionMockMvc.perform(post("/api/deliveryOptions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(deliveryOptionDTO)))
                .andExpect(status().isBadRequest());

        List<DeliveryOption> deliveryOptions = deliveryOptionRepository.findAll();
        assertThat(deliveryOptions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkWithinIsRequired() throws Exception {
        int databaseSizeBeforeTest = deliveryOptionRepository.findAll().size();
        // set the field null
        deliveryOption.setWithin(null);

        // Create the DeliveryOption, which fails.
        DeliveryOptionDTO deliveryOptionDTO = deliveryOptionMapper.deliveryOptionToDeliveryOptionDTO(deliveryOption);

        restDeliveryOptionMockMvc.perform(post("/api/deliveryOptions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(deliveryOptionDTO)))
                .andExpect(status().isBadRequest());

        List<DeliveryOption> deliveryOptions = deliveryOptionRepository.findAll();
        assertThat(deliveryOptions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCountryIsRequired() throws Exception {
        int databaseSizeBeforeTest = deliveryOptionRepository.findAll().size();
        // set the field null
        deliveryOption.setCountry(null);

        // Create the DeliveryOption, which fails.
        DeliveryOptionDTO deliveryOptionDTO = deliveryOptionMapper.deliveryOptionToDeliveryOptionDTO(deliveryOption);

        restDeliveryOptionMockMvc.perform(post("/api/deliveryOptions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(deliveryOptionDTO)))
                .andExpect(status().isBadRequest());

        List<DeliveryOption> deliveryOptions = deliveryOptionRepository.findAll();
        assertThat(deliveryOptions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTaxrowIsRequired() throws Exception {
        int databaseSizeBeforeTest = deliveryOptionRepository.findAll().size();
        // set the field null
        deliveryOption.setTaxrow(null);

        // Create the DeliveryOption, which fails.
        DeliveryOptionDTO deliveryOptionDTO = deliveryOptionMapper.deliveryOptionToDeliveryOptionDTO(deliveryOption);

        restDeliveryOptionMockMvc.perform(post("/api/deliveryOptions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(deliveryOptionDTO)))
                .andExpect(status().isBadRequest());

        List<DeliveryOption> deliveryOptions = deliveryOptionRepository.findAll();
        assertThat(deliveryOptions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCostIsRequired() throws Exception {
        int databaseSizeBeforeTest = deliveryOptionRepository.findAll().size();
        // set the field null
        deliveryOption.setCost(null);

        // Create the DeliveryOption, which fails.
        DeliveryOptionDTO deliveryOptionDTO = deliveryOptionMapper.deliveryOptionToDeliveryOptionDTO(deliveryOption);

        restDeliveryOptionMockMvc.perform(post("/api/deliveryOptions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(deliveryOptionDTO)))
                .andExpect(status().isBadRequest());

        List<DeliveryOption> deliveryOptions = deliveryOptionRepository.findAll();
        assertThat(deliveryOptions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCurrencyIsRequired() throws Exception {
        int databaseSizeBeforeTest = deliveryOptionRepository.findAll().size();
        // set the field null
        deliveryOption.setCurrency(null);

        // Create the DeliveryOption, which fails.
        DeliveryOptionDTO deliveryOptionDTO = deliveryOptionMapper.deliveryOptionToDeliveryOptionDTO(deliveryOption);

        restDeliveryOptionMockMvc.perform(post("/api/deliveryOptions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(deliveryOptionDTO)))
                .andExpect(status().isBadRequest());

        List<DeliveryOption> deliveryOptions = deliveryOptionRepository.findAll();
        assertThat(deliveryOptions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDeliveryOptions() throws Exception {
        // Initialize the database
        deliveryOptionRepository.saveAndFlush(deliveryOption);

        // Get all the deliveryOptions
        restDeliveryOptionMockMvc.perform(get("/api/deliveryOptions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(deliveryOption.getId().intValue())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].within").value(hasItem(DEFAULT_WITHIN.toString())))
                .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY.toString())))
                .andExpect(jsonPath("$.[*].taxrow").value(hasItem(DEFAULT_TAXROW.toString())))
                .andExpect(jsonPath("$.[*].cost").value(hasItem(DEFAULT_COST.doubleValue())))
                .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY.toString())));
    }

    @Test
    @Transactional
    public void getDeliveryOption() throws Exception {
        // Initialize the database
        deliveryOptionRepository.saveAndFlush(deliveryOption);

        // Get the deliveryOption
        restDeliveryOptionMockMvc.perform(get("/api/deliveryOptions/{id}", deliveryOption.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(deliveryOption.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.within").value(DEFAULT_WITHIN.toString()))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY.toString()))
            .andExpect(jsonPath("$.taxrow").value(DEFAULT_TAXROW.toString()))
            .andExpect(jsonPath("$.cost").value(DEFAULT_COST.doubleValue()))
            .andExpect(jsonPath("$.currency").value(DEFAULT_CURRENCY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDeliveryOption() throws Exception {
        // Get the deliveryOption
        restDeliveryOptionMockMvc.perform(get("/api/deliveryOptions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDeliveryOption() throws Exception {
        // Initialize the database
        deliveryOptionRepository.saveAndFlush(deliveryOption);

		int databaseSizeBeforeUpdate = deliveryOptionRepository.findAll().size();

        // Update the deliveryOption
        deliveryOption.setTitle(UPDATED_TITLE);
        deliveryOption.setWithin(UPDATED_WITHIN);
        deliveryOption.setCountry(UPDATED_COUNTRY);
        deliveryOption.setTaxrow(UPDATED_TAXROW);
        deliveryOption.setCost(UPDATED_COST);
        deliveryOption.setCurrency(UPDATED_CURRENCY);
        
        DeliveryOptionDTO deliveryOptionDTO = deliveryOptionMapper.deliveryOptionToDeliveryOptionDTO(deliveryOption);

        restDeliveryOptionMockMvc.perform(put("/api/deliveryOptions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(deliveryOptionDTO)))
                .andExpect(status().isOk());

        // Validate the DeliveryOption in the database
        List<DeliveryOption> deliveryOptions = deliveryOptionRepository.findAll();
        assertThat(deliveryOptions).hasSize(databaseSizeBeforeUpdate);
        DeliveryOption testDeliveryOption = deliveryOptions.get(deliveryOptions.size() - 1);
        assertThat(testDeliveryOption.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testDeliveryOption.getWithin()).isEqualTo(UPDATED_WITHIN);
        assertThat(testDeliveryOption.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testDeliveryOption.getTaxrow()).isEqualTo(UPDATED_TAXROW);
        assertThat(testDeliveryOption.getCost()).isEqualTo(UPDATED_COST);
        assertThat(testDeliveryOption.getCurrency()).isEqualTo(UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    public void deleteDeliveryOption() throws Exception {
        // Initialize the database
        deliveryOptionRepository.saveAndFlush(deliveryOption);

		int databaseSizeBeforeDelete = deliveryOptionRepository.findAll().size();

        // Get the deliveryOption
        restDeliveryOptionMockMvc.perform(delete("/api/deliveryOptions/{id}", deliveryOption.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<DeliveryOption> deliveryOptions = deliveryOptionRepository.findAll();
        assertThat(deliveryOptions).hasSize(databaseSizeBeforeDelete - 1);
    }
}
