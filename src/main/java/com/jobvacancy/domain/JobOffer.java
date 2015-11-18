package com.jobvacancy.domain;

import com.jobvacancy.domain.enumeration.JobOfferStatus;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * A JobOffer.
 */
@Entity
@Table(name = "JOB_OFFER")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class JobOffer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "location")
    private String location;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private JobOfferStatus status;

    @Column(name = "start_date")
    @Type(type = "date")
    private Date startDate;

    @Column(name = "end_date")
    @Type(type = "date")
    private Date endDate;

    @Column(name = "applications_count")
    private Long applicationsCount;

    @ManyToOne
    private User owner;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public JobOfferStatus getStatus() {
        return status;
    }

    public void setStatus(JobOfferStatus status) {
        this.status = status;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User user) {
        this.owner = user;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Long getApplicationsCount() {
        return this.applicationsCount;
    }

    public void setApplicationsCount(Long applicationsCount) {
        this.applicationsCount = applicationsCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        JobOffer jobOffer = (JobOffer) o;

        if (!Objects.equals(id, jobOffer.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "JobOffer{" +
            "id=" + id +
            ", title='" + title + "'" +
            ", location='" + location + "'" +
            ", description='" + description + "'" +
            ", status='" + status + "'" +
            ", startDate='" + startDate + "'" +
            ", endDate='" + endDate + "'" +
            ", applicationsCount=" + applicationsCount +
            '}';
    }
}
