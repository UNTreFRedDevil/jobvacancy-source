package com.jobvacancy.web.rest;

import com.jobvacancy.Application;
import com.jobvacancy.domain.JobOffer;
import com.jobvacancy.domain.Statistic;
import com.jobvacancy.domain.User;
import com.jobvacancy.domain.enumeration.JobOfferStatus;
import com.jobvacancy.repository.JobOfferRepository;
import com.jobvacancy.repository.StatisticRepository;
import com.jobvacancy.repository.UserRepository;
import org.apache.commons.lang.time.DateUtils;
import org.joda.time.DateTimeComparator;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the JobOfferResource REST controller.
 *
 * @see JobOfferResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class JobOfferResourceTest {

    private static final String DEFAULT_TITLE = "SAMPLE_TEXT";
    private static final String UPDATED_TITLE = "UPDATED_TEXT";
    private static final String DEFAULT_LOCATION = "SAMPLE_TEXT";
    private static final String UPDATED_LOCATION = "UPDATED_TEXT";
    private static final String DEFAULT_DESCRIPTION = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRIPTION = "UPDATED_TEXT";
    private static final JobOfferStatus DEFAULT_STATUS = JobOfferStatus.AVAILABLE;
    private static final JobOfferStatus UPDATED_STATUS = JobOfferStatus.CANCELED;

    @Inject
    private PasswordEncoder passwordEncoder;

    @Inject
    private UserRepository userRepository;

    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private StatisticRepository mockStatisticRepository;

    @Inject
    private StatisticRepository statisticRepository;

    @Inject
    private JobOfferRepository jobOfferRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restJobOfferMockMvc;

    private JobOffer jobOffer;

    private User user;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        JobOfferResource jobOfferResource = new JobOfferResource();
        ReflectionTestUtils.setField(jobOfferResource, "jobOfferRepository", jobOfferRepository);

        // TODO: this should be refactored in a based class because is a common concern
        Optional<User> user = userRepository.findOneByLogin("user");
        when(mockUserRepository.findOneByLogin(Mockito.any())).thenReturn(user);

        Statistic statistic = statisticRepository.getPublishedJobOffers();
        when(mockStatisticRepository.getPublishedJobOffers()).thenReturn(statistic);

        ReflectionTestUtils.setField(jobOfferResource, "statisticRepository", mockStatisticRepository);
        ReflectionTestUtils.setField(jobOfferResource, "userRepository", mockUserRepository);

        restJobOfferMockMvc = MockMvcBuilders.standaloneSetup(jobOfferResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter)
            .build();
    }


    @Before
    public void initTest() {
        jobOffer = new JobOffer();
        jobOffer.setTitle(DEFAULT_TITLE);
        jobOffer.setLocation(DEFAULT_LOCATION);
        jobOffer.setDescription(DEFAULT_DESCRIPTION);
        jobOffer.setStatus(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void whenAJobOfferisDefinedWithoutAStartDateItShouldBeCreatedOkAndWithTodaysDate() throws Exception {
        int databaseSizeBeforeCreate = jobOfferRepository.findAll().size();
        Date today = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());

        // Create the JobOffer
        restJobOfferMockMvc.perform(post("/api/jobOffers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobOffer)))
            .andExpect(status().isCreated());

        // Validate the JobOffer in the database
        List<JobOffer> jobOffers = jobOfferRepository.findAll();
        assertThat(jobOffers).hasSize(databaseSizeBeforeCreate + 1);
        JobOffer testJobOffer = jobOffers.get(jobOffers.size() - 1);
        assertThat(testJobOffer.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testJobOffer.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testJobOffer.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testJobOffer.getStatus()).isEqualTo(DEFAULT_STATUS);
        int dateComparation = DateTimeComparator.getDateOnlyInstance().compare(today, testJobOffer.getStartDate());
        assertThat(dateComparation).isEqualTo(0);
    }

    @Test
    @Transactional
    public void whenAJobOfferisDefinedWithAStartDateInTheFutureItShouldBeCreatedOk() throws Exception {
        int databaseSizeBeforeCreate = jobOfferRepository.findAll().size();

        //Set the start date in the future
        Date today = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date tomorrow = new Date(today.getTime() + TimeUnit.DAYS.toMillis(1));
        jobOffer.setStartDate(tomorrow);
        // Create the JobOffer
        restJobOfferMockMvc.perform(post("/api/jobOffers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobOffer)))
            .andExpect(status().isCreated());

        // Validate the JobOffer in the database
        List<JobOffer> jobOffers = jobOfferRepository.findAll();
        assertThat(jobOffers).hasSize(databaseSizeBeforeCreate + 1);
        JobOffer testJobOffer = jobOffers.get(jobOffers.size() - 1);
        assertThat(testJobOffer.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testJobOffer.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testJobOffer.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testJobOffer.getStatus()).isEqualTo(DEFAULT_STATUS);
        int dateComparation = DateTimeComparator.getDateOnlyInstance().compare(tomorrow, testJobOffer.getStartDate());
        assertThat(dateComparation).isEqualTo(0);
    }

    @Test
    @Transactional
    public void whenAJobOfferisDefinedWithAStartDateInThePastItShouldNotBeCreatedAndShouldReturnBadRequest() throws Exception {
        int databaseSizeBeforeCreate = jobOfferRepository.findAll().size();

        //Set the start date in the past
        Date today = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date yesterday = new Date(today.getTime() - TimeUnit.DAYS.toMillis(1));
        jobOffer.setStartDate(yesterday);
        // Create the JobOffer
        restJobOfferMockMvc.perform(post("/api/jobOffers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobOffer)))
            .andExpect(status().isBadRequest());

        // Validate the JobOffer in the database
        List<JobOffer> jobOffers = jobOfferRepository.findAll();
        assertThat(jobOffers).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void whenAJobOfferisDefinedWithoutAnEndDateItShouldBeCreatedOkAndWithaDateWhithinaMonth() throws Exception {
        int databaseSizeBeforeCreate = jobOfferRepository.findAll().size();
        Date today = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date todayPlusOneMonth = DateUtils.addMonths(today, 1);

        // Create the JobOffer
        restJobOfferMockMvc.perform(post("/api/jobOffers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobOffer)))
            .andExpect(status().isCreated());

        // Validate the JobOffer in the database
        List<JobOffer> jobOffers = jobOfferRepository.findAll();
        assertThat(jobOffers).hasSize(databaseSizeBeforeCreate + 1);
        JobOffer testJobOffer = jobOffers.get(jobOffers.size() - 1);
        assertThat(testJobOffer.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testJobOffer.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testJobOffer.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        int dateComparation = DateTimeComparator.getDateOnlyInstance().compare(todayPlusOneMonth, testJobOffer.getEndDate());
        assertThat(dateComparation).isEqualTo(0);
    }


    @Test
    @Transactional
    public void whenAJobOfferisDefinedWithAnEndDateInThePastItShouldNotBeCreatedAndShouldReturnBadRequest() throws Exception {
        int databaseSizeBeforeCreate = jobOfferRepository.findAll().size();

        //Set the start date in the past
        Date today = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date yesterday = new Date(today.getTime() - TimeUnit.DAYS.toMillis( 1 ));
        jobOffer.setEndDate(yesterday);
        // Create the JobOffer
        restJobOfferMockMvc.perform(post("/api/jobOffers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobOffer)))
            .andExpect(status().isBadRequest());

        // Validate the JobOffer in the database
        List<JobOffer> jobOffers = jobOfferRepository.findAll();
        assertThat(jobOffers).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void whenAJobOfferisDefinedWithnEndDateInTheFutureItShouldBeCreatedOk() throws Exception {
        int databaseSizeBeforeCreate = jobOfferRepository.findAll().size();

        //Set the start date in the future
        Date today = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date tomorrow = new Date(today.getTime() + TimeUnit.DAYS.toMillis( 1 ));
        jobOffer.setEndDate(tomorrow);
        // Create the JobOffer
        restJobOfferMockMvc.perform(post("/api/jobOffers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobOffer)))
            .andExpect(status().isCreated());

        // Validate the JobOffer in the database
        List<JobOffer> jobOffers = jobOfferRepository.findAll();
        assertThat(jobOffers).hasSize(databaseSizeBeforeCreate + 1);
        JobOffer testJobOffer = jobOffers.get(jobOffers.size() - 1);
        assertThat(testJobOffer.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testJobOffer.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testJobOffer.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        int dateComparation = DateTimeComparator.getDateOnlyInstance().compare(tomorrow, testJobOffer.getEndDate());
        assertThat(dateComparation).isEqualTo(0);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = jobOfferRepository.findAll().size();
        // set the field null
        jobOffer.setTitle(null);

        // Create the JobOffer, which fails.

        restJobOfferMockMvc.perform(post("/api/jobOffers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobOffer)))
            .andExpect(status().isBadRequest());

        List<JobOffer> jobOffers = jobOfferRepository.findAll();
        assertThat(jobOffers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Ignore
    @Transactional
    public void getAllJobOffers() throws Exception {
        // Initialize the database
        jobOfferRepository.saveAndFlush(jobOffer);

        // Get all the jobOffers
        restJobOfferMockMvc.perform(get("/api/jobOffers"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobOffer.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getJobOffer() throws Exception {
        // Initialize the database
        jobOfferRepository.saveAndFlush(jobOffer);

        // Get the jobOffer
        restJobOfferMockMvc.perform(get("/api/jobOffers/{id}", jobOffer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(jobOffer.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingJobOffer() throws Exception {
        // Get the jobOffer
        restJobOfferMockMvc.perform(get("/api/jobOffers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJobOffer() throws Exception {
        // Initialize the database
        jobOfferRepository.saveAndFlush(jobOffer);

        int databaseSizeBeforeUpdate = jobOfferRepository.findAll().size();

        // Update the jobOffer
        jobOffer.setTitle(UPDATED_TITLE);
        jobOffer.setLocation(UPDATED_LOCATION);
        jobOffer.setDescription(UPDATED_DESCRIPTION);
        jobOffer.setStatus(UPDATED_STATUS);

        restJobOfferMockMvc.perform(put("/api/jobOffers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobOffer)))
            .andExpect(status().isOk());

        // Validate the JobOffer in the database
        List<JobOffer> jobOffers = jobOfferRepository.findAll();
        assertThat(jobOffers).hasSize(databaseSizeBeforeUpdate);
        JobOffer testJobOffer = jobOffers.get(jobOffers.size() - 1);
        assertThat(testJobOffer.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testJobOffer.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testJobOffer.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testJobOffer.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void deleteJobOffer() throws Exception {
        // Initialize the database
        jobOfferRepository.saveAndFlush(jobOffer);

        int databaseSizeBeforeDelete = jobOfferRepository.findAll().size();

        // Get the jobOffer
        restJobOfferMockMvc.perform(delete("/api/jobOffers/{id}", jobOffer.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<JobOffer> jobOffers = jobOfferRepository.findAll();
        assertThat(jobOffers).hasSize(databaseSizeBeforeDelete - 1);
    }

    public static class MockSecurityContext implements SecurityContext {

        private static final long serialVersionUID = -1386535243513362694L;

        private Authentication authentication;

        public MockSecurityContext(Authentication authentication) {
            this.authentication = authentication;
        }

        @Override
        public Authentication getAuthentication() {
            return this.authentication;
        }

        @Override
        public void setAuthentication(Authentication authentication) {
            this.authentication = authentication;
        }
    }
}
