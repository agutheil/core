package com.mightymerce.core.web.rest;

import com.mightymerce.core.Application;
import com.mightymerce.core.domain.ChannelPost;
import com.mightymerce.core.repository.ChannelPostRepository;

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
 * Test class for the ChannelPostResource REST controller.
 *
 * @see ChannelPostResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ChannelPostResourceTest {

    private static final String DEFAULT_STATUS = "SAMPLE_TEXT";
    private static final String UPDATED_STATUS = "UPDATED_TEXT";

    @Inject
    private ChannelPostRepository channelPostRepository;

    private MockMvc restChannelPostMockMvc;

    private ChannelPost channelPost;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ChannelPostResource channelPostResource = new ChannelPostResource();
        ReflectionTestUtils.setField(channelPostResource, "channelPostRepository", channelPostRepository);
        this.restChannelPostMockMvc = MockMvcBuilders.standaloneSetup(channelPostResource).build();
    }

    @Before
    public void initTest() {
        channelPost = new ChannelPost();
        channelPost.setStatus(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createChannelPost() throws Exception {
        int databaseSizeBeforeCreate = channelPostRepository.findAll().size();

        // Create the ChannelPost
        restChannelPostMockMvc.perform(post("/api/channelPosts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(channelPost)))
                .andExpect(status().isCreated());

        // Validate the ChannelPost in the database
        List<ChannelPost> channelPosts = channelPostRepository.findAll();
        assertThat(channelPosts).hasSize(databaseSizeBeforeCreate + 1);
        ChannelPost testChannelPost = channelPosts.get(channelPosts.size() - 1);
        assertThat(testChannelPost.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void getAllChannelPosts() throws Exception {
        // Initialize the database
        channelPostRepository.saveAndFlush(channelPost);

        // Get all the channelPosts
        restChannelPostMockMvc.perform(get("/api/channelPosts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(channelPost.getId().intValue())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getChannelPost() throws Exception {
        // Initialize the database
        channelPostRepository.saveAndFlush(channelPost);

        // Get the channelPost
        restChannelPostMockMvc.perform(get("/api/channelPosts/{id}", channelPost.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(channelPost.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingChannelPost() throws Exception {
        // Get the channelPost
        restChannelPostMockMvc.perform(get("/api/channelPosts/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChannelPost() throws Exception {
        // Initialize the database
        channelPostRepository.saveAndFlush(channelPost);
		
		int databaseSizeBeforeUpdate = channelPostRepository.findAll().size();

        // Update the channelPost
        channelPost.setStatus(UPDATED_STATUS);
        restChannelPostMockMvc.perform(put("/api/channelPosts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(channelPost)))
                .andExpect(status().isOk());

        // Validate the ChannelPost in the database
        List<ChannelPost> channelPosts = channelPostRepository.findAll();
        assertThat(channelPosts).hasSize(databaseSizeBeforeUpdate);
        ChannelPost testChannelPost = channelPosts.get(channelPosts.size() - 1);
        assertThat(testChannelPost.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void deleteChannelPost() throws Exception {
        // Initialize the database
        channelPostRepository.saveAndFlush(channelPost);
		
		int databaseSizeBeforeDelete = channelPostRepository.findAll().size();

        // Get the channelPost
        restChannelPostMockMvc.perform(delete("/api/channelPosts/{id}", channelPost.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ChannelPost> channelPosts = channelPostRepository.findAll();
        assertThat(channelPosts).hasSize(databaseSizeBeforeDelete - 1);
    }
}
