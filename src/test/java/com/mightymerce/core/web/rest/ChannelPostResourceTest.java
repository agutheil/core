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

import com.mightymerce.core.domain.enumeration.PublicationStatus;

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

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");


    private static final PublicationStatus DEFAULT_STATUS = PublicationStatus.pending;
    private static final PublicationStatus UPDATED_STATUS = PublicationStatus.published;

    private static final DateTime DEFAULT_PUBLICATION_DATE = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_PUBLICATION_DATE = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_PUBLICATION_DATE_STR = dateTimeFormatter.print(DEFAULT_PUBLICATION_DATE);
    private static final String DEFAULT_EXTERNAL_POST_KEY = "SAMPLE_TEXT";
    private static final String UPDATED_EXTERNAL_POST_KEY = "UPDATED_TEXT";

    @Inject
    private ChannelPostRepository channelPostRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restChannelPostMockMvc;

    private ChannelPost channelPost;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ChannelPostResource channelPostResource = new ChannelPostResource();
        ReflectionTestUtils.setField(channelPostResource, "channelPostRepository", channelPostRepository);
        this.restChannelPostMockMvc = MockMvcBuilders.standaloneSetup(channelPostResource).setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        channelPost = new ChannelPost();
        channelPost.setStatus(DEFAULT_STATUS);
        channelPost.setPublicationDate(DEFAULT_PUBLICATION_DATE);
        channelPost.setExternalPostKey(DEFAULT_EXTERNAL_POST_KEY);
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
        assertThat(testChannelPost.getPublicationDate().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_PUBLICATION_DATE);
        assertThat(testChannelPost.getExternalPostKey()).isEqualTo(DEFAULT_EXTERNAL_POST_KEY);
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
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
                .andExpect(jsonPath("$.[*].publicationDate").value(hasItem(DEFAULT_PUBLICATION_DATE_STR)))
                .andExpect(jsonPath("$.[*].externalPostKey").value(hasItem(DEFAULT_EXTERNAL_POST_KEY.toString())));
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
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.publicationDate").value(DEFAULT_PUBLICATION_DATE_STR))
            .andExpect(jsonPath("$.externalPostKey").value(DEFAULT_EXTERNAL_POST_KEY.toString()));
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
        channelPost.setPublicationDate(UPDATED_PUBLICATION_DATE);
        channelPost.setExternalPostKey(UPDATED_EXTERNAL_POST_KEY);
        

        restChannelPostMockMvc.perform(put("/api/channelPosts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(channelPost)))
                .andExpect(status().isOk());

        // Validate the ChannelPost in the database
        List<ChannelPost> channelPosts = channelPostRepository.findAll();
        assertThat(channelPosts).hasSize(databaseSizeBeforeUpdate);
        ChannelPost testChannelPost = channelPosts.get(channelPosts.size() - 1);
        assertThat(testChannelPost.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testChannelPost.getPublicationDate().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_PUBLICATION_DATE);
        assertThat(testChannelPost.getExternalPostKey()).isEqualTo(UPDATED_EXTERNAL_POST_KEY);
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
