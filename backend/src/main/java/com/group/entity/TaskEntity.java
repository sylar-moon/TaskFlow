package com.group.entity;

import com.group.enumeration.StateEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table (name = "tasks")
public class TaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    private String name;

    private StateEnum state;

    private LocalDateTime dateTimeCreate;

    private long personId;

    public TaskEntity(String name,long personId) {
        this.name = name;
        this.state=StateEnum.NEW;
        dateTimeCreate=LocalDateTime.now();
        this.personId=personId;
    }

    public TaskEntity() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskEntity task = (TaskEntity) o;
        return id == task.id && personId == task.personId && Objects.equals(name, task.name) && state == task.state && Objects.equals(dateTimeCreate, task.dateTimeCreate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, state, dateTimeCreate, personId);
    }
}