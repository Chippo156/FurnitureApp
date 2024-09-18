package org.ecommerce.ecommerce.services.impl;

import org.ecommerce.ecommerce.models.Role;
import org.ecommerce.ecommerce.repository.RoleRepository;
import org.ecommerce.ecommerce.services.iRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService implements iRoleService {
    @Autowired
    private RoleRepository roleRepository;
    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
}
