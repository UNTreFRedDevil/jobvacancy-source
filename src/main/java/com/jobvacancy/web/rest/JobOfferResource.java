package com.jobvacancy.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.jobvacancy.domain.JobOffer;
import com.jobvacancy.domain.Statistic;
import com.jobvacancy.domain.User;
import com.jobvacancy.domain.enumeration.JobOfferStatus;
import com.jobvacancy.repository.JobOfferRepository;
import com.jobvacancy.repository.StatisticRepository;
import com.jobvacancy.repository.UserRepository;
import com.jobvacancy.security.SecurityUtils;
import com.jobvacancy.web.rest.util.HeaderUtil;
import com.jobvacancy.web.rest.util.PaginationUtil;
import org.apache.commons.lang.time.DateUtils;
import org.joda.time.DateTimeComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing JobOffer.
 */
@RestController
@RequestMapping("/api")
public class JobOfferResource {

    private static final Long INITIAL_APPLICATIONS_COUNT = 0L;

    private static final JobOfferStatus INITIAL_STATUS = JobOfferStatus.AVAILABLE;

    private final Logger log = LoggerFactory.getLogger(JobOfferResource.class);

    @Inject
    private JobOfferRepository jobOfferRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private StatisticRepository statisticRepository;

    /**
     * POST  /jobOffers -> Create a new jobOffer.
     */
    @RequestMapping(value = "/jobOffers",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<JobOffer> createJobOffer(@Valid @RequestBody JobOffer jobOffer) throws URISyntaxException {
        log.debug("REST request to save JobOffer : {}", jobOffer);

        Date today = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());

        if (jobOffer.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new jobOffer cannot already have an ID").body(null);
        }

        if (jobOffer.getStartDate() == null) {
            jobOffer.setStartDate(today);
        }

        if (jobOffer.getEndDate() == null) {
            jobOffer.setEndDate(DateUtils.addMonths(today, 1));
        }

        int startDateComparation = DateTimeComparator.getDateOnlyInstance().compare(jobOffer.getStartDate(), today);
        int endDateComparation = DateTimeComparator.getDateOnlyInstance().compare(jobOffer.getEndDate(), today);
        int startAndEndDateComparation = DateTimeComparator.getDateOnlyInstance().compare(jobOffer.getEndDate(), jobOffer.getStartDate());

        if (startDateComparation < 0) {
            return ResponseEntity.badRequest().header("Failure", "A jobOffers start date cannot be in the past").body(null);
        }

        if (endDateComparation < 0) {
            return ResponseEntity.badRequest().header("Failure", "A jobOffers end date cannot be in the past").body(null);
        }

        if (startAndEndDateComparation < 0) {
            return ResponseEntity.badRequest().header("Failure", "A jobOffers end date cannot be before the start date").body(null);
        }

        String currentLogin = SecurityUtils.getCurrentLogin();
        Optional<User> currentUser = userRepository.findOneByLogin(currentLogin);
        jobOffer.setOwner(currentUser.get());
        jobOffer.setApplicationsCount(INITIAL_APPLICATIONS_COUNT);
        jobOffer.setStatus(INITIAL_STATUS);
        JobOffer result = jobOfferRepository.save(jobOffer);
        Statistic totalJobOffers = statisticRepository.getPublishedJobOffers();
        totalJobOffers.setValue(totalJobOffers.getValue() + 1);
        statisticRepository.save(totalJobOffers);

        return ResponseEntity.created(new URI("/api/jobOffers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("jobOffer", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /jobOffers -> Updates an existing jobOffer.
     */
    @RequestMapping(value = "/jobOffers",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<JobOffer> updateJobOffer(@Valid @RequestBody JobOffer jobOffer) throws URISyntaxException {
        log.debug("REST request to update JobOffer : {}", jobOffer);

        if (jobOffer.getStatus() != JobOfferStatus.AVAILABLE) {
            return ResponseEntity.badRequest()
                .header("Failure", "The status of a JobOffer can't be changed")
                .body(null);
        }

        log.debug("REST request to update JobOffer : {}", jobOffer);

        if (jobOffer.getId() == null) {
            return createJobOffer(jobOffer);
        }

        log.debug("REST request to update JobOffer : {}", jobOffer);

        JobOffer result = jobOfferRepository.save(jobOffer);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("jobOffer", jobOffer.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /jobOffers -> Finish an existing jobOffer.
     */
    @RequestMapping(value = "/finish-job-offer",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<JobOffer> finishJobOffer(@Valid @RequestBody JobOffer jobOffer) throws URISyntaxException {
        log.debug("REST request to update JobOffer : {}", jobOffer);

        JobOffer result = jobOfferRepository.save(jobOffer);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("jobOffer", jobOffer.getId().toString()))
            .body(result);
    }

    /**
     * GET  /jobOffers -> get all the jobOffers.
     */
    @RequestMapping(value = "/jobOffers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<JobOffer>> getAllJobOffers(Pageable pageable) throws URISyntaxException {
        List<JobOffer> list = jobOfferRepository.findByOwnerIsCurrentUser();
        Page<JobOffer> page = new Page<JobOffer>() {
            @Override
            public int getTotalPages() {
                return 1;
            }

            @Override
            public long getTotalElements() {
                return list.size();
            }

            @Override
            public int getNumber() {
                return 0;
            }

            @Override
            public int getSize() {
                return list.size();
            }

            @Override
            public int getNumberOfElements() {
                return list.size();
            }

            @Override
            public List<JobOffer> getContent() {
                return list;
            }

            @Override
            public boolean hasContent() {
                return true;
            }

            @Override
            public Sort getSort() {
                return null;
            }

            @Override
            public boolean isFirst() {
                return true;
            }

            @Override
            public boolean isLast() {
                return true;
            }

            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public boolean hasPrevious() {
                return false;
            }

            @Override
            public Pageable nextPageable() {
                return null;
            }

            @Override
            public Pageable previousPageable() {
                return null;
            }

            @Override
            public Iterator<JobOffer> iterator() {
                return list.iterator();
            }
        };
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/jobOffers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /jobOffers/:id -> get the "id" jobOffer.
     */
    @RequestMapping(value = "/jobOffers/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<JobOffer> getJobOffer(@PathVariable Long id) {
        log.debug("REST request to get JobOffer : {}", id);
        return Optional.ofNullable(jobOfferRepository.findOne(id))
            .map(jobOffer -> new ResponseEntity<>(jobOffer, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /jobOffers/:id -> delete the "id" jobOffer.
     */
    @RequestMapping(value = "/jobOffers/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteJobOffer(@PathVariable Long id) {
        log.debug("REST request to delete JobOffer : {}", id);
        jobOfferRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("jobOffer", id.toString())).build();
    }


    /**
     * GET  /jobOffers -> get all the jobOffers.
     */
    @RequestMapping(value = "/offers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<JobOffer>> getAllOffers(Pageable pageable) throws URISyntaxException {
        Page<JobOffer> page = new PageImpl<JobOffer>(jobOfferRepository.findAllCurrent());
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/offers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
