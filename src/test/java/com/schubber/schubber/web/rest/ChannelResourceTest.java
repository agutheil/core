package com.schubber.schubber.web.rest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import com.schubber.schubber.Application;
import com.schubber.schubber.domain.Channel;
import com.schubber.schubber.repository.ChannelRepository;

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

    private static final String DEFAULT_TYPE = "SAMPLE_TEXT";
    private static final String UPDATED_TYPE = "UPDATED_TEXT";
    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";
    private static final String DEFAULT_TOKEN = "SAMPLE_TEXT";
    private static final String UPDATED_TOKEN = "UPDATED_TEXT";

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
        channelRepository.deleteAll();
        channel = new Channel();
        channel.setType(DEFAULT_TYPE);
        channel.setName(DEFAULT_NAME);
        channel.setToken(DEFAULT_TOKEN);
    }

    @Test
    public void createChannel() throws Exception {
        // Validate the database is empty
        assertThat(channelRepository.findAll()).hasSize(0);

        // Create the Channel
        restChannelMockMvc.perform(post("/api/channels")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(channel)))
                .andExpect(status().isOk());

        // Validate the Channel in the database
        List<Channel> channels = channelRepository.findAll();
        assertThat(channels).hasSize(1);
        Channel testChannel = channels.iterator().next();
        assertThat(testChannel.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testChannel.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testChannel.getToken()).isEqualTo(DEFAULT_TOKEN);
    }

    @Test
    public void getAllChannels() throws Exception {
        // Initialize the database
        channelRepository.save(channel);

        // Get all the channels
        restChannelMockMvc.perform(get("/api/channels"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(channel.getId()))
                .andExpect(jsonPath("$.[0].type").value(DEFAULT_TYPE.toString()))
                .andExpect(jsonPath("$.[0].name").value(DEFAULT_NAME.toString()))
                .andExpect(jsonPath("$.[0].token").value(DEFAULT_TOKEN.toString()));
    }

    @Test
    public void getChannel() throws Exception {
        // Initialize the database
        channelRepository.save(channel);

        // Get the channel
        restChannelMockMvc.perform(get("/api/channels/{id}", channel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(channel.getId()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.token").value(DEFAULT_TOKEN.toString()));
    }

    @Test
    public void getNonExistingChannel() throws Exception {
        // Get the channel
        restChannelMockMvc.perform(get("/api/channels/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateChannel() throws Exception {
        // Initialize the database
        channelRepository.save(channel);

        // Update the channel
        channel.setType(UPDATED_TYPE);
        channel.setName(UPDATED_NAME);
        channel.setToken(UPDATED_TOKEN);
        restChannelMockMvc.perform(post("/api/channels")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(channel)))
                .andExpect(status().isOk());

        // Validate the Channel in the database
        List<Channel> channels = channelRepository.findAll();
        assertThat(channels).hasSize(1);
        Channel testChannel = channels.iterator().next();
        assertThat(testChannel.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testChannel.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testChannel.getToken()).isEqualTo(UPDATED_TOKEN);
    }

    @Test
    public void deleteChannel() throws Exception {
        // Initialize the database
        channelRepository.save(channel);

        // Get the channel
        restChannelMockMvc.perform(delete("/api/channels/{id}", channel.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Channel> channels = channelRepository.findAll();
        assertThat(channels).hasSize(0);
    }
}
