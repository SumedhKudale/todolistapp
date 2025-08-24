package com.example.tasktracker.controller;

import com.example.tasktracker.model.Task;
import com.example.tasktracker.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Controller
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping("/")
    public String home(Model model,
                       @RequestParam(required = false) String search,
                       @RequestParam(required = false) String status,
                       @RequestParam(required = false) String priority,
                       @RequestParam(required = false) String category,
                       @RequestParam(required = false, defaultValue = "createdDate") String sortBy) {

        Task.Status statusEnum = null;
        Task.Priority priorityEnum = null;
        Task.Category categoryEnum = null;

        if (status != null && !status.isEmpty()) {
            try { statusEnum = Task.Status.valueOf(status); } catch (Exception e) {}
        }
        if (priority != null && !priority.isEmpty()) {
            try { priorityEnum = Task.Priority.valueOf(priority); } catch (Exception e) {}
        }
        if (category != null && !category.isEmpty()) {
            try { categoryEnum = Task.Category.valueOf(category); } catch (Exception e) {}
        }

        List<Task> tasks = taskService.getTasksWithFilters(search, statusEnum, priorityEnum, categoryEnum, sortBy);

        model.addAttribute("tasks", tasks);
        model.addAttribute("search", search);
        model.addAttribute("status", status);
        model.addAttribute("priority", priority);
        model.addAttribute("category", category);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("statuses", Task.Status.values());
        model.addAttribute("priorities", Task.Priority.values());
        model.addAttribute("categories", Task.Category.values());

        return "tasks";
    }

    @GetMapping("/tasks")
    public String getAllTasks(Model model,
                              @RequestParam(required = false) String search,
                              @RequestParam(required = false) String status,
                              @RequestParam(required = false) String priority,
                              @RequestParam(required = false) String category,
                              @RequestParam(required = false, defaultValue = "createdDate") String sortBy) {
        return home(model, search, status, priority, category, sortBy);
    }

    @GetMapping("/tasks/new")
    public String showAddTaskForm(Model model) {
        model.addAttribute("task", new Task());
        model.addAttribute("priorities", Task.Priority.values());
        model.addAttribute("statuses", Task.Status.values());
        model.addAttribute("categories", Task.Category.values());
        return "add-task";
    }

    @PostMapping("/tasks")
    public String addTask(@ModelAttribute Task task,
                          @RequestParam(required = false) String dueDateString,
                          RedirectAttributes redirectAttributes) {
        if (dueDateString != null && !dueDateString.trim().isEmpty()) {
            LocalDateTime dueDate = LocalDateTime.parse(dueDateString,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
            task.setDueDate(dueDate);
        }
        taskService.saveTask(task);
        redirectAttributes.addFlashAttribute("message", "Task created successfully!");
        return "redirect:/tasks";
    }

    @GetMapping("/tasks/{id}/edit")
    public String showEditTaskForm(@PathVariable Long id, Model model) {
        Task task = taskService.getTaskById(id).orElse(null);
        if (task != null) {
            model.addAttribute("task", task);
            model.addAttribute("priorities", Task.Priority.values());
            model.addAttribute("statuses", Task.Status.values());
            model.addAttribute("categories", Task.Category.values());
            return "edit-task";
        }
        return "redirect:/tasks";
    }

    @PostMapping("/tasks/{id}")
    public String updateTask(@PathVariable Long id, @ModelAttribute Task task,
                             @RequestParam(required = false) String dueDateString,
                             RedirectAttributes redirectAttributes) {
        task.setId(id);
        if (dueDateString != null && !dueDateString.trim().isEmpty()) {
            LocalDateTime dueDate = LocalDateTime.parse(dueDateString,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
            task.setDueDate(dueDate);
        }
        taskService.saveTask(task);
        redirectAttributes.addFlashAttribute("message", "Task updated successfully!");
        return "redirect:/tasks";
    }

    @PostMapping("/tasks/{id}/delete")
    public String deleteTask(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        taskService.deleteTask(id);
        redirectAttributes.addFlashAttribute("message", "Task deleted successfully!");
        return "redirect:/tasks";
    }

    @PostMapping("/tasks/{id}/complete")
    public String markTaskAsCompleted(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        taskService.markAsCompleted(id);
        redirectAttributes.addFlashAttribute("message", "Task marked as completed!");
        return "redirect:/tasks";
    }

    // Bulk Operations
    @PostMapping("/tasks/bulk-delete")
    public String bulkDeleteTasks(@RequestParam("selectedTasks") List<Long> taskIds,
                                  RedirectAttributes redirectAttributes) {
        taskService.deleteTasks(taskIds);
        redirectAttributes.addFlashAttribute("message",
                "Deleted " + taskIds.size() + " task(s) successfully!");
        return "redirect:/tasks";
    }

    @PostMapping("/tasks/bulk-status")
    public String bulkUpdateStatus(@RequestParam("selectedTasks") List<Long> taskIds,
                                   @RequestParam("bulkStatus") String status,
                                   RedirectAttributes redirectAttributes) {
        Task.Status statusEnum = Task.Status.valueOf(status);
        taskService.bulkUpdateStatus(taskIds, statusEnum);
        redirectAttributes.addFlashAttribute("message",
                "Updated status for " + taskIds.size() + " task(s) successfully!");
        return "redirect:/tasks";
    }

    @PostMapping("/tasks/bulk-priority")
    public String bulkUpdatePriority(@RequestParam("selectedTasks") List<Long> taskIds,
                                     @RequestParam("bulkPriority") String priority,
                                     RedirectAttributes redirectAttributes) {
        Task.Priority priorityEnum = Task.Priority.valueOf(priority);
        taskService.bulkUpdatePriority(taskIds, priorityEnum);
        redirectAttributes.addFlashAttribute("message",
                "Updated priority for " + taskIds.size() + " task(s) successfully!");
        return "redirect:/tasks";
    }
}
