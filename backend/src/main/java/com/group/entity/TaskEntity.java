package com.group.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.group.enumeration.StateEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "tasks")
public class TaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    private String name;

    private StateEnum state;

    private LocalDateTime dateTimeCreate;

    private long personId;


    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "tasks_subtasks", joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "subtask_id"))
    @JsonProperty("subtasks")
    private Set<SubTaskEntity> subtasks = new HashSet<>();


    public TaskEntity(String name, long personId) {
        this.name = name;
        this.state = StateEnum.NEW;
        dateTimeCreate = LocalDateTime.now();
        this.personId = personId;
    }

    public TaskEntity(String name, long personId, Set<SubTaskEntity> subtasks) {
        this.name = name;
        this.state = StateEnum.NEW;
        dateTimeCreate = LocalDateTime.now();
        this.personId = personId;
        this.subtasks = subtasks;
    }

    public TaskEntity() {
    }

    public void addSubtask(SubTaskEntity subtask) {
        subtasks.add(subtask);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskEntity task = (TaskEntity) o;
        return id == task.id && personId == task.personId && Objects.equals(name, task.name) && state == task.state && Objects.equals(dateTimeCreate, task.dateTimeCreate) && Objects.equals(subtasks, task.subtasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, state, dateTimeCreate, personId, subtasks);
    }

    @Override
    public String toString() {
        return "TaskEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", state=" + state +
                ", dateTimeCreate=" + dateTimeCreate +
                ", personId=" + personId +
                ", subtasks=" + subtasks +
                '}';
    }
}