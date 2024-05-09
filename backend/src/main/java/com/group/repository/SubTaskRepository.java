package com.group.repository;

import com.group.entity.SubTaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubTaskRepository extends JpaRepository<SubTaskEntity, Long> {
}
