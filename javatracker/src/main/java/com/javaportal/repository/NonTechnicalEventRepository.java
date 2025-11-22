package com.javaportal.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.javaportal.entity.NonTechnicalEvent;
import com.javaportal.entity.Status;

public interface NonTechnicalEventRepository extends JpaRepository<NonTechnicalEvent, Integer> {

    @Query("SELECT e FROM NonTechnicalEvent e WHERE e.location = :location "
         + "AND e.startTime < :end AND e.endTime > :start")
    List<NonTechnicalEvent> findConflictingEvents(@Param("location") String location,
                                                 @Param("start") LocalDateTime start,
                                                 @Param("end") LocalDateTime end);

    boolean existsByEventName(String eventName);

    @Query("SELECT e FROM NonTechnicalEvent e WHERE e.location = :location "
         + "AND e.nonTechnicalEventId <> :eventId "
         + "AND e.startTime < :end AND e.endTime > :start")
    List<NonTechnicalEvent> findConflictingEventsExcludingCurrent(@Param("location") String location,
                                                                  @Param("start") LocalDateTime start,
                                                                  @Param("end") LocalDateTime end,
                                                                  @Param("eventId") Integer nonTechEventId);

    boolean existsByEventNameAndNonTechnicalEventIdNot(String eventName, Integer nonTechEventId);

    List<NonTechnicalEvent> findByStatus(Status status);

    List<NonTechnicalEvent> findByEventCoordinator_EmpId(Integer empId);

    Optional<NonTechnicalEvent> findByEventName(String eventName);
}
