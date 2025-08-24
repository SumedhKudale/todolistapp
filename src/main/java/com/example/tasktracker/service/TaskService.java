package com.example.tasktracker.service;

import com.example.tasktracker.model.Task;
import com.example.tasktracker.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public List<Task> getAllTasks() {
        return taskRepository.findAllByOrderByCreatedDateDesc();
    }

    public List<Task> getTasksWithFilters(String search, Task.Status status,
                                          Task.Priority priority, Task.Category category,
                                          String sortBy) {
        return taskRepository.findTasksWithFilters(search, status, priority, category, sortBy);
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    public void deleteTasks(List<Long> ids) {
        taskRepository.deleteAllById(ids);
    }

    public List<Task> getTasksByStatus(Task.Status status) {
        return taskRepository.findByStatusOrderByCreatedDateDesc(status);
    }

    public List<Task> getTasksByPriority(Task.Priority priority) {
        return taskRepository.findByPriorityOrderByCreatedDateDesc(priority);
    }

    public List<Task> getTasksByCategory(Task.Category category) {
        return taskRepository.findByCategoryOrderByCreatedDateDesc(category);
    }

    public Task markAsCompleted(Long id) {
        Optional<Task> taskOpt = taskRepository.findById(id);
        if (taskOpt.isPresent()) {
            Task task = taskOpt.get();
            task.setStatus(Task.Status.COMPLETED);
            return taskRepository.save(task);
        }
        return null;
    }

    public void bulkUpdateStatus(List<Long> taskIds, Task.Status status) {
        List<Task> tasks = taskRepository.findAllById(taskIds);
        for (Task task : tasks) {
            task.setStatus(status);
        }
        taskRepository.saveAll(tasks);
    }

    public void bulkUpdatePriority(List<Long> taskIds, Task.Priority priority) {
        List<Task> tasks = taskRepository.findAllById(taskIds);
        for (Task task : tasks) {
            task.setPriority(priority);
        }
        taskRepository.saveAll(tasks);
    }
}
