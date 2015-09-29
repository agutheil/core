package com.mightymerce.core.web.rest;

import com.mightymerce.core.Application;
import com.mightymerce.core.domain.Notification;
import com.mightymerce.core.repository.NotificationRepository;
import com.mightymerce.core.web.rest.dto.NotificationDTO;
import com.mightymerce.core.web.rest.mapper.NotificationMapper;

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
 * Test class for the NotificationResource REST controller.
 *
 * @see NotificationResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class NotificationResourceTest {

    private static final String DEFAULT_TITLE = "SAMPLE_TEXT";
    private static final String UPDATED_TITLE = "UPDATED_TEXT";
    private static final String DEFAULT_PURPOSE = "SAMPLE_TEXT";
    private static final String UPDATED_PURPOSE = "UPDATED_TEXT";
    private static final String DEFAULT_EMAIL_SUBJECT = "SAMPLE_TEXT";
    private static final String UPDATED_EMAIL_SUBJECT = "UPDATED_TEXT";
    private static final String DEFAULT_EMAIL_BODY = "SAMPLE_TEXT";
    private static final String UPDATED_EMAIL_BODY = "UPDATED_TEXT";

    @Inject
    private NotificationRepository notificationRepository;

    @Inject
    private NotificationMapper notificationMapper;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restNotificationMockMvc;

    private Notification notification;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        NotificationResource notificationResource = new NotificationResource();
        ReflectionTestUtils.setField(notificationResource, "notificationRepository", notificationRepository);
        ReflectionTestUtils.setField(notificationResource, "notificationMapper", notificationMapper);
        this.restNotificationMockMvc = MockMvcBuilders.standaloneSetup(notificationResource).setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        notification = new Notification();
        notification.setTitle(DEFAULT_TITLE);
        notification.setPurpose(DEFAULT_PURPOSE);
        notification.setEmailSubject(DEFAULT_EMAIL_SUBJECT);
        notification.setEmailBody(DEFAULT_EMAIL_BODY);
    }

    @Test
    @Transactional
    public void createNotification() throws Exception {
        int databaseSizeBeforeCreate = notificationRepository.findAll().size();

        // Create the Notification
        NotificationDTO notificationDTO = notificationMapper.notificationToNotificationDTO(notification);

        restNotificationMockMvc.perform(post("/api/notifications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(notificationDTO)))
                .andExpect(status().isCreated());

        // Validate the Notification in the database
        List<Notification> notifications = notificationRepository.findAll();
        assertThat(notifications).hasSize(databaseSizeBeforeCreate + 1);
        Notification testNotification = notifications.get(notifications.size() - 1);
        assertThat(testNotification.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testNotification.getPurpose()).isEqualTo(DEFAULT_PURPOSE);
        assertThat(testNotification.getEmailSubject()).isEqualTo(DEFAULT_EMAIL_SUBJECT);
        assertThat(testNotification.getEmailBody()).isEqualTo(DEFAULT_EMAIL_BODY);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = notificationRepository.findAll().size();
        // set the field null
        notification.setTitle(null);

        // Create the Notification, which fails.
        NotificationDTO notificationDTO = notificationMapper.notificationToNotificationDTO(notification);

        restNotificationMockMvc.perform(post("/api/notifications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(notificationDTO)))
                .andExpect(status().isBadRequest());

        List<Notification> notifications = notificationRepository.findAll();
        assertThat(notifications).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPurposeIsRequired() throws Exception {
        int databaseSizeBeforeTest = notificationRepository.findAll().size();
        // set the field null
        notification.setPurpose(null);

        // Create the Notification, which fails.
        NotificationDTO notificationDTO = notificationMapper.notificationToNotificationDTO(notification);

        restNotificationMockMvc.perform(post("/api/notifications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(notificationDTO)))
                .andExpect(status().isBadRequest());

        List<Notification> notifications = notificationRepository.findAll();
        assertThat(notifications).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailSubjectIsRequired() throws Exception {
        int databaseSizeBeforeTest = notificationRepository.findAll().size();
        // set the field null
        notification.setEmailSubject(null);

        // Create the Notification, which fails.
        NotificationDTO notificationDTO = notificationMapper.notificationToNotificationDTO(notification);

        restNotificationMockMvc.perform(post("/api/notifications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(notificationDTO)))
                .andExpect(status().isBadRequest());

        List<Notification> notifications = notificationRepository.findAll();
        assertThat(notifications).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailBodyIsRequired() throws Exception {
        int databaseSizeBeforeTest = notificationRepository.findAll().size();
        // set the field null
        notification.setEmailBody(null);

        // Create the Notification, which fails.
        NotificationDTO notificationDTO = notificationMapper.notificationToNotificationDTO(notification);

        restNotificationMockMvc.perform(post("/api/notifications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(notificationDTO)))
                .andExpect(status().isBadRequest());

        List<Notification> notifications = notificationRepository.findAll();
        assertThat(notifications).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllNotifications() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notifications
        restNotificationMockMvc.perform(get("/api/notifications"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(notification.getId().intValue())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].purpose").value(hasItem(DEFAULT_PURPOSE.toString())))
                .andExpect(jsonPath("$.[*].emailSubject").value(hasItem(DEFAULT_EMAIL_SUBJECT.toString())))
                .andExpect(jsonPath("$.[*].emailBody").value(hasItem(DEFAULT_EMAIL_BODY.toString())));
    }

    @Test
    @Transactional
    public void getNotification() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get the notification
        restNotificationMockMvc.perform(get("/api/notifications/{id}", notification.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(notification.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.purpose").value(DEFAULT_PURPOSE.toString()))
            .andExpect(jsonPath("$.emailSubject").value(DEFAULT_EMAIL_SUBJECT.toString()))
            .andExpect(jsonPath("$.emailBody").value(DEFAULT_EMAIL_BODY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingNotification() throws Exception {
        // Get the notification
        restNotificationMockMvc.perform(get("/api/notifications/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNotification() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

		int databaseSizeBeforeUpdate = notificationRepository.findAll().size();

        // Update the notification
        notification.setTitle(UPDATED_TITLE);
        notification.setPurpose(UPDATED_PURPOSE);
        notification.setEmailSubject(UPDATED_EMAIL_SUBJECT);
        notification.setEmailBody(UPDATED_EMAIL_BODY);
        
        NotificationDTO notificationDTO = notificationMapper.notificationToNotificationDTO(notification);

        restNotificationMockMvc.perform(put("/api/notifications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(notificationDTO)))
                .andExpect(status().isOk());

        // Validate the Notification in the database
        List<Notification> notifications = notificationRepository.findAll();
        assertThat(notifications).hasSize(databaseSizeBeforeUpdate);
        Notification testNotification = notifications.get(notifications.size() - 1);
        assertThat(testNotification.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testNotification.getPurpose()).isEqualTo(UPDATED_PURPOSE);
        assertThat(testNotification.getEmailSubject()).isEqualTo(UPDATED_EMAIL_SUBJECT);
        assertThat(testNotification.getEmailBody()).isEqualTo(UPDATED_EMAIL_BODY);
    }

    @Test
    @Transactional
    public void deleteNotification() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

		int databaseSizeBeforeDelete = notificationRepository.findAll().size();

        // Get the notification
        restNotificationMockMvc.perform(delete("/api/notifications/{id}", notification.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Notification> notifications = notificationRepository.findAll();
        assertThat(notifications).hasSize(databaseSizeBeforeDelete - 1);
    }
}
