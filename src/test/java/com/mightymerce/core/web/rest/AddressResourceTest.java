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

import com.mightymerce.core.domain.enumeration.Country;

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

    private static final String DEFAULT_ADDRESSEE = "SAMPLE_TEXT";
    private static final String UPDATED_ADDRESSEE = "UPDATED_TEXT";
    private static final String DEFAULT_DETAILED_DESCRIPTION = "SAMPLE_TEXT";
    private static final String UPDATED_DETAILED_DESCRIPTION = "UPDATED_TEXT";
    private static final String DEFAULT_STREETNAME = "SAMPLE_TEXT";
    private static final String UPDATED_STREETNAME = "UPDATED_TEXT";
    private static final String DEFAULT_HOUSENUMBER = "SAMPLE_TEXT";
    private static final String UPDATED_HOUSENUMBER = "UPDATED_TEXT";
    private static final String DEFAULT_POSTAL_CODE = "SAMPLE_TEXT";
    private static final String UPDATED_POSTAL_CODE = "UPDATED_TEXT";
    private static final String DEFAULT_TOWN = "SAMPLE_TEXT";
    private static final String UPDATED_TOWN = "UPDATED_TEXT";

    private static final Country DEFAULT_COUNTRY = Country.de;
    private static final Country UPDATED_COUNTRY = Country.us;

    @Inject
    private AddressRepository addressRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restAddressMockMvc;

    private Address address;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AddressResource addressResource = new AddressResource();
        ReflectionTestUtils.setField(addressResource, "addressRepository", addressRepository);
        this.restAddressMockMvc = MockMvcBuilders.standaloneSetup(addressResource).setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        address = new Address();
        address.setAddressee(DEFAULT_ADDRESSEE);
        address.setDetailedDescription(DEFAULT_DETAILED_DESCRIPTION);
        address.setStreetname(DEFAULT_STREETNAME);
        address.setHousenumber(DEFAULT_HOUSENUMBER);
        address.setPostalCode(DEFAULT_POSTAL_CODE);
        address.setTown(DEFAULT_TOWN);
        address.setCountry(DEFAULT_COUNTRY);
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
        assertThat(testAddress.getAddressee()).isEqualTo(DEFAULT_ADDRESSEE);
        assertThat(testAddress.getDetailedDescription()).isEqualTo(DEFAULT_DETAILED_DESCRIPTION);
        assertThat(testAddress.getStreetname()).isEqualTo(DEFAULT_STREETNAME);
        assertThat(testAddress.getHousenumber()).isEqualTo(DEFAULT_HOUSENUMBER);
        assertThat(testAddress.getPostalCode()).isEqualTo(DEFAULT_POSTAL_CODE);
        assertThat(testAddress.getTown()).isEqualTo(DEFAULT_TOWN);
        assertThat(testAddress.getCountry()).isEqualTo(DEFAULT_COUNTRY);
    }

    @Test
    @Transactional
    public void checkAddresseeIsRequired() throws Exception {
        int databaseSizeBeforeTest = addressRepository.findAll().size();
        // set the field null
        address.setAddressee(null);

        // Create the Address, which fails.

        restAddressMockMvc.perform(post("/api/addresss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(address)))
                .andExpect(status().isBadRequest());

        List<Address> addresss = addressRepository.findAll();
        assertThat(addresss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStreetnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = addressRepository.findAll().size();
        // set the field null
        address.setStreetname(null);

        // Create the Address, which fails.

        restAddressMockMvc.perform(post("/api/addresss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(address)))
                .andExpect(status().isBadRequest());

        List<Address> addresss = addressRepository.findAll();
        assertThat(addresss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkHousenumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = addressRepository.findAll().size();
        // set the field null
        address.setHousenumber(null);

        // Create the Address, which fails.

        restAddressMockMvc.perform(post("/api/addresss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(address)))
                .andExpect(status().isBadRequest());

        List<Address> addresss = addressRepository.findAll();
        assertThat(addresss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPostalCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = addressRepository.findAll().size();
        // set the field null
        address.setPostalCode(null);

        // Create the Address, which fails.

        restAddressMockMvc.perform(post("/api/addresss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(address)))
                .andExpect(status().isBadRequest());

        List<Address> addresss = addressRepository.findAll();
        assertThat(addresss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTownIsRequired() throws Exception {
        int databaseSizeBeforeTest = addressRepository.findAll().size();
        // set the field null
        address.setTown(null);

        // Create the Address, which fails.

        restAddressMockMvc.perform(post("/api/addresss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(address)))
                .andExpect(status().isBadRequest());

        List<Address> addresss = addressRepository.findAll();
        assertThat(addresss).hasSize(databaseSizeBeforeTest);
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
                .andExpect(jsonPath("$.[*].addressee").value(hasItem(DEFAULT_ADDRESSEE.toString())))
                .andExpect(jsonPath("$.[*].detailedDescription").value(hasItem(DEFAULT_DETAILED_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].streetname").value(hasItem(DEFAULT_STREETNAME.toString())))
                .andExpect(jsonPath("$.[*].housenumber").value(hasItem(DEFAULT_HOUSENUMBER.toString())))
                .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE.toString())))
                .andExpect(jsonPath("$.[*].town").value(hasItem(DEFAULT_TOWN.toString())))
                .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY.toString())));
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
            .andExpect(jsonPath("$.addressee").value(DEFAULT_ADDRESSEE.toString()))
            .andExpect(jsonPath("$.detailedDescription").value(DEFAULT_DETAILED_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.streetname").value(DEFAULT_STREETNAME.toString()))
            .andExpect(jsonPath("$.housenumber").value(DEFAULT_HOUSENUMBER.toString()))
            .andExpect(jsonPath("$.postalCode").value(DEFAULT_POSTAL_CODE.toString()))
            .andExpect(jsonPath("$.town").value(DEFAULT_TOWN.toString()))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY.toString()));
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
        address.setAddressee(UPDATED_ADDRESSEE);
        address.setDetailedDescription(UPDATED_DETAILED_DESCRIPTION);
        address.setStreetname(UPDATED_STREETNAME);
        address.setHousenumber(UPDATED_HOUSENUMBER);
        address.setPostalCode(UPDATED_POSTAL_CODE);
        address.setTown(UPDATED_TOWN);
        address.setCountry(UPDATED_COUNTRY);
        

        restAddressMockMvc.perform(put("/api/addresss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(address)))
                .andExpect(status().isOk());

        // Validate the Address in the database
        List<Address> addresss = addressRepository.findAll();
        assertThat(addresss).hasSize(databaseSizeBeforeUpdate);
        Address testAddress = addresss.get(addresss.size() - 1);
        assertThat(testAddress.getAddressee()).isEqualTo(UPDATED_ADDRESSEE);
        assertThat(testAddress.getDetailedDescription()).isEqualTo(UPDATED_DETAILED_DESCRIPTION);
        assertThat(testAddress.getStreetname()).isEqualTo(UPDATED_STREETNAME);
        assertThat(testAddress.getHousenumber()).isEqualTo(UPDATED_HOUSENUMBER);
        assertThat(testAddress.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
        assertThat(testAddress.getTown()).isEqualTo(UPDATED_TOWN);
        assertThat(testAddress.getCountry()).isEqualTo(UPDATED_COUNTRY);
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
