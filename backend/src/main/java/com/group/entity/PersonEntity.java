package com.group.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@RestController
@Setter
@Getter
@Table(name = "persons")
@Entity
public class PersonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "person_seq")
    @SequenceGenerator(name = "person_seq", sequenceName = "persons_seq", allocationSize = 1)
    private long id;

    @NotBlank
    private String name;

    @NotBlank
    private String password;

    @NotBlank
    @Column(unique = true)
    @Email
    private String email;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "persons_roles", joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleEntity> roles = new HashSet<>();

    @NotBlank
    private String userPic;

    public PersonEntity() {
    }

    public PersonEntity(String name, String password, String email, Set<RoleEntity> roles) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.roles = roles;
    }


    public PersonEntity(String name, String password, String email, Set<RoleEntity> roles,String userPic) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.roles = roles;
        this.userPic=userPic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonEntity person = (PersonEntity) o;
        return id == person.id && Objects.equals(name, person.name) && Objects.equals(password, person.password) && Objects.equals(email, person.email) && Objects.equals(roles, person.roles) && Objects.equals(userPic, person.userPic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, password, email, roles, userPic);
    }
}
