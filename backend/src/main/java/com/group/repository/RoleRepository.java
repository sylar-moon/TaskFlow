package com.group.repository;

import com.group.entity.RoleEntity;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<RoleEntity,Long> {
    RoleEntity findByName(String roleUser);
}
