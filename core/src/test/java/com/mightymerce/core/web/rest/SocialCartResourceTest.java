package com.mightymerce.core.web.rest;

import com.mightymerce.core.Application;
import com.mightymerce.core.domain.SocialCart;
import com.mightymerce.core.repository.SocialCartRepository;

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
 * Test class for the SocialCartResource REST controller.
 *
 * @see SocialCartResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class SocialCartResourceTest {


    @Inject
    private SocialCartRepository socialCartRepository;

    private MockMvc restSocialCartMockMvc;

    private SocialCart socialCart;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SocialCartResource socialCartResource = new SocialCartResource();
        ReflectionTestUtils.setField(socialCartResource, "socialCartRepository", socialCartRepository);
        this.restSocialCartMockMvc = MockMvcBuilders.standaloneSetup(socialCartResource).build();
    }

    @Before
    public void initTest() {
        socialCart = new SocialCart();
    }

    @Test
    @Transactional
    public void createSocialCart() throws Exception {
        int databaseSizeBeforeCreate = socialCartRepository.findAll().size();

        // Create the SocialCart
        restSocialCartMockMvc.perform(post("/api/socialCarts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(socialCart)))
                .andExpect(status().isCreated());

        // Validate the SocialCart in the database
        List<SocialCart> socialCarts = socialCartRepository.findAll();
        assertThat(socialCarts).hasSize(databaseSizeBeforeCreate + 1);
        SocialCart testSocialCart = socialCarts.get(socialCarts.size() - 1);
    }

    @Test
    @Transactional
    public void getAllSocialCarts() throws Exception {
        // Initialize the database
        socialCartRepository.saveAndFlush(socialCart);

        // Get all the socialCarts
        restSocialCartMockMvc.perform(get("/api/socialCarts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(socialCart.getId().intValue())));
    }

    @Test
    @Transactional
    public void getSocialCart() throws Exception {
        // Initialize the database
        socialCartRepository.saveAndFlush(socialCart);

        // Get the socialCart
        restSocialCartMockMvc.perform(get("/api/socialCarts/{id}", socialCart.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(socialCart.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSocialCart() throws Exception {
        // Get the socialCart
        restSocialCartMockMvc.perform(get("/api/socialCarts/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSocialCart() throws Exception {
        // Initialize the database
        socialCartRepository.saveAndFlush(socialCart);
		
		int databaseSizeBeforeUpdate = socialCartRepository.findAll().size();

        // Update the socialCart
        restSocialCartMockMvc.perform(put("/api/socialCarts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(socialCart)))
                .andExpect(status().isOk());

        // Validate the SocialCart in the database
        List<SocialCart> socialCarts = socialCartRepository.findAll();
        assertThat(socialCarts).hasSize(databaseSizeBeforeUpdate);
        SocialCart testSocialCart = socialCarts.get(socialCarts.size() - 1);
    }

    @Test
    @Transactional
    public void deleteSocialCart() throws Exception {
        // Initialize the database
        socialCartRepository.saveAndFlush(socialCart);
		
		int databaseSizeBeforeDelete = socialCartRepository.findAll().size();

        // Get the socialCart
        restSocialCartMockMvc.perform(delete("/api/socialCarts/{id}", socialCart.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<SocialCart> socialCarts = socialCartRepository.findAll();
        assertThat(socialCarts).hasSize(databaseSizeBeforeDelete - 1);
    }
}
