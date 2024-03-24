package com.group.repository;

import com.group.entity.PersonEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PersonRepository extends CrudRepository<PersonEntity,Long> {
    Optional<PersonEntity> findByName(String username);
    Optional<PersonEntity> findByEmail(String email);
}
