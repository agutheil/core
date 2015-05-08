package com.mightymerce.core.web.rest;

import com.mightymerce.core.Application;
import com.mightymerce.core.domain.CustomerChannel;
import com.mightymerce.core.repository.CustomerChannelRepository;

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
 * Test class for the CustomerChannelResource REST controller.
 *
 * @see CustomerChannelResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class CustomerChannelResourceTest {

    private static final String DEFAULT_ACCESSTOKEN = "SAMPLE_TEXT";
    private static final String UPDATED_ACCESSTOKEN = "UPDATED_TEXT";
    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";

    @Inject
    private CustomerChannelRepository customerChannelRepository;

    private MockMvc restCustomerChannelMockMvc;

    private CustomerChannel customerChannel;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CustomerChannelResource customerChannelResource = new CustomerChannelResource();
        ReflectionTestUtils.setField(customerChannelResource, "customerChannelRepository", customerChannelRepository);
        this.restCustomerChannelMockMvc = MockMvcBuilders.standaloneSetup(customerChannelResource).build();
    }

    @Before
    public void initTest() {
        customerChannel = new CustomerChannel();
        customerChannel.setAccessToken(DEFAULT_ACCESSTOKEN);
        customerChannel.setName(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createCustomerChannel() throws Exception {
        int databaseSizeBeforeCreate = customerChannelRepository.findAll().size();

        // Create the CustomerChannel
        restCustomerChannelMockMvc.perform(post("/api/customerChannels")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customerChannel)))
                .andExpect(status().isCreated());

        // Validate the CustomerChannel in the database
        List<CustomerChannel> customerChannels = customerChannelRepository.findAll();
        assertThat(customerChannels).hasSize(databaseSizeBeforeCreate + 1);
        CustomerChannel testCustomerChannel = customerChannels.get(customerChannels.size() - 1);
        assertThat(testCustomerChannel.getAccessToken()).isEqualTo(DEFAULT_ACCESSTOKEN);
        assertThat(testCustomerChannel.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void getAllCustomerChannels() throws Exception {
        // Initialize the database
        customerChannelRepository.saveAndFlush(customerChannel);

        // Get all the customerChannels
        restCustomerChannelMockMvc.perform(get("/api/customerChannels"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(customerChannel.getId().intValue())))
                .andExpect(jsonPath("$.[*].accessToken").value(hasItem(DEFAULT_ACCESSTOKEN.toString())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getCustomerChannel() throws Exception {
        // Initialize the database
        customerChannelRepository.saveAndFlush(customerChannel);

        // Get the customerChannel
        restCustomerChannelMockMvc.perform(get("/api/customerChannels/{id}", customerChannel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(customerChannel.getId().intValue()))
            .andExpect(jsonPath("$.accessToken").value(DEFAULT_ACCESSTOKEN.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCustomerChannel() throws Exception {
        // Get the customerChannel
        restCustomerChannelMockMvc.perform(get("/api/customerChannels/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCustomerChannel() throws Exception {
        // Initialize the database
        customerChannelRepository.saveAndFlush(customerChannel);
		
		int databaseSizeBeforeUpdate = customerChannelRepository.findAll().size();

        // Update the customerChannel
        customerChannel.setAccessToken(UPDATED_ACCESSTOKEN);
        customerChannel.setName(UPDATED_NAME);
        restCustomerChannelMockMvc.perform(put("/api/customerChannels")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customerChannel)))
                .andExpect(status().isOk());

        // Validate the CustomerChannel in the database
        List<CustomerChannel> customerChannels = customerChannelRepository.findAll();
        assertThat(customerChannels).hasSize(databaseSizeBeforeUpdate);
        CustomerChannel testCustomerChannel = customerChannels.get(customerChannels.size() - 1);
        assertThat(testCustomerChannel.getAccessToken()).isEqualTo(UPDATED_ACCESSTOKEN);
        assertThat(testCustomerChannel.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteCustomerChannel() throws Exception {
        // Initialize the database
        customerChannelRepository.saveAndFlush(customerChannel);
		
		int databaseSizeBeforeDelete = customerChannelRepository.findAll().size();

        // Get the customerChannel
        restCustomerChannelMockMvc.perform(delete("/api/customerChannels/{id}", customerChannel.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<CustomerChannel> customerChannels = customerChannelRepository.findAll();
        assertThat(customerChannels).hasSize(databaseSizeBeforeDelete - 1);
    }
}
