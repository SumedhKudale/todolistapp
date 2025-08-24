package com.example.tasktracker.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @Column(length = 2000)
    private String notes;

    // Constructors
    public Task() {
        this.createdDate = LocalDateTime.now();
        this.status = Status.PENDING;
        this.category = Category.PERSONAL;
    }

    public Task(String title, String description, Priority priority, Category category) {
        this();
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.category = category;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Priority getPriority() { return priority; }
    public void setPriority(Priority priority) { this.priority = priority; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }

    public LocalDateTime getDueDate() { return dueDate; }
    public void setDueDate(LocalDateTime dueDate) { this.dueDate = dueDate; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    // Enums
    public enum Priority {
        LOW("Low", "#4a5568"),
        MEDIUM("Medium", "#d69e2e"),
        HIGH("High", "#e53e3e");

        private final String displayName;
        private final String color;

        Priority(String displayName, String color) {
            this.displayName = displayName;
            this.color = color;
        }

        public String getDisplayName() { return displayName; }
        public String getColor() { return color; }
    }

    public enum Status {
        PENDING("Pending", "#ed8936"),
        IN_PROGRESS("In Progress", "#4299e1"),
        COMPLETED("Completed", "#48bb78");

        private final String displayName;
        private final String color;

        Status(String displayName, String color) {
            this.displayName = displayName;
            this.color = color;
        }

        public String getDisplayName() { return displayName; }
        public String getColor() { return color; }
    }

    public enum Category {
        WORK("Work", "#667eea"),
        PERSONAL("Personal", "#48bb78"),
        SHOPPING("Shopping", "#ed8936"),
        HEALTH("Health", "#f56565"),
        EDUCATION("Education", "#9f7aea"),
        FINANCE("Finance", "#38b2ac"),
        HOME("Home", "#d69e2e"),
        OTHER("Other", "#a0aec0");

        private final String displayName;
        private final String color;

        Category(String displayName, String color) {
            this.displayName = displayName;
            this.color = color;
        }

        public String getDisplayName() { return displayName; }
        public String getColor() { return color; }
    }
}

