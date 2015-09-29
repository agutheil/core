package com.mightymerce.core.web.rest;

import com.mightymerce.core.Application;
import com.mightymerce.core.domain.TutorialStep;
import com.mightymerce.core.repository.TutorialStepRepository;
import com.mightymerce.core.web.rest.dto.TutorialStepDTO;
import com.mightymerce.core.web.rest.mapper.TutorialStepMapper;

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
 * Test class for the TutorialStepResource REST controller.
 *
 * @see TutorialStepResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class TutorialStepResourceTest {

    private static final String DEFAULT_STEP = "SAMPLE_TEXT";
    private static final String UPDATED_STEP = "UPDATED_TEXT";

    private static final Boolean DEFAULT_COMPLETED = false;
    private static final Boolean UPDATED_COMPLETED = true;

    @Inject
    private TutorialStepRepository tutorialStepRepository;

    @Inject
    private TutorialStepMapper tutorialStepMapper;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restTutorialStepMockMvc;

    private TutorialStep tutorialStep;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TutorialStepResource tutorialStepResource = new TutorialStepResource();
        ReflectionTestUtils.setField(tutorialStepResource, "tutorialStepRepository", tutorialStepRepository);
        ReflectionTestUtils.setField(tutorialStepResource, "tutorialStepMapper", tutorialStepMapper);
        this.restTutorialStepMockMvc = MockMvcBuilders.standaloneSetup(tutorialStepResource).setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        tutorialStep = new TutorialStep();
        tutorialStep.setStep(DEFAULT_STEP);
        tutorialStep.setCompleted(DEFAULT_COMPLETED);
    }

    @Test
    @Transactional
    public void createTutorialStep() throws Exception {
        int databaseSizeBeforeCreate = tutorialStepRepository.findAll().size();

        // Create the TutorialStep
        TutorialStepDTO tutorialStepDTO = tutorialStepMapper.tutorialStepToTutorialStepDTO(tutorialStep);

        restTutorialStepMockMvc.perform(post("/api/tutorialSteps")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tutorialStepDTO)))
                .andExpect(status().isCreated());

        // Validate the TutorialStep in the database
        List<TutorialStep> tutorialSteps = tutorialStepRepository.findAll();
        assertThat(tutorialSteps).hasSize(databaseSizeBeforeCreate + 1);
        TutorialStep testTutorialStep = tutorialSteps.get(tutorialSteps.size() - 1);
        assertThat(testTutorialStep.getStep()).isEqualTo(DEFAULT_STEP);
        assertThat(testTutorialStep.getCompleted()).isEqualTo(DEFAULT_COMPLETED);
    }

    @Test
    @Transactional
    public void checkStepIsRequired() throws Exception {
        int databaseSizeBeforeTest = tutorialStepRepository.findAll().size();
        // set the field null
        tutorialStep.setStep(null);

        // Create the TutorialStep, which fails.
        TutorialStepDTO tutorialStepDTO = tutorialStepMapper.tutorialStepToTutorialStepDTO(tutorialStep);

        restTutorialStepMockMvc.perform(post("/api/tutorialSteps")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tutorialStepDTO)))
                .andExpect(status().isBadRequest());

        List<TutorialStep> tutorialSteps = tutorialStepRepository.findAll();
        assertThat(tutorialSteps).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTutorialSteps() throws Exception {
        // Initialize the database
        tutorialStepRepository.saveAndFlush(tutorialStep);

        // Get all the tutorialSteps
        restTutorialStepMockMvc.perform(get("/api/tutorialSteps"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(tutorialStep.getId().intValue())))
                .andExpect(jsonPath("$.[*].step").value(hasItem(DEFAULT_STEP.toString())))
                .andExpect(jsonPath("$.[*].completed").value(hasItem(DEFAULT_COMPLETED.booleanValue())));
    }

    @Test
    @Transactional
    public void getTutorialStep() throws Exception {
        // Initialize the database
        tutorialStepRepository.saveAndFlush(tutorialStep);

        // Get the tutorialStep
        restTutorialStepMockMvc.perform(get("/api/tutorialSteps/{id}", tutorialStep.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(tutorialStep.getId().intValue()))
            .andExpect(jsonPath("$.step").value(DEFAULT_STEP.toString()))
            .andExpect(jsonPath("$.completed").value(DEFAULT_COMPLETED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingTutorialStep() throws Exception {
        // Get the tutorialStep
        restTutorialStepMockMvc.perform(get("/api/tutorialSteps/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTutorialStep() throws Exception {
        // Initialize the database
        tutorialStepRepository.saveAndFlush(tutorialStep);

		int databaseSizeBeforeUpdate = tutorialStepRepository.findAll().size();

        // Update the tutorialStep
        tutorialStep.setStep(UPDATED_STEP);
        tutorialStep.setCompleted(UPDATED_COMPLETED);
        
        TutorialStepDTO tutorialStepDTO = tutorialStepMapper.tutorialStepToTutorialStepDTO(tutorialStep);

        restTutorialStepMockMvc.perform(put("/api/tutorialSteps")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tutorialStepDTO)))
                .andExpect(status().isOk());

        // Validate the TutorialStep in the database
        List<TutorialStep> tutorialSteps = tutorialStepRepository.findAll();
        assertThat(tutorialSteps).hasSize(databaseSizeBeforeUpdate);
        TutorialStep testTutorialStep = tutorialSteps.get(tutorialSteps.size() - 1);
        assertThat(testTutorialStep.getStep()).isEqualTo(UPDATED_STEP);
        assertThat(testTutorialStep.getCompleted()).isEqualTo(UPDATED_COMPLETED);
    }

    @Test
    @Transactional
    public void deleteTutorialStep() throws Exception {
        // Initialize the database
        tutorialStepRepository.saveAndFlush(tutorialStep);

		int databaseSizeBeforeDelete = tutorialStepRepository.findAll().size();

        // Get the tutorialStep
        restTutorialStepMockMvc.perform(delete("/api/tutorialSteps/{id}", tutorialStep.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<TutorialStep> tutorialSteps = tutorialStepRepository.findAll();
        assertThat(tutorialSteps).hasSize(databaseSizeBeforeDelete - 1);
    }
}
