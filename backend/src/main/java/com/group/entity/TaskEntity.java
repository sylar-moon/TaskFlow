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

    public TaskEntity(String name) {
        this.name = name;
        this.state=StateEnum.NEW;
        dateTimeCreate=LocalDateTime.now();
    }

    public TaskEntity() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskEntity that = (TaskEntity) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(state, that.state) && Objects.equals(dateTimeCreate, that.dateTimeCreate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, state, dateTimeCreate);
    }
}
