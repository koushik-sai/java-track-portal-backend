package com.javaportal.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.javaportal.entity.Status;
import com.javaportal.entity.TrainingEvent;

public interface TrainingEventRepository extends JpaRepository<TrainingEvent, Integer> {

    @Query("SELECT e FROM TrainingEvent e WHERE e.location = :location " +
           "AND e.startTime < :end AND e.endTime > :start")
    List<TrainingEvent> findConflictingEvents(@Param("location") String location,
                                              @Param("start") LocalDateTime start,
                                              @Param("end") LocalDateTime end);

    boolean existsByEventName(String eventName);

    @Query("SELECT e FROM TrainingEvent e WHERE e.location = :location " +
           "AND e.trainingEventId <> :eventId " +
           "AND e.startTime < :end AND e.endTime > :start")
    List<TrainingEvent> findConflictingEventsExcludingCurrent(@Param("location") String location,
                                                              @Param("start") LocalDateTime start,
                                                              @Param("end") LocalDateTime end,
                                                              @Param("eventId") Integer eventId);

    boolean existsByEventNameAndTrainingEventIdNot(String eventName, Integer trainingEventId);

    List<TrainingEvent> findByStatus(Status status);

    List<TrainingEvent> findByOperationsAnchor_EmpId(Integer empId);

    Optional<TrainingEvent> findByEventName(String eventName);
}

