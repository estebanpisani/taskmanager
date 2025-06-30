package com.esteban.taskmanager.domain;

import com.esteban.taskmanager.domain.enums.PriorityEnum;
import com.esteban.taskmanager.domain.enums.StatusEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Table(name = "tasks")
@Entity
public class Task {

    @Id()
    private UUID id = UUID.randomUUID();
    @Column(name = "title", nullable = false, length = 100)
    private String title;
    @Column(name = "description",length = 255)
    private String description;
    @Column(name = "due_date_time", nullable = false)
    private LocalDateTime dueDateTime;
    @Column(name = "creation_timestamp", nullable = false)
    @CreationTimestamp()
    private LocalDateTime createdAt;
    @Column(name = "update_timestamp", nullable = false)
    @UpdateTimestamp()
    private LocalDateTime updateAt;
    @Column(name = "priority", nullable = false)
    @Enumerated(EnumType.STRING)
    private PriorityEnum priority = PriorityEnum.LOW;
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusEnum status = StatusEnum.TO_DO;

    public Task() {
    }

    public Task(String title, String description, LocalDateTime dueDateTime, PriorityEnum priority, StatusEnum status) {
        this.title = title;
        this.description = description;
        this.dueDateTime = dueDateTime;
        this.priority = priority;
        this.status = status;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public PriorityEnum getPriority() {
        return priority;
    }

    public void setPriority(PriorityEnum priority) {
        this.priority = priority;
    }

    public LocalDateTime getDueDateTime() {
        return dueDateTime;
    }

    public void setDueDateTime(LocalDateTime dueDateTime) {
        this.dueDateTime = dueDateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public UUID getId() {
        return id;
    }

    public void setId(String id) {
        this.id = UUID.fromString(id);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Task task)) return false;
        return Objects.equals(id, task.id) && Objects.equals(title, task.title) && Objects.equals(description, task.description) && Objects.equals(dueDateTime, task.dueDateTime) && priority == task.priority && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, dueDateTime, priority, status);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", dueDateTime=" + dueDateTime +
                ", priority=" + priority +
                ", status=" + status +
                '}';
    }
}
