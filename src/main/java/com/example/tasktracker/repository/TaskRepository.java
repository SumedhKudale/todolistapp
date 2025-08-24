package com.example.tasktracker.repository;

import com.example.tasktracker.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByStatusOrderByCreatedDateDesc(Task.Status status);
    List<Task> findByPriorityOrderByCreatedDateDesc(Task.Priority priority);
    List<Task> findByCategoryOrderByCreatedDateDesc(Task.Category category);
    List<Task> findAllByOrderByCreatedDateDesc();
    List<Task> findAllByOrderByDueDateAsc();
    List<Task> findAllByOrderByPriorityDesc();
    List<Task> findAllByOrderByTitleAsc();

    @Query("SELECT t FROM Task t WHERE " +
            "(:search IS NULL OR :search = '' OR " +
            "LOWER(t.title) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(t.description) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(t.notes) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "AND (:status IS NULL OR t.status = :status) " +
            "AND (:priority IS NULL OR t.priority = :priority) " +
            "AND (:category IS NULL OR t.category = :category) " +
            "ORDER BY " +
            "CASE WHEN :sortBy = 'dueDate' THEN t.dueDate END ASC, " +
            "CASE WHEN :sortBy = 'priority' THEN t.priority END DESC, " +
            "CASE WHEN :sortBy = 'title' THEN t.title END ASC, " +
            "CASE WHEN :sortBy = 'createdDate' OR :sortBy IS NULL THEN t.createdDate END DESC")
    List<Task> findTasksWithFilters(@Param("search") String search,
                                    @Param("status") Task.Status status,
                                    @Param("priority") Task.Priority priority,
                                    @Param("category") Task.Category category,
                                    @Param("sortBy") String sortBy);
}
