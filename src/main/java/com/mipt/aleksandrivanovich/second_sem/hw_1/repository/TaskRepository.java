package com.mipt.aleksandrivanovich.second_sem.hw_1.repository;

import com.mipt.aleksandrivanovich.second_sem.hw_1.model.Priority;
import com.mipt.aleksandrivanovich.second_sem.hw_1.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByCompleted(boolean completed);

    List<Task> findByPriority(Priority priority);

    List<Task> findByCompletedAndPriority(boolean completed, Priority priority);

    @Query("SELECT t FROM Task t WHERE t.dueDate BETWEEN :startDate AND :endDate")
    List<Task> findTasksDueInDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT t FROM Task t WHERE t.dueDate <= :date")
    List<Task> findTasksDueBefore(@Param("date") LocalDate date);

    @Query("SELECT t FROM Task t WHERE t.title LIKE %:keyword%")
    List<Task> searchByTitle(@Param("keyword") String keyword);

    @Query("SELECT DISTINCT t FROM Task t LEFT JOIN FETCH t.attachments WHERE t.completed = :completed")
    List<Task> findAllWithAttachmentsByCompleted(@Param("completed") boolean completed);
}