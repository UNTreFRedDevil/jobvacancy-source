package com.jobvacancy.repository;

import com.jobvacancy.domain.Statistic;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Statistic entity.
 */
public interface StatisticRepository extends JpaRepository<Statistic,Long> {

}
