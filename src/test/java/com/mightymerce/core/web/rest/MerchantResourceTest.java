package com.mightymerce.core.web.rest;

import com.mightymerce.core.Application;
import com.mightymerce.core.domain.Merchant;
import com.mightymerce.core.repository.MerchantRepository;

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
 * Test class for the MerchantResource REST controller.
 *
 * @see MerchantResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class MerchantResourceTest {

    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";
    private static final String DEFAULT_PRENAME = "SAMPLE_TEXT";
    private static final String UPDATED_PRENAME = "UPDATED_TEXT";
    private static final String DEFAULT_FIRMA = "SAMPLE_TEXT";
    private static final String UPDATED_FIRMA = "UPDATED_TEXT";

    @Inject
    private MerchantRepository merchantRepository;

    private MockMvc restMerchantMockMvc;

    private Merchant merchant;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MerchantResource merchantResource = new MerchantResource();
        ReflectionTestUtils.setField(merchantResource, "merchantRepository", merchantRepository);
        this.restMerchantMockMvc = MockMvcBuilders.standaloneSetup(merchantResource).build();
    }

    @Before
    public void initTest() {
        merchant = new Merchant();
        merchant.setName(DEFAULT_NAME);
        merchant.setPrename(DEFAULT_PRENAME);
        merchant.setFirma(DEFAULT_FIRMA);
    }

    @Test
    @Transactional
    public void createMerchant() throws Exception {
        int databaseSizeBeforeCreate = merchantRepository.findAll().size();

        // Create the Merchant
        restMerchantMockMvc.perform(post("/api/merchants")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(merchant)))
                .andExpect(status().isCreated());

        // Validate the Merchant in the database
        List<Merchant> merchants = merchantRepository.findAll();
        assertThat(merchants).hasSize(databaseSizeBeforeCreate + 1);
        Merchant testMerchant = merchants.get(merchants.size() - 1);
        assertThat(testMerchant.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMerchant.getPrename()).isEqualTo(DEFAULT_PRENAME);
        assertThat(testMerchant.getFirma()).isEqualTo(DEFAULT_FIRMA);
    }

    @Test
    @Transactional
    public void getAllMerchants() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchants
        restMerchantMockMvc.perform(get("/api/merchants"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(merchant.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].prename").value(hasItem(DEFAULT_PRENAME.toString())))
                .andExpect(jsonPath("$.[*].firma").value(hasItem(DEFAULT_FIRMA.toString())));
    }

    @Test
    @Transactional
    public void getMerchant() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get the merchant
        restMerchantMockMvc.perform(get("/api/merchants/{id}", merchant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(merchant.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.prename").value(DEFAULT_PRENAME.toString()))
            .andExpect(jsonPath("$.firma").value(DEFAULT_FIRMA.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMerchant() throws Exception {
        // Get the merchant
        restMerchantMockMvc.perform(get("/api/merchants/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMerchant() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);
		
		int databaseSizeBeforeUpdate = merchantRepository.findAll().size();

        // Update the merchant
        merchant.setName(UPDATED_NAME);
        merchant.setPrename(UPDATED_PRENAME);
        merchant.setFirma(UPDATED_FIRMA);
        restMerchantMockMvc.perform(put("/api/merchants")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(merchant)))
                .andExpect(status().isOk());

        // Validate the Merchant in the database
        List<Merchant> merchants = merchantRepository.findAll();
        assertThat(merchants).hasSize(databaseSizeBeforeUpdate);
        Merchant testMerchant = merchants.get(merchants.size() - 1);
        assertThat(testMerchant.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMerchant.getPrename()).isEqualTo(UPDATED_PRENAME);
        assertThat(testMerchant.getFirma()).isEqualTo(UPDATED_FIRMA);
    }

    @Test
    @Transactional
    public void deleteMerchant() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);
		
		int databaseSizeBeforeDelete = merchantRepository.findAll().size();

        // Get the merchant
        restMerchantMockMvc.perform(delete("/api/merchants/{id}", merchant.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Merchant> merchants = merchantRepository.findAll();
        assertThat(merchants).hasSize(databaseSizeBeforeDelete - 1);
    }
}
