package com.mightymerce.core.web.rest;

import com.mightymerce.core.Application;
import com.mightymerce.core.domain.Channel;
import com.mightymerce.core.repository.ChannelRepository;

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
 * Test class for the ChannelResource REST controller.
 *
 * @see ChannelResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ChannelResourceTest {

    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";

    @Inject
    private ChannelRepository channelRepository;

    private MockMvc restChannelMockMvc;

    private Channel channel;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ChannelResource channelResource = new ChannelResource();
        ReflectionTestUtils.setField(channelResource, "channelRepository", channelRepository);
        this.restChannelMockMvc = MockMvcBuilders.standaloneSetup(channelResource).build();
    }

    @Before
    public void initTest() {
        channel = new Channel();
        channel.setName(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createChannel() throws Exception {
        int databaseSizeBeforeCreate = channelRepository.findAll().size();

        // Create the Channel
        restChannelMockMvc.perform(post("/api/channels")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(channel)))
                .andExpect(status().isCreated());

        // Validate the Channel in the database
        List<Channel> channels = channelRepository.findAll();
        assertThat(channels).hasSize(databaseSizeBeforeCreate + 1);
        Channel testChannel = channels.get(channels.size() - 1);
        assertThat(testChannel.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void getAllChannels() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);

        // Get all the channels
        restChannelMockMvc.perform(get("/api/channels"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(channel.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getChannel() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);

        // Get the channel
        restChannelMockMvc.perform(get("/api/channels/{id}", channel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(channel.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingChannel() throws Exception {
        // Get the channel
        restChannelMockMvc.perform(get("/api/channels/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChannel() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);
		
		int databaseSizeBeforeUpdate = channelRepository.findAll().size();

        // Update the channel
        channel.setName(UPDATED_NAME);
        restChannelMockMvc.perform(put("/api/channels")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(channel)))
                .andExpect(status().isOk());

        // Validate the Channel in the database
        List<Channel> channels = channelRepository.findAll();
        assertThat(channels).hasSize(databaseSizeBeforeUpdate);
        Channel testChannel = channels.get(channels.size() - 1);
        assertThat(testChannel.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteChannel() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);
		
		int databaseSizeBeforeDelete = channelRepository.findAll().size();

        // Get the channel
        restChannelMockMvc.perform(delete("/api/channels/{id}", channel.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Channel> channels = channelRepository.findAll();
        assertThat(channels).hasSize(databaseSizeBeforeDelete - 1);
    }
}
