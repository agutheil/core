package com.mightymerce.core.web.rest;

import com.mightymerce.core.Application;
import com.mightymerce.core.domain.LegalInfo;
import com.mightymerce.core.repository.LegalInfoRepository;
import com.mightymerce.core.web.rest.dto.LegalInfoDTO;
import com.mightymerce.core.web.rest.mapper.LegalInfoMapper;

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
 * Test class for the LegalInfoResource REST controller.
 *
 * @see LegalInfoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class LegalInfoResourceTest {

    private static final String DEFAULT_TITLE = "SAMPLE_TEXT";
    private static final String UPDATED_TITLE = "UPDATED_TEXT";
    private static final String DEFAULT_PURPOSE = "SAMPLE_TEXT";
    private static final String UPDATED_PURPOSE = "UPDATED_TEXT";
    private static final String DEFAULT_PAGE_TITLE = "SAMPLE_TEXT";
    private static final String UPDATED_PAGE_TITLE = "UPDATED_TEXT";
    private static final String DEFAULT_PAGE_TEXT = "SAMPLE_TEXT";
    private static final String UPDATED_PAGE_TEXT = "UPDATED_TEXT";

    @Inject
    private LegalInfoRepository legalInfoRepository;

    @Inject
    private LegalInfoMapper legalInfoMapper;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restLegalInfoMockMvc;

    private LegalInfo legalInfo;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LegalInfoResource legalInfoResource = new LegalInfoResource();
        ReflectionTestUtils.setField(legalInfoResource, "legalInfoRepository", legalInfoRepository);
        ReflectionTestUtils.setField(legalInfoResource, "legalInfoMapper", legalInfoMapper);
        this.restLegalInfoMockMvc = MockMvcBuilders.standaloneSetup(legalInfoResource).setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        legalInfo = new LegalInfo();
        legalInfo.setTitle(DEFAULT_TITLE);
        legalInfo.setPurpose(DEFAULT_PURPOSE);
        legalInfo.setPageTitle(DEFAULT_PAGE_TITLE);
        legalInfo.setPageText(DEFAULT_PAGE_TEXT);
    }

    @Test
    @Transactional
    public void createLegalInfo() throws Exception {
        int databaseSizeBeforeCreate = legalInfoRepository.findAll().size();

        // Create the LegalInfo
        LegalInfoDTO legalInfoDTO = legalInfoMapper.legalInfoToLegalInfoDTO(legalInfo);

        restLegalInfoMockMvc.perform(post("/api/legalInfos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(legalInfoDTO)))
                .andExpect(status().isCreated());

        // Validate the LegalInfo in the database
        List<LegalInfo> legalInfos = legalInfoRepository.findAll();
        assertThat(legalInfos).hasSize(databaseSizeBeforeCreate + 1);
        LegalInfo testLegalInfo = legalInfos.get(legalInfos.size() - 1);
        assertThat(testLegalInfo.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testLegalInfo.getPurpose()).isEqualTo(DEFAULT_PURPOSE);
        assertThat(testLegalInfo.getPageTitle()).isEqualTo(DEFAULT_PAGE_TITLE);
        assertThat(testLegalInfo.getPageText()).isEqualTo(DEFAULT_PAGE_TEXT);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = legalInfoRepository.findAll().size();
        // set the field null
        legalInfo.setTitle(null);

        // Create the LegalInfo, which fails.
        LegalInfoDTO legalInfoDTO = legalInfoMapper.legalInfoToLegalInfoDTO(legalInfo);

        restLegalInfoMockMvc.perform(post("/api/legalInfos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(legalInfoDTO)))
                .andExpect(status().isBadRequest());

        List<LegalInfo> legalInfos = legalInfoRepository.findAll();
        assertThat(legalInfos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPurposeIsRequired() throws Exception {
        int databaseSizeBeforeTest = legalInfoRepository.findAll().size();
        // set the field null
        legalInfo.setPurpose(null);

        // Create the LegalInfo, which fails.
        LegalInfoDTO legalInfoDTO = legalInfoMapper.legalInfoToLegalInfoDTO(legalInfo);

        restLegalInfoMockMvc.perform(post("/api/legalInfos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(legalInfoDTO)))
                .andExpect(status().isBadRequest());

        List<LegalInfo> legalInfos = legalInfoRepository.findAll();
        assertThat(legalInfos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPageTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = legalInfoRepository.findAll().size();
        // set the field null
        legalInfo.setPageTitle(null);

        // Create the LegalInfo, which fails.
        LegalInfoDTO legalInfoDTO = legalInfoMapper.legalInfoToLegalInfoDTO(legalInfo);

        restLegalInfoMockMvc.perform(post("/api/legalInfos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(legalInfoDTO)))
                .andExpect(status().isBadRequest());

        List<LegalInfo> legalInfos = legalInfoRepository.findAll();
        assertThat(legalInfos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPageTextIsRequired() throws Exception {
        int databaseSizeBeforeTest = legalInfoRepository.findAll().size();
        // set the field null
        legalInfo.setPageText(null);

        // Create the LegalInfo, which fails.
        LegalInfoDTO legalInfoDTO = legalInfoMapper.legalInfoToLegalInfoDTO(legalInfo);

        restLegalInfoMockMvc.perform(post("/api/legalInfos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(legalInfoDTO)))
                .andExpect(status().isBadRequest());

        List<LegalInfo> legalInfos = legalInfoRepository.findAll();
        assertThat(legalInfos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLegalInfos() throws Exception {
        // Initialize the database
        legalInfoRepository.saveAndFlush(legalInfo);

        // Get all the legalInfos
        restLegalInfoMockMvc.perform(get("/api/legalInfos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(legalInfo.getId().intValue())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].purpose").value(hasItem(DEFAULT_PURPOSE.toString())))
                .andExpect(jsonPath("$.[*].pageTitle").value(hasItem(DEFAULT_PAGE_TITLE.toString())))
                .andExpect(jsonPath("$.[*].pageText").value(hasItem(DEFAULT_PAGE_TEXT.toString())));
    }

    @Test
    @Transactional
    public void getLegalInfo() throws Exception {
        // Initialize the database
        legalInfoRepository.saveAndFlush(legalInfo);

        // Get the legalInfo
        restLegalInfoMockMvc.perform(get("/api/legalInfos/{id}", legalInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(legalInfo.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.purpose").value(DEFAULT_PURPOSE.toString()))
            .andExpect(jsonPath("$.pageTitle").value(DEFAULT_PAGE_TITLE.toString()))
            .andExpect(jsonPath("$.pageText").value(DEFAULT_PAGE_TEXT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLegalInfo() throws Exception {
        // Get the legalInfo
        restLegalInfoMockMvc.perform(get("/api/legalInfos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLegalInfo() throws Exception {
        // Initialize the database
        legalInfoRepository.saveAndFlush(legalInfo);

		int databaseSizeBeforeUpdate = legalInfoRepository.findAll().size();

        // Update the legalInfo
        legalInfo.setTitle(UPDATED_TITLE);
        legalInfo.setPurpose(UPDATED_PURPOSE);
        legalInfo.setPageTitle(UPDATED_PAGE_TITLE);
        legalInfo.setPageText(UPDATED_PAGE_TEXT);
        
        LegalInfoDTO legalInfoDTO = legalInfoMapper.legalInfoToLegalInfoDTO(legalInfo);

        restLegalInfoMockMvc.perform(put("/api/legalInfos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(legalInfoDTO)))
                .andExpect(status().isOk());

        // Validate the LegalInfo in the database
        List<LegalInfo> legalInfos = legalInfoRepository.findAll();
        assertThat(legalInfos).hasSize(databaseSizeBeforeUpdate);
        LegalInfo testLegalInfo = legalInfos.get(legalInfos.size() - 1);
        assertThat(testLegalInfo.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testLegalInfo.getPurpose()).isEqualTo(UPDATED_PURPOSE);
        assertThat(testLegalInfo.getPageTitle()).isEqualTo(UPDATED_PAGE_TITLE);
        assertThat(testLegalInfo.getPageText()).isEqualTo(UPDATED_PAGE_TEXT);
    }

    @Test
    @Transactional
    public void deleteLegalInfo() throws Exception {
        // Initialize the database
        legalInfoRepository.saveAndFlush(legalInfo);

		int databaseSizeBeforeDelete = legalInfoRepository.findAll().size();

        // Get the legalInfo
        restLegalInfoMockMvc.perform(delete("/api/legalInfos/{id}", legalInfo.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<LegalInfo> legalInfos = legalInfoRepository.findAll();
        assertThat(legalInfos).hasSize(databaseSizeBeforeDelete - 1);
    }
}
