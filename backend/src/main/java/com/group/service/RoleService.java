package com.group.service;

import com.group.entity.RoleEntity;
import com.group.repository.RoleRepository;
import org.hibernate.internal.build.AllowPrintStacktrace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public RoleEntity getUserRole(){
       return roleRepository.findByName("ROLE_USER");
    }

}
