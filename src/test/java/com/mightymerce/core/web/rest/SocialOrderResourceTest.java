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
 * Test class for the SocialOrderResource REST controller.
 *
 * @see SocialOrderResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class SocialOrderResourceTest {

    private static final String DEFAULT_TEST = "SAMPLE_TEXT";
    private static final String UPDATED_TEST = "UPDATED_TEXT";
    private static final String DEFAULT_TEST2 = "SAMPLE_TEXT";
    private static final String UPDATED_TEST2 = "UPDATED_TEXT";

    @Inject
    private SocialOrderRepository socialOrderRepository;

    private MockMvc restSocialOrderMockMvc;

    private SocialOrder socialOrder;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SocialOrderResource socialOrderResource = new SocialOrderResource();
        ReflectionTestUtils.setField(socialOrderResource, "socialOrderRepository", socialOrderRepository);
        this.restSocialOrderMockMvc = MockMvcBuilders.standaloneSetup(socialOrderResource).build();
    }

    @Before
    public void initTest() {
        socialOrder = new SocialOrder();
        socialOrder.setTest(DEFAULT_TEST);
        socialOrder.setTest2(DEFAULT_TEST2);
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
        assertThat(testSocialOrder.getTest()).isEqualTo(DEFAULT_TEST);
        assertThat(testSocialOrder.getTest2()).isEqualTo(DEFAULT_TEST2);
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
                .andExpect(jsonPath("$.[*].test").value(hasItem(DEFAULT_TEST.toString())))
                .andExpect(jsonPath("$.[*].test2").value(hasItem(DEFAULT_TEST2.toString())));
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
            .andExpect(jsonPath("$.test").value(DEFAULT_TEST.toString()))
            .andExpect(jsonPath("$.test2").value(DEFAULT_TEST2.toString()));
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
        socialOrder.setTest(UPDATED_TEST);
        socialOrder.setTest2(UPDATED_TEST2);
        restSocialOrderMockMvc.perform(put("/api/socialOrders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(socialOrder)))
                .andExpect(status().isOk());

        // Validate the SocialOrder in the database
        List<SocialOrder> socialOrders = socialOrderRepository.findAll();
        assertThat(socialOrders).hasSize(databaseSizeBeforeUpdate);
        SocialOrder testSocialOrder = socialOrders.get(socialOrders.size() - 1);
        assertThat(testSocialOrder.getTest()).isEqualTo(UPDATED_TEST);
        assertThat(testSocialOrder.getTest2()).isEqualTo(UPDATED_TEST2);
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
