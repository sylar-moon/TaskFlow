package com.group.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.Set;

@RestController
@Setter
@Getter
@Table(name = "persons")
@Entity
public class PersonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    private String name;


    @OneToMany(mappedBy = "person", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Set<RoleEntity> roles;

    public PersonEntity() {
    }

    public PersonEntity(String name, Set<RoleEntity> roles) {
        this.name = name;
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonEntity that = (PersonEntity) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(roles, that.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, roles);
    }
}
