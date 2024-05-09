package com.group.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "subtasks")
public class SubTaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    private String name;

    private boolean isComplete;

    public SubTaskEntity() {
    }

    public SubTaskEntity(String name) {
        this.name = name;
        this.isComplete = false;
    }

    public boolean changeStatus(){
        isComplete=!isComplete;
        return isComplete;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubTaskEntity that = (SubTaskEntity) o;
        return id == that.id && isComplete == that.isComplete && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, isComplete);
    }

    @Override
    public String toString() {
        return "SubTaskEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isComplete=" + isComplete +
                '}';
    }
}
