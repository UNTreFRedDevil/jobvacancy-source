package com.jobvacancy.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.jobvacancy.domain.Statistic;
import com.jobvacancy.repository.StatisticRepository;
import com.jobvacancy.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Statistic.
 */
@RestController
@RequestMapping("/api")
public class StatisticResource {

    private final Logger log = LoggerFactory.getLogger(StatisticResource.class);

    @Inject
    private StatisticRepository statisticRepository;

    /**
     * POST  /statistics -> Create a new statistic.
     */
    @RequestMapping(value = "/statistics",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Statistic> createStatistic(@RequestBody Statistic statistic) throws URISyntaxException {
        log.debug("REST request to save Statistic : {}", statistic);
        if (statistic.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new statistic cannot already have an ID").body(null);
        }
        Statistic result = statisticRepository.save(statistic);
        return ResponseEntity.created(new URI("/api/statistics/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("statistic", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /statistics -> Updates an existing statistic.
     */
    @RequestMapping(value = "/statistics",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Statistic> updateStatistic(@RequestBody Statistic statistic) throws URISyntaxException {
        log.debug("REST request to update Statistic : {}", statistic);
        if (statistic.getId() == null) {
            return createStatistic(statistic);
        }
        Statistic result = statisticRepository.save(statistic);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("statistic", statistic.getId().toString()))
                .body(result);
    }

    /**
     * GET  /statistics -> get all the statistics.
     */
    @RequestMapping(value = "/statistics",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Statistic> getAllStatistics() {
        log.debug("REST request to get all Statistics");
        return statisticRepository.findAll();
    }

    /**
     * GET  /statistics/:id -> get the "id" statistic.
     */
    @RequestMapping(value = "/statistics/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Statistic> getStatistic(@PathVariable Long id) {
        log.debug("REST request to get Statistic : {}", id);
        return Optional.ofNullable(statisticRepository.findOne(id))
            .map(statistic -> new ResponseEntity<>(
                statistic,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /statistics/:id -> delete the "id" statistic.
     */
    @RequestMapping(value = "/statistics/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteStatistic(@PathVariable Long id) {
        log.debug("REST request to delete Statistic : {}", id);
        statisticRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("statistic", id.toString())).build();
    }
}
