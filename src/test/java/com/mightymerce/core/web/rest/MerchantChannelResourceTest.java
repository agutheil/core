package com.mightymerce.core.web.rest;

import com.mightymerce.core.Application;
import com.mightymerce.core.domain.MerchantChannel;
import com.mightymerce.core.repository.MerchantChannelRepository;

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

import com.mightymerce.core.domain.enumeration.Channel;

/**
 * Test class for the MerchantChannelResource REST controller.
 *
 * @see MerchantChannelResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class MerchantChannelResourceTest {

    private static final String DEFAULT_ACCESS_TOKEN = "SAMPLE_TEXT";
    private static final String UPDATED_ACCESS_TOKEN = "UPDATED_TEXT";

    private static final Channel DEFAULT_CHANNEL = Channel.facebook;
    private static final Channel UPDATED_CHANNEL = Channel.twitter;

    @Inject
    private MerchantChannelRepository merchantChannelRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restMerchantChannelMockMvc;

    private MerchantChannel merchantChannel;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MerchantChannelResource merchantChannelResource = new MerchantChannelResource();
        ReflectionTestUtils.setField(merchantChannelResource, "merchantChannelRepository", merchantChannelRepository);
        this.restMerchantChannelMockMvc = MockMvcBuilders.standaloneSetup(merchantChannelResource).setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        merchantChannel = new MerchantChannel();
        merchantChannel.setAccessToken(DEFAULT_ACCESS_TOKEN);
        merchantChannel.setChannel(DEFAULT_CHANNEL);
    }

    @Test
    @Transactional
    public void createMerchantChannel() throws Exception {
        int databaseSizeBeforeCreate = merchantChannelRepository.findAll().size();

        // Create the MerchantChannel

        restMerchantChannelMockMvc.perform(post("/api/merchantChannels")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(merchantChannel)))
                .andExpect(status().isCreated());

        // Validate the MerchantChannel in the database
        List<MerchantChannel> merchantChannels = merchantChannelRepository.findAll();
        assertThat(merchantChannels).hasSize(databaseSizeBeforeCreate + 1);
        MerchantChannel testMerchantChannel = merchantChannels.get(merchantChannels.size() - 1);
        assertThat(testMerchantChannel.getAccessToken()).isEqualTo(DEFAULT_ACCESS_TOKEN);
        assertThat(testMerchantChannel.getChannel()).isEqualTo(DEFAULT_CHANNEL);
    }

    @Test
    @Transactional
    public void checkAccessTokenIsRequired() throws Exception {
        int databaseSizeBeforeTest = merchantChannelRepository.findAll().size();
        // set the field null
        merchantChannel.setAccessToken(null);

        // Create the MerchantChannel, which fails.

        restMerchantChannelMockMvc.perform(post("/api/merchantChannels")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(merchantChannel)))
                .andExpect(status().isBadRequest());

        List<MerchantChannel> merchantChannels = merchantChannelRepository.findAll();
        assertThat(merchantChannels).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkChannelIsRequired() throws Exception {
        int databaseSizeBeforeTest = merchantChannelRepository.findAll().size();
        // set the field null
        merchantChannel.setChannel(null);

        // Create the MerchantChannel, which fails.

        restMerchantChannelMockMvc.perform(post("/api/merchantChannels")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(merchantChannel)))
                .andExpect(status().isBadRequest());

        List<MerchantChannel> merchantChannels = merchantChannelRepository.findAll();
        assertThat(merchantChannels).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMerchantChannels() throws Exception {
        // Initialize the database
        merchantChannelRepository.saveAndFlush(merchantChannel);

        // Get all the merchantChannels
        restMerchantChannelMockMvc.perform(get("/api/merchantChannels"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(merchantChannel.getId().intValue())))
                .andExpect(jsonPath("$.[*].accessToken").value(hasItem(DEFAULT_ACCESS_TOKEN.toString())))
                .andExpect(jsonPath("$.[*].channel").value(hasItem(DEFAULT_CHANNEL.toString())));
    }

    @Test
    @Transactional
    public void getMerchantChannel() throws Exception {
        // Initialize the database
        merchantChannelRepository.saveAndFlush(merchantChannel);

        // Get the merchantChannel
        restMerchantChannelMockMvc.perform(get("/api/merchantChannels/{id}", merchantChannel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(merchantChannel.getId().intValue()))
            .andExpect(jsonPath("$.accessToken").value(DEFAULT_ACCESS_TOKEN.toString()))
            .andExpect(jsonPath("$.channel").value(DEFAULT_CHANNEL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMerchantChannel() throws Exception {
        // Get the merchantChannel
        restMerchantChannelMockMvc.perform(get("/api/merchantChannels/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMerchantChannel() throws Exception {
        // Initialize the database
        merchantChannelRepository.saveAndFlush(merchantChannel);

		int databaseSizeBeforeUpdate = merchantChannelRepository.findAll().size();

        // Update the merchantChannel
        merchantChannel.setAccessToken(UPDATED_ACCESS_TOKEN);
        merchantChannel.setChannel(UPDATED_CHANNEL);
        

        restMerchantChannelMockMvc.perform(put("/api/merchantChannels")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(merchantChannel)))
                .andExpect(status().isOk());

        // Validate the MerchantChannel in the database
        List<MerchantChannel> merchantChannels = merchantChannelRepository.findAll();
        assertThat(merchantChannels).hasSize(databaseSizeBeforeUpdate);
        MerchantChannel testMerchantChannel = merchantChannels.get(merchantChannels.size() - 1);
        assertThat(testMerchantChannel.getAccessToken()).isEqualTo(UPDATED_ACCESS_TOKEN);
        assertThat(testMerchantChannel.getChannel()).isEqualTo(UPDATED_CHANNEL);
    }

    @Test
    @Transactional
    public void deleteMerchantChannel() throws Exception {
        // Initialize the database
        merchantChannelRepository.saveAndFlush(merchantChannel);

		int databaseSizeBeforeDelete = merchantChannelRepository.findAll().size();

        // Get the merchantChannel
        restMerchantChannelMockMvc.perform(delete("/api/merchantChannels/{id}", merchantChannel.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<MerchantChannel> merchantChannels = merchantChannelRepository.findAll();
        assertThat(merchantChannels).hasSize(databaseSizeBeforeDelete - 1);
    }
}
