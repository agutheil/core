package com.mightymerce.core.web.rest;

import com.mightymerce.core.Application;
import com.mightymerce.core.domain.Address;
import com.mightymerce.core.repository.AddressRepository;

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
 * Test class for the AddressResource REST controller.
 *
 * @see AddressResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class AddressResourceTest {

    private static final String DEFAULT_STREET = "SAMPLE_TEXT";
    private static final String UPDATED_STREET = "UPDATED_TEXT";
    private static final String DEFAULT_NO = "SAMPLE_TEXT";
    private static final String UPDATED_NO = "UPDATED_TEXT";
    private static final String DEFAULT_ZIP = "SAMPLE_TEXT";
    private static final String UPDATED_ZIP = "UPDATED_TEXT";
    private static final String DEFAULT_CITY = "SAMPLE_TEXT";
    private static final String UPDATED_CITY = "UPDATED_TEXT";

    @Inject
    private AddressRepository addressRepository;

    private MockMvc restAddressMockMvc;

    private Address address;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AddressResource addressResource = new AddressResource();
        ReflectionTestUtils.setField(addressResource, "addressRepository", addressRepository);
        this.restAddressMockMvc = MockMvcBuilders.standaloneSetup(addressResource).build();
    }

    @Before
    public void initTest() {
        address = new Address();
        address.setStreet(DEFAULT_STREET);
        address.setNo(DEFAULT_NO);
        address.setZip(DEFAULT_ZIP);
        address.setCity(DEFAULT_CITY);
    }

    @Test
    @Transactional
    public void createAddress() throws Exception {
        int databaseSizeBeforeCreate = addressRepository.findAll().size();

        // Create the Address
        restAddressMockMvc.perform(post("/api/addresss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(address)))
                .andExpect(status().isCreated());

        // Validate the Address in the database
        List<Address> addresss = addressRepository.findAll();
        assertThat(addresss).hasSize(databaseSizeBeforeCreate + 1);
        Address testAddress = addresss.get(addresss.size() - 1);
        assertThat(testAddress.getStreet()).isEqualTo(DEFAULT_STREET);
        assertThat(testAddress.getNo()).isEqualTo(DEFAULT_NO);
        assertThat(testAddress.getZip()).isEqualTo(DEFAULT_ZIP);
        assertThat(testAddress.getCity()).isEqualTo(DEFAULT_CITY);
    }

    @Test
    @Transactional
    public void getAllAddresss() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addresss
        restAddressMockMvc.perform(get("/api/addresss"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(address.getId().intValue())))
                .andExpect(jsonPath("$.[*].street").value(hasItem(DEFAULT_STREET.toString())))
                .andExpect(jsonPath("$.[*].no").value(hasItem(DEFAULT_NO.toString())))
                .andExpect(jsonPath("$.[*].zip").value(hasItem(DEFAULT_ZIP.toString())))
                .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())));
    }

    @Test
    @Transactional
    public void getAddress() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get the address
        restAddressMockMvc.perform(get("/api/addresss/{id}", address.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(address.getId().intValue()))
            .andExpect(jsonPath("$.street").value(DEFAULT_STREET.toString()))
            .andExpect(jsonPath("$.no").value(DEFAULT_NO.toString()))
            .andExpect(jsonPath("$.zip").value(DEFAULT_ZIP.toString()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAddress() throws Exception {
        // Get the address
        restAddressMockMvc.perform(get("/api/addresss/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAddress() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);
		
		int databaseSizeBeforeUpdate = addressRepository.findAll().size();

        // Update the address
        address.setStreet(UPDATED_STREET);
        address.setNo(UPDATED_NO);
        address.setZip(UPDATED_ZIP);
        address.setCity(UPDATED_CITY);
        restAddressMockMvc.perform(put("/api/addresss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(address)))
                .andExpect(status().isOk());

        // Validate the Address in the database
        List<Address> addresss = addressRepository.findAll();
        assertThat(addresss).hasSize(databaseSizeBeforeUpdate);
        Address testAddress = addresss.get(addresss.size() - 1);
        assertThat(testAddress.getStreet()).isEqualTo(UPDATED_STREET);
        assertThat(testAddress.getNo()).isEqualTo(UPDATED_NO);
        assertThat(testAddress.getZip()).isEqualTo(UPDATED_ZIP);
        assertThat(testAddress.getCity()).isEqualTo(UPDATED_CITY);
    }

    @Test
    @Transactional
    public void deleteAddress() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);
		
		int databaseSizeBeforeDelete = addressRepository.findAll().size();

        // Get the address
        restAddressMockMvc.perform(delete("/api/addresss/{id}", address.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Address> addresss = addressRepository.findAll();
        assertThat(addresss).hasSize(databaseSizeBeforeDelete - 1);
    }
}
