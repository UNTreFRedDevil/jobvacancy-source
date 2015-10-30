package com.jobvacancy.web.rest;

import com.jobvacancy.Application;
import com.jobvacancy.domain.Statistic;
import com.jobvacancy.repository.StatisticRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
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
 * Test class for the StatisticResource REST controller.
 *
 * @see StatisticResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class StatisticResourceTest {

    private static final String DEFAULT_METRIC = "AAAAA";
    private static final String UPDATED_METRIC = "BBBBB";

    private static final Long DEFAULT_VALUE = 1L;
    private static final Long UPDATED_VALUE = 2L;

    @Inject
    private StatisticRepository statisticRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restStatisticMockMvc;

    private Statistic statistic;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StatisticResource statisticResource = new StatisticResource();
        ReflectionTestUtils.setField(statisticResource, "statisticRepository", statisticRepository);
        this.restStatisticMockMvc = MockMvcBuilders.standaloneSetup(statisticResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        statistic = new Statistic();
        statistic.setMetric(DEFAULT_METRIC);
        statistic.setValue(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void createStatistic() throws Exception {
        int databaseSizeBeforeCreate = statisticRepository.findAll().size();

        // Create the Statistic

        restStatisticMockMvc.perform(post("/api/statistics")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(statistic)))
                .andExpect(status().isCreated());

        // Validate the Statistic in the database
        List<Statistic> statistics = statisticRepository.findAll();
        assertThat(statistics).hasSize(databaseSizeBeforeCreate + 1);
        Statistic testStatistic = statistics.get(statistics.size() - 1);
        assertThat(testStatistic.getMetric()).isEqualTo(DEFAULT_METRIC);
        assertThat(testStatistic.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void getAllStatistics() throws Exception {
        // Initialize the database
        statisticRepository.saveAndFlush(statistic);

        // Get all the statistics
        restStatisticMockMvc.perform(get("/api/statistics"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(statistic.getId().intValue())))
                .andExpect(jsonPath("$.[*].metric").value(hasItem(DEFAULT_METRIC.toString())))
                .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.intValue())));
    }

    @Test
    @Transactional
    public void getStatistic() throws Exception {
        // Initialize the database
        statisticRepository.saveAndFlush(statistic);

        // Get the statistic
        restStatisticMockMvc.perform(get("/api/statistics/{id}", statistic.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(statistic.getId().intValue()))
            .andExpect(jsonPath("$.metric").value(DEFAULT_METRIC.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingStatistic() throws Exception {
        // Get the statistic
        restStatisticMockMvc.perform(get("/api/statistics/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStatistic() throws Exception {
        // Initialize the database
        statisticRepository.saveAndFlush(statistic);

		int databaseSizeBeforeUpdate = statisticRepository.findAll().size();

        // Update the statistic
        statistic.setMetric(UPDATED_METRIC);
        statistic.setValue(UPDATED_VALUE);

        restStatisticMockMvc.perform(put("/api/statistics")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(statistic)))
                .andExpect(status().isOk());

        // Validate the Statistic in the database
        List<Statistic> statistics = statisticRepository.findAll();
        assertThat(statistics).hasSize(databaseSizeBeforeUpdate);
        Statistic testStatistic = statistics.get(statistics.size() - 1);
        assertThat(testStatistic.getMetric()).isEqualTo(UPDATED_METRIC);
        assertThat(testStatistic.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void deleteStatistic() throws Exception {
        // Initialize the database
        statisticRepository.saveAndFlush(statistic);

		int databaseSizeBeforeDelete = statisticRepository.findAll().size();

        // Get the statistic
        restStatisticMockMvc.perform(delete("/api/statistics/{id}", statistic.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Statistic> statistics = statisticRepository.findAll();
        assertThat(statistics).hasSize(databaseSizeBeforeDelete - 1);
    }
}
