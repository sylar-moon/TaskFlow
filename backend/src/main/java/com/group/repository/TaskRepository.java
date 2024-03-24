package com.group.repository;

import com.group.entity.PersonEntity;
import com.group.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity,Long> {
    Optional<List<TaskEntity>> findAllByPersonId(Long personId);

}